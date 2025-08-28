package my.inplace.api.influencer.dto;

import my.inplace.application.influencer.query.dto.InfluencerResult;
import my.inplace.application.video.query.dto.VideoResult;

public class InfluencerResponse {

    //TODO: 클래스 이름 변경해야함
    public record Simple(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        boolean likes
    ) {

        public static InfluencerResponse.Simple from(InfluencerResult.Simple influencerInfo) {
            return new InfluencerResponse.Simple(
                influencerInfo.id(),
                influencerInfo.name(),
                influencerInfo.imgUrl(),
                influencerInfo.job(),
                influencerInfo.isLiked()
            );
        }
    }

    public record Name(
        String influencerName
    ) {

        public static Name from(InfluencerResult.Name influencerInfo) {
            return new Name(influencerInfo.name());
        }
    }

    public record Detail(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        Boolean likes,
        Long follower,
        Long placeCount
    ) {

        public static Detail from(InfluencerResult.Detail influencerInfo) {
            return new Detail(
                influencerInfo.id(),
                influencerInfo.name(),
                influencerInfo.imgUrl(),
                influencerInfo.job(),
                influencerInfo.isLiked(),
                influencerInfo.follower(),
                influencerInfo.placeCount()
            );
        }
    }

    public record Video(
        Long videoId,
        String influencerName,
        String videoUrl,
        InfluencerResponse.Place place
    ) {

        public static InfluencerResponse.Video from(VideoResult.DetailedVideo videoInfo) {
            var place = new InfluencerResponse.Place(
                videoInfo.placeId(),
                videoInfo.placeName(),
                new Address(
                    videoInfo.address1(),
                    videoInfo.address2(),
                    videoInfo.address3()
                )
            );
            return new InfluencerResponse.Video(
                videoInfo.videoId(),
                videoInfo.influencerName(),
                videoInfo.videoUrl(),
                place
            );
        }
    }

    public record Place(
        Long placeId,
        String placeName,
        Address address
    ) {

    }

    public record Address(
        String address1,
        String address2,
        String address3
    ) {

    }
}
