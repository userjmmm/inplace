package team7.inplace.review.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.ReviewErrorCode;
import team7.inplace.place.domain.Place;
import team7.inplace.place.persistence.PlaceRepository;
import team7.inplace.review.application.dto.ReviewCommand;
import team7.inplace.review.application.dto.ReviewInfo;
import team7.inplace.review.domain.Review;
import team7.inplace.review.persistence.ReviewRepository;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public void createReview(Long placeId, ReviewCommand command) {
        Long userId = AuthorizationUtil.getUserId();
        if (userId == null) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        User user = userRepository.findById(userId).orElseThrow();
        Place place = placeRepository.findById(placeId).orElseThrow();

        if (reviewRepository.existsByUserIdAndPlaceId(userId, placeId)) {
            throw InplaceException.of(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        Review review = ReviewCommand.toEntity(user, place, command);
        reviewRepository.save(review);
    }

    public List<ReviewInfo> getReviews(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow();
        List<Review> reviews = reviewRepository.findByPlaceId(placeId);
        return reviews.stream()
            .map(ReviewInfo::from)
            .toList();
    }
}
