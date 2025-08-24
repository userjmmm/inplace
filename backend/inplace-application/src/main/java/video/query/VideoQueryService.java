package video.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import video.jpa.CoolVideoJpaRepository;
import video.jpa.RecentVideoJpaRepository;
import video.query.dto.VideoParam;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoReadRepository videoReadRepository;
    private final CoolVideoJpaRepository coolVideoJpaRepository;
    private final RecentVideoJpaRepository recentVideoJpaRepository;
    
    // TODO 다른 도메인에서 사용하는 거 처리
    @Transactional(readOnly = true)
    public List<VideoQueryResult.DetailedVideo> readVideosBySurround(
        VideoParam.LatAndLon videoSearchParams,
        Pageable pageable
    ) {
        var surroundVideos = videoReadRepository.findSimpleVideosInSurround(
            Double.valueOf(videoSearchParams.topLeftLongitude()),
            Double.valueOf(videoSearchParams.topLeftLatitude()),
            Double.valueOf(videoSearchParams.bottomRightLongitude()),
            Double.valueOf(videoSearchParams.bottomRightLatitude()),
            Double.valueOf(videoSearchParams.longitude()),
            Double.valueOf(videoSearchParams.latitude()),
            pageable
        );

        return surroundVideos.getContent();
    }

    @Transactional(readOnly = true)
    public List<video.RecentVideo> readRecentVideos() {
        return recentVideoJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<video.CoolVideo> readCoolVideos(String parentCategoryName) {
        return coolVideoJpaRepository.findByPlaceCategoryParentName(parentCategoryName);
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.DetailedVideo> readMyInfluencerVideos(Long userId) {
        return videoReadRepository.findTop10ByLikedInfluencer(userId);
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.DetailedVideo> readOneInfluencerVideos(Long influencerId, Pageable pageable) {
        return videoReadRepository.findDetailedVideosWithOneInfluencerId(influencerId, pageable);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<VideoQueryResult.SimpleVideo>> readVideosByPlaceId(List<Long> placeIds) {
        return videoReadRepository.findSimpleVideosByPlaceIds(placeIds);
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> readVideosByPlaceId(Long placeId) {
        return videoReadRepository.findSimpleVideosByPlaceId(placeId);
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.AdminVideo> readAdminVideosByCondition(
        VideoParam.Condition condition, Pageable pageable) {
        return videoReadRepository.findAdminVideoByCondition(condition.toQueryParam(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.SimpleVideo> readVideoWithNoPlace(Pageable pageable) {
        return videoReadRepository.findVideoWithNoPlace(pageable);
    }
}
