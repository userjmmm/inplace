package influencer.dto;

import influencer.Influencer;

public record InfluencerInfo(
    Long influencerId,
    String influencerName,
    String influencerImgUrl,
    String influencerJob,
    boolean likes
) {

    public static InfluencerInfo from(Influencer influencer, boolean isLiked) {
        return new InfluencerInfo(
            influencer.getId(),
            influencer.getName(),
            influencer.getImgUrl(),
            influencer.getJob(),
            isLiked
        );
    }
}
