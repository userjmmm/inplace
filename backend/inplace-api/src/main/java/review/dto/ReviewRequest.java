package review.dto;


public class ReviewRequest {

    public record Save(
        Boolean likes,
        String comments
    ) {

        public ReviewCommand.create toCommandFrom(String uuid) {
            return new ReviewCommand.create(uuid, likes, comments);
        }
    }
}
