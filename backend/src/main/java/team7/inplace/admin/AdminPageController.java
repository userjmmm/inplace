package team7.inplace.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import team7.inplace.admin.banner.persistence.BannerRepository;
import team7.inplace.global.kakao.config.KakaoApiProperties;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.VideoRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {
    private final KakaoApiProperties kakaoApiProperties;
    private final VideoRepository videoRepository;
    private final BannerRepository bannerRepository;

    @GetMapping("/video")
    public String getVideos(@PageableDefault Pageable pageable, Model model) {
        Page<Video> videoPage = videoRepository.findAllByPlaceIsNull(pageable);
        model.addAttribute("videos", videoPage.getContent());
        model.addAttribute("currentPage", videoPage.getNumber());
        model.addAttribute("totalPages", videoPage.getTotalPages());
        model.addAttribute("isFirst", videoPage.isFirst());
        model.addAttribute("isLast", videoPage.isLast());
        model.addAttribute("kakaoApiKey", kakaoApiProperties.jsKey());
        return "admin/video.html";
    }

    @GetMapping("/banner")
    public String getBanners(Model model) {
        var banners = bannerRepository.findAll();

        model.addAttribute("banners", banners);
        return "admin/banner.html";
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "admin/main.html";
    }
}
