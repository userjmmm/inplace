package influencer.dto;

import influencer.LikedInfluencer;
import java.util.List;

public class LikedInfluencerCommand {

    public record Single(
        Long influencerId,
        Boolean like
    ) {

        public LikedInfluencer toEntity(Long userId) {
            return new LikedInfluencer(userId, influencerId, like);
        }
    }

    public record Multiple(
        List<Single> likes
    ) {

    }
}
