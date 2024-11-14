package team7.inplace.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import team7.inplace.user.presentation.dto.LikedInfluencerResponse;
import team7.inplace.user.presentation.dto.LikedPlaceResponse;
import team7.inplace.user.presentation.dto.MyReviewResponse;
import team7.inplace.user.presentation.dto.UserInfoResponse;

public interface UserControllerApiSepc {

    @Operation(
            summary = "유저 닉네임 변경",
            description = "RequestParameter로 받은 유저 닉네임으로 닉네임 변경"
    )
    ResponseEntity<Void> updateNickname(
            @RequestParam String nickname
    );

    @Operation(summary = "좋아요한 인플루언서 반환", description = "좋아요한 인플루언서를 반환합니다.")
    ResponseEntity<Page<LikedInfluencerResponse>> getMyFavoriteInfluencers(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "좋아요한 장소 반환", description = "좋아요한 장소를 반환합니다.")
    ResponseEntity<Page<LikedPlaceResponse>> getMyFavoritePlaces(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "작성한 리뷰 반환", description = "작성한 리뷰를 반환합니다.")
    ResponseEntity<Page<MyReviewResponse>> getMyReviews(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "유저 정보 반환", description = "유저 정보를 반환합니다.")
    ResponseEntity<UserInfoResponse> getUserInfo();
}