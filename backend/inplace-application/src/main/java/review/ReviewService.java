package review;

import exception.InplaceException;
import exception.code.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.dto.ReviewResult;
import review.jpa.ReviewJpaRepository;
import review.query.ReviewQueryResult;
import review.query.ReviewReadRepository;
import util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewReadRepository reviewReadRepository;
    private final ReviewJpaRepository reviewJPARepository;

    @Transactional(readOnly = true)
    public Page<ReviewResult.Simple> getPlaceReviews(Long placeId, Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrNull();

        return reviewReadRepository
            .findSimpleReviewByUserIdAndPlaceId(placeId, userId, pageable)
            .map(ReviewResult.Simple::from);
    }


    @Transactional(readOnly = true)
    public Page<ReviewQueryResult.Detail> getUserReviews(Long userId, Pageable pageable) {
        return reviewReadRepository.findDetailedReviewByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public ReviewQueryResult.LikeRate getReviewLikeRate(Long reviewId) {
        return reviewReadRepository.countRateByPlaceId(reviewId);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        Review review = reviewJPARepository.findById(reviewId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.NOT_FOUND));

        if (review.isNotOwner(userId)) {
            throw InplaceException.of(ReviewErrorCode.NOT_OWNER);
        }
        reviewJPARepository.delete(review);
    }
}
