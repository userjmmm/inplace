package team7.inplace.user.presentation.dto;

import team7.inplace.influencer.application.dto.InfluencerInfo;

public record LikedInfluencerResponse(
    Long influencerId,
    String influencerName,
    String influencerImgUrl,
    String influencerJob,
    boolean likes
) {

    public static LikedInfluencerResponse from(InfluencerInfo influencerInfo) {
        return new LikedInfluencerResponse(
            influencerInfo.influencerId(),
            influencerInfo.influencerName(),
            influencerInfo.influencerImgUrl(),
            influencerInfo.influencerJob(),
            influencerInfo.likes()
        );
    }
}
