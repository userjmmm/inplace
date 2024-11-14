package team7.inplace.admin.banner.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.admin.banner.application.command.BannerCommand.Create;
import team7.inplace.admin.banner.application.dto.BannerInfo;
import team7.inplace.admin.banner.application.dto.BannerInfo.Detail;
import team7.inplace.admin.banner.persistence.BannerRepository;
import team7.inplace.admin.banner.persistence.BannerS3Repository;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerS3Repository bannerS3Repository;
    private final BannerRepository bannerRepository;

    public void uploadBanner(Create command) {
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

    public void deleteBanner(Long id) {
        var banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배너입니다."));
        bannerS3Repository.deleteBanner(banner.getImgPath());
        bannerRepository.delete(banner);
    }
}
