package team7.inplace.favoriteInfluencer.presentation.dto;

import team7.inplace.favoriteInfluencer.application.dto.FavoriteInfluencerListCommand;

import java.util.List;

public record InfluencerListLikeRequest(
        List<Long> influencerIds,
        Boolean likes
) {

    public FavoriteInfluencerListCommand toCommand() {
        return new FavoriteInfluencerListCommand(influencerIds, likes);
    }
}
