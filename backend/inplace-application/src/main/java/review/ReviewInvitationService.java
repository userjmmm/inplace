package review;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.query.PlaceReadRepository;
import review.ReviewInvitation;
import review.jpa.ReviewJpaRepository;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.ReviewErrorCode;
import team7.inplace.review.application.dto.ReviewCommand;
import team7.inplace.review.application.dto.ReviewInfo;
import user.jpa.UserJpaRepository;
import video.query.VideoReadRepository;

@Service
@RequiredArgsConstructor
public class ReviewInvitationService {

    private final ReviewInvitationRedisRepository reviewInvitationRedisRepository;
    private final ReviewJpaRepository reviewJPARepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceReadRepository placeReadRepository;
    private final VideoReadRepository videoReadRepository;

    @Transactional
    public String generateReviewUuid(Long userId, Long placeId) {
        //리뷰가 작성되어 있다면 null을 반환
        var reviewed = reviewJPARepository.existsByUserIdAndPlaceId(userId, placeId);
        if (reviewed) {
            return null;
        }

        //이미 초대장이 존재한다면 해당 초대장의 uuid를 반환
        var existingInvitation = reviewInvitationRedisRepository.findUUIDByUserIdAndPlaceId(
            userId, placeId);
        if (existingInvitation.isPresent()) {
            return existingInvitation.get();
        }

        // uuid 새로 생성
        String newInvitationId = UUID.randomUUID().toString();
        ReviewInvitation newInvitation = new ReviewInvitation(newInvitationId, userId, placeId);
        reviewInvitationRedisRepository.save(newInvitationId, newInvitation);

        return newInvitationId;
    }

    @Transactional
    public void saveReview(ReviewCommand.create command) {
        var reviewInvitation = reviewInvitationRedisRepository.get(command.uuid())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_UUID));

        var review = command.toEntityFrom(reviewInvitation.getUserId(),
            reviewInvitation.getPlaceId());
        reviewJPARepository.save(review);
        reviewInvitationRedisRepository.delete(command.uuid());
    }

    @Transactional(readOnly = true)
    public ReviewInfo.Invitation getReviewInvitation(String invitationId) {
        var invitation = reviewInvitationRedisRepository.get(invitationId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_UUID));

        var place = placeReadRepository.findDetailedPlaceById(invitation.getPlaceId(),
                invitation.getUserId())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_PLACE_ID));
        var user = userJpaRepository.findById(invitation.getUserId())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_USER_ID));
        var videos = videoReadRepository.findSimpleVideosByPlaceId(invitation.getPlaceId());

        return ReviewInfo.Invitation.from(place, videos, user);
    }
}
