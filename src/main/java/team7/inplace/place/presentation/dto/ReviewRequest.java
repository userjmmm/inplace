package team7.inplace.place.presentation.dto;

import team7.inplace.review.application.dto.ReviewCommand;

public record ReviewRequest(
    boolean likes,
    String comments
) {

    public static ReviewCommand to(ReviewRequest request) {
        return new ReviewCommand(
            request.likes(),
            request.comments()
        );
    }
}
