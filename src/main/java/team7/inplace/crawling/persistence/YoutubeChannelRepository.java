package team7.inplace.crawling.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.crawling.domain.YoutubeChannel;

import java.util.Optional;

public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {
    Optional<YoutubeChannel> findYoutubeChannelByPlayListUUID(String playListUUID);
}
