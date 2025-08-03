package influencer.command;

import exception.InplaceException;
import exception.code.InfluencerErrorCode;
import influencer.Influencer;
import influencer.dto.InfluencerCommand;
import influencer.dto.LikedInfluencerCommand;
import influencer.jpa.InfluencerJpaRepository;
import influencer.jpa.LikedInfluencerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InfluencerCommandService {

    private final InfluencerJpaRepository influencerRepository;
    private final LikedInfluencerJpaRepository likedInfluencerJpaRepository;

    @Transactional
    public Long createInfluencer(InfluencerCommand command) {
        var influencer = command.toEntity();
        influencerRepository.save(influencer);

        return influencer.getId();
    }

    @Transactional
    public Long updateInfluencer(Long id, InfluencerCommand command) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.update(command.influencerName(), command.influencerImgUrl(),
            command.influencerJob());

        return influencer.getId();
    }

    @Transactional
    public Long updateVisibility(Long id) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.changeVisibility();

        return influencer.getId();
    }

    @Transactional
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));

        influencerRepository.delete(influencer);
    }

    @Transactional
    public void likeToInfluencer(LikedInfluencerCommand.Single command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        upsertFavoriteInfluencer(userId, command);
    }

    @Transactional
    public void likeToManyInfluencer(LikedInfluencerCommand.Multiple command) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        for (LikedInfluencerCommand.Single single : command.likes()) {
            upsertFavoriteInfluencer(userId, single);
        }
    }

    private void upsertFavoriteInfluencer(Long userId, LikedInfluencerCommand.Single command) {
        var likedInfluencer = likedInfluencerJpaRepository
            .findByUserIdAndInfluencerId(userId, command.influencerId())
            .orElseGet(() -> {
                var newLikedInfluencer = command.toEntity(userId);
                return likedInfluencerJpaRepository.save(newLikedInfluencer);
            });

        if (command.like()) {
            likedInfluencer.like();
            return;
        }
        likedInfluencer.unlike();
    }

    @Transactional()
    public void updateLastMediumVideo(Long influencerId, String lastVideo) {
        Influencer influencer = influencerRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastMediumVideo(lastVideo);
    }

    @Transactional()
    public void updateLastLongVideo(Long influencerId, String lastLongVideo) {
        Influencer influencer = influencerRepository.findById(influencerId)
            .orElseThrow(() -> InplaceException.of(InfluencerErrorCode.NOT_FOUND));
        influencer.updateLastLongVideo(lastLongVideo);
    }
}
