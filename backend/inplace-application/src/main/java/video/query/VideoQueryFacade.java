package video.query;

import annotation.Facade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;
import video.query.dto.VideoResult.DetailedVideo;
import video.query.dto.VideoParam;
import video.query.dto.VideoResult;

@Facade
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoQueryFacade {
    private final VideoQueryService videoQueryService;
    
    public List<VideoResult.DetailedVideo> getVideosBySurround(
        VideoParam.LatAndLon videoSearchParams,
        Pageable pageable
    ) {
        return videoQueryService.readVideosBySurround(videoSearchParams, pageable).stream()
                   .map(DetailedVideo::from)
                   .toList();
    }
    
    public List<VideoResult.RecentVideo> getRecentVideos() {
        return videoQueryService.readRecentVideos().stream()
                   .map(VideoResult.RecentVideo::from)
                   .toList();
    }
    
    public List<VideoResult.CoolVideo> getCoolVideos(String parentCategoryName) {
        return videoQueryService.readCoolVideos(parentCategoryName).stream()
                   .map(VideoResult.CoolVideo::from)
                   .toList();
    }
    
    public List<VideoResult.DetailedVideo> getMyInfluencerVideos() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        
        return videoQueryService.readMyInfluencerVideos(userId).stream()
                   .map(VideoResult.DetailedVideo::from)
                   .toList();
    }
    
    // ADMIN 메서드
    public Page<VideoResult.Admin> getAdminVideosByCondition(VideoParam.Condition condition, Pageable pageable) {
        return videoQueryService.readAdminVideosByCondition(condition, pageable)
                   .map(VideoResult.Admin::from);
    }
    
    public Page<VideoResult.SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoQueryService.readVideoWithNoPlace(pageable)
                   .map(VideoResult.SimpleVideo::from);
    }
}
