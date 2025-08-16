package video.query.dto;

import video.query.VideoQueryResult;

public class VideoResult { // TODO: VideoQueryResult -> VideoResult 변환

    public record Admin(
        Long id,
        String uuid,
        Boolean registered
    ) {

        public static Admin from(VideoQueryResult.AdminVideo videoInfo) {
            return new Admin(
                videoInfo.videoId(),
                videoInfo.videoUUID(),
                videoInfo.registered()
            );
        }
    }
}
