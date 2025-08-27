package video.query;

import annotation.Facade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;
import video.query.dto.VideoParam;
import video.query.dto.VideoResult;
import video.query.dto.VideoResult.DetailedVideo;
import video.query.dto.VideoResult.SimpleVideo;

@Facade
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoQueryFacade {

    private final VideoQueryService videoQueryService;

    public List<DetailedVideo> getMyInfluencerVideos() {
        // User 정보를 쿠키에서 추출
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        // 인플루언서 id를 사용하여 영상을 조회
        return videoQueryService.getMyInfluencerVideos(userId);
    }

    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoQueryService.getVideoWithNoPlace(pageable);
    }

    public Page<VideoResult.Admin> getAdminVideosByCondition(
        VideoParam.Condition condition, Pageable pageable
    ) {
        return videoQueryService.getAdminVideosByCondition(condition, pageable);
    }
}
