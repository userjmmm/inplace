package influencer.query.dto;

import influencer.Influencer;

public class InfluencerResult {

    public record Detail(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        boolean likes
    ) {

        public static Detail from(Influencer influencer, boolean isLiked) {
            return new Detail(
                influencer.getId(),
                influencer.getName(),
                influencer.getImgUrl(),
                influencer.getJob(),
                isLiked
            );
        }
    }

    public record Name(
        String name
    ) {

    }

}
