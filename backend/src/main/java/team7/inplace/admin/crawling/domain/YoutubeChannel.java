package team7.inplace.admin.crawling.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Entity(name = "youtube_channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YoutubeChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long influencerId;

    private String channelUUID;
    private String playListUUID;
    private String lastVideoUUID;

    @Enumerated(value = STRING)
    private ChannelType channelType;

    public String getChannelTypeCode() {
        return channelType.getCode();
    }

    public void updateLastVideoUUID(String lastVideoUUID) {
        this.lastVideoUUID = lastVideoUUID;
    }
}