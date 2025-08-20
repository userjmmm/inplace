package influencer.query.dto;

import influencer.Influencer;
import influencer.query.InfluencerQueryResult;

public class InfluencerResult {

    public record Detail(
        Long id,
        String name,
        String imgUrl,
        String job,
        Boolean isLiked,
        Long follower,
        Long placeCount
    ) {

        public static Detail from(InfluencerQueryResult.Detail influencer) {
            return new Detail(
                influencer.id(),
                influencer.name(),
                influencer.imgUrl(),
                influencer.job(),
                influencer.isLiked(),
                influencer.follower(),
                influencer.placeCount()
            );
        }

    }

    public record Simple(
        Long id,
        String name,
        String imgUrl,
        String job,
        boolean isLiked
    ) {

        public static Simple from(Influencer influencer, boolean isLiked) {
            return new Simple(
                influencer.getId(),
                influencer.getName(),
                influencer.getImgUrl(),
                influencer.getJob(),
                isLiked
            );
        }

        public static Simple from(InfluencerQueryResult.Simple influencer) {
            return new Simple(
                influencer.id(),
                influencer.name(),
                influencer.imgUrl(),
                influencer.job(),
                influencer.isLiked()
            );
        }
    }

    public record Name(
        String name
    ) {

    }

}
