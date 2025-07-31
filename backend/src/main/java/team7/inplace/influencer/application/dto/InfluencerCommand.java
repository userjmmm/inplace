package team7.inplace.influencer.application.dto;

import influencer.Influencer;

public record InfluencerCommand(
    String influencerName,
    String influencerImgUrl,
    String influencerJob,
    String channelTitle,
    String channelId
) {

    public Influencer toEntity() {
        return new Influencer(
            influencerName(),
            influencerImgUrl(),
            influencerJob(),
            channelTitle(),
            channelId()
        );
    }
}
