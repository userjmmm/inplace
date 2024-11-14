package team7.inplace.admin.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.admin.application.command.BannerCommand;
import team7.inplace.admin.application.dto.BannerInfo;
import team7.inplace.admin.application.dto.BannerInfo.Detail;
import team7.inplace.admin.persistence.BannerRepository;
import team7.inplace.admin.persistence.BannerS3Repository;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerS3Repository bannerS3Repository;
    private final BannerRepository bannerRepository;

    public void uploadBanner(BannerCommand.Create command) {
        var imgPath = bannerS3Repository.uploadBanner(command.imageFile());
        var banner = command.toEntity(imgPath);
        bannerRepository.save(banner);
    }

    public List<Detail> getBanners() {
        var now = LocalDateTime.now();
        var banners = bannerRepository.findActiveBanner(now);

        return banners.stream()
                .sorted((a, b) -> Boolean.compare(b.getIsFixed(), a.getIsFixed()))
                .map(BannerInfo.Detail::from)
                .toList();
    }
}
