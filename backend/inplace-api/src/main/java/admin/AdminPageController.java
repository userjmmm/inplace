package admin;

import banner.jpa.BannerJpaRepository;
import influencer.jpa.InfluencerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import place.Category;
import place.jpa.CategoryRepository;
import post.jpa.CommentJpaRepository;
import post.jpa.PostJpaRepository;
import properties.GoogleApiProperties;
import properties.KakaoApiProperties;
import team7.inplace.admin.dto.CategoryForm;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.CategoryErrorCode;
import team7.inplace.place.application.PlaceService;
import team7.inplace.post.application.PostService;
import team7.inplace.video.application.VideoService;
import team7.inplace.video.persistence.VideoRepository;
import video.VideoResponse;
import video.query.VideoQueryFacade;
import video.query.VideoQueryParam;
import video.query.dto.VideoParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final KakaoApiProperties kakaoApiProperties;
    private final GoogleApiProperties googleApiProperties;
    private final VideoRepository videoRepository;
    private final BannerJpaRepository bannerJpaRepository;
    private final InfluencerJpaRepository influencerRepository;
    private final CategoryRepository categoryRepository;
    private final PostJpaRepository postJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final VideoQueryFacade videoQueryFacade;
    private final PlaceService placeService;
    private final PostService postService;

    @GetMapping("/video")
    public String getVideos(
        @RequestParam(required = false) Long influencerId,
        @RequestParam(required = false, defaultValue = "false") Boolean videoRegistration,
        @PageableDefault Pageable pageable, Model model
    ) {
        Page<VideoResponse.Admin> videoPage = videoQueryFacade
            .getAdminVideosByCondition(VideoParam.Condition.of(videoRegistration, influencerId),
                pageable)
            .map(VideoResponse.Admin::from);

        model.addAttribute("videoPage", videoPage);
        model.addAttribute("influencers", influencerRepository.findAll());
        model.addAttribute("selectedInfluencerId", influencerId);
        model.addAttribute("videoRegistration", videoRegistration);
        model.addAttribute("kakaoApiKey", kakaoApiProperties.jsKey());
        model.addAttribute("googleApiKey", googleApiProperties.placeKey1());
        model.addAttribute("categories", categoryRepository.findSubCategories());
        return "admin/video.html";
    }

    @GetMapping("/banner")
    public String getBanners(Model model) {
        var banners = bannerJpaRepository.findAll();

        model.addAttribute("banners", banners);
        return "admin/banner.html";
    }

    @GetMapping("/influencer/new")
    public String getInfluencers(Model model) {
        model.addAttribute("youtubeApiKey", googleApiProperties.crawlingKey());
        return "admin/influencer/new.html";
    }

    @GetMapping("/influencer/list")
    public String getInfluencerList(Model model) {
        model.addAttribute("influencers", influencerRepository.findAll());
        return "admin/influencer/list.html";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "admin/main.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "admin/login.html";
    }

    @GetMapping("/category")
    public String getCategoryPage(Model model) {
        model.addAttribute("parentCategories", categoryRepository.findParentCategories());
        return "admin/category/list.html";
    }

    @GetMapping("/category/{categoryId}/edit")
    public String getCategoryEditForm(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> InplaceException.of(CategoryErrorCode.NOT_FOUND));
        CategoryForm categoryForm = CategoryForm.of(category);
        model.addAttribute("categoryForm", categoryForm);
        return "admin/category/edit.html";
    }

    @PostMapping("/category/{categoryId}/edit")
    public String editCategory(
        @PathVariable Long categoryId, @ModelAttribute CategoryForm categoryForm) {
        placeService.updateCategory(categoryId, categoryForm);
        return "redirect:/admin/category";
    }

    @GetMapping("/category/add")
    public String getCategoryAddForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "admin/category/add.html";
    }

    @PostMapping("/category/add")
    public String saveCategory(@ModelAttribute CategoryForm categoryForm) {
        categoryRepository.save(categoryForm.toEntity());
        return "redirect:/admin/category";
    }

    @GetMapping("/report")
    public String getReportPage(Model model) {
        model.addAttribute("reportedPosts",
            postJpaRepository.findAllByIsReportedTrueAndDeleteAtIsNull());
        model.addAttribute("reportedComments",
            commentJpaRepository.findAllByIsReportedTrueAndDeleteAtIsNull());
        return "admin/report";
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePostSoftly(postId);
        return "redirect:/admin/report";
    }

    @PostMapping("/post/unreport/{postId}")
    public String unreportPost(@PathVariable Long postId) {
        postService.unreportPost(postId);
        return "redirect:/admin/report";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        postService.deleteCommentSoftly(commentId);
        return "redirect:/admin/report";
    }

    @PostMapping("/comment/unreport/{commentId}")
    public String unreportComment(@PathVariable Long commentId) {
        postService.unreportComment(commentId);
        return "redirect:/admin/report";
    }


}
