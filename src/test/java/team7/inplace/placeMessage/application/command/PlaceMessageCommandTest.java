package team7.inplace.placeMessage.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlaceMessageCommandTest {

    @Test
    void videoUrltoImageUrlTest() {
        String videoUrl = "https://www.youtube.com/watch?v=6T7fWf5EA0s";
        String expectImageUrl = "https://img.youtube.com/vi/6T7fWf5EA0s/maxresdefault.jpg";
        assertThat(PlaceMessageCommand.videoUrlToImgUrl(videoUrl)).isEqualTo(expectImageUrl);
    }

}
