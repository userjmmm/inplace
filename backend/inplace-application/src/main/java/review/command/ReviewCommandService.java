package review.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.Review;
import review.jpa.ReviewJpaRepository;

@RequiredArgsConstructor
@Service
public class ReviewCommandService {

    private final ReviewJpaRepository reviewJPARepository;

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
