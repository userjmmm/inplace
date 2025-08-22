package video.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;
import video.jpa.CoolVideoJpaRepository;
import video.jpa.RecentVideoJpaRepository;
import video.jpa.VideoJpaRepository;
import video.query.dto.VideoResult;
import video.query.dto.VideoResult.Admin;
import video.query.dto.VideoResult.DetailedVideo;
import video.query.dto.VideoResult.SimpleVideo;
import video.query.dto.VideoParam;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoReadRepository videoReadRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final CoolVideoJpaRepository coolVideoJpaRepository;
    private final RecentVideoJpaRepository recentVideoJpaRepository;

    //TODO: Facade에서 호출로 변경해야함.
    @Transactional(readOnly = true)
    public List<DetailedVideo> getVideosBySurround(
        VideoParam.LatAndLon videoSearchParams,
        Pageable pageable,
        boolean authorizationRequired
    ) {
        // 토큰 정보에 대한 검증
        if (authorizationRequired) {
            AuthorizationUtil.checkLoginUser();
        }
        var surroundVideos = videoReadRepository.findSimpleVideosInSurround(
            Double.valueOf(videoSearchParams.topLeftLongitude()),
            Double.valueOf(videoSearchParams.topLeftLatitude()),
            Double.valueOf(videoSearchParams.bottomRightLongitude()),
            Double.valueOf(videoSearchParams.bottomRightLatitude()),
            Double.valueOf(videoSearchParams.longitude()),
            Double.valueOf(videoSearchParams.latitude()),
            pageable
        );

        return surroundVideos.getContent().stream().map(DetailedVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.RecentVideo> getRecentVideos() {
        return recentVideoJpaRepository.findAll().stream().map(VideoResult.RecentVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.CoolVideo> getCoolVideo(String parentCategoryName) {
        return coolVideoJpaRepository.findByPlaceCategoryParentName(parentCategoryName).stream()
                   .map(VideoResult.CoolVideo::from)
                   .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.DetailedVideo> getMyInfluencerVideos(Long userId) {
        var top10Videos = videoReadRepository.findTop10ByLikedInfluencer(userId);

        return top10Videos.stream().map(DetailedVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<DetailedVideo> getOneInfluencerVideos(
        Long influencerId, Pageable pageable) {
        var videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(influencerId,
            pageable);
        return videos.map(DetailedVideo::from);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<SimpleVideo>> getVideosByPlaceId(List<Long> placeIds) {
        return videoReadRepository.findSimpleVideosByPlaceIds(placeIds).entrySet().stream()
                   .collect(
                       Collectors.toMap(
                           Map.Entry::getKey,
                           entry -> entry.getValue().stream().map(SimpleVideo::from).toList()
                   ));
    }

    @Transactional(readOnly = true)
    public List<SimpleVideo> getVideosByPlaceId(Long placeId) {
        return videoReadRepository.findSimpleVideosByPlaceId(placeId).stream().map(SimpleVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<Admin> getAdminVideosByCondition(
        VideoParam.Condition condition, Pageable pageable) {
        return videoReadRepository.findAdminVideoByCondition(condition.toQueryParam(), pageable).map(Admin::from);
    }

    @Transactional(readOnly = true)
    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoReadRepository.findVideoWithNoPlace(pageable).map(SimpleVideo::from);
    }
}
