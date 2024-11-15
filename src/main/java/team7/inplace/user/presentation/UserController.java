package team7.inplace.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team7.inplace.user.application.UserFacade;
import team7.inplace.user.application.UserService;
import team7.inplace.user.presentation.dto.LikedInfluencerResponse;
import team7.inplace.user.presentation.dto.LikedPlaceResponse;
import team7.inplace.user.presentation.dto.MyReviewResponse;
import team7.inplace.user.presentation.dto.UserInfoResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerApiSepc {

    public final UserService userService;
    public final UserFacade userFacade;

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
            @RequestParam String nickname
    ) {
        userService.updateNickname(nickname);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/influencers")
    public ResponseEntity<Page<LikedInfluencerResponse>> getMyFavoriteInfluencers(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<LikedInfluencerResponse> influencers = userFacade.getMyFavoriteInfluencers(pageable)
                .map(LikedInfluencerResponse::from);
        return new ResponseEntity<>(influencers, HttpStatus.OK);
    }

    @GetMapping("/places")
    public ResponseEntity<Page<LikedPlaceResponse>> getMyFavoritePlaces(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<LikedPlaceResponse> places = userFacade.getMyFavoritePlaces(pageable)
                .map(LikedPlaceResponse::from);
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<MyReviewResponse>> getMyReviews(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<MyReviewResponse> reviews = userFacade.getMyReviews(pageable)
                .map(MyReviewResponse::from);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        UserInfoResponse response = UserInfoResponse.from(userService.getUserInfo());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
