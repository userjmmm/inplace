package team7.inplace.influencer.application;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.favoriteInfluencer.persistent.FavoriteInfluencerRepository;
import team7.inplace.influencer.application.dto.InfluencerCommand;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.persistence.UserRepository;

@RequiredArgsConstructor
@Service
public class InfluencerService {

    private final InfluencerRepository influencerRepository;
    private final FavoriteInfluencerRepository favoriteRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<InfluencerInfo> getAllInfluencers() {
        List<Influencer> influencers = influencerRepository.findAll();
        Long userId = AuthorizationUtil.getUserId();

        // 로그인 안된 경우, likes를 모두 false로 설정
        if (userId == null) {
            return influencers.stream()
                .map(influencer -> InfluencerInfo.from(influencer, false))
                .toList();
        }

        // 로그인 된 경우
        Set<Long> likedInfluencerIds = favoriteRepository.findLikedInfluencerIdsByUserId(userId);

        List<InfluencerInfo> influencerInfos = influencers.stream()
            .map(influencer -> {
                boolean isLiked = likedInfluencerIds.contains(influencer.getId());
                return InfluencerInfo.from(influencer, isLiked);
            })
            .sorted((a, b) -> Boolean.compare(b.likes(), a.likes()))
            .toList();

        return influencerInfos;
    }

    @Transactional
    public Long createInfluencer(InfluencerCommand command) {
        Influencer influencer = InfluencerCommand.to(command);
        return influencerRepository.save(influencer).getId();
    }

    @Transactional
    public Long updateInfluencer(Long id, InfluencerCommand command) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow();
        influencer.update(command.influencerName(), command.influencerImgUrl(),
            command.influencerJob());

        return influencer.getId();
    }

    @Transactional
    public void deleteInfluencer(Long id) {
        Influencer influencer = influencerRepository.findById(id).orElseThrow();

        influencerRepository.delete(influencer);
    }
}
