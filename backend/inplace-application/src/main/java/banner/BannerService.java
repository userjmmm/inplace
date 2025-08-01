package banner;

import banner.dto.BannerCommand;
import banner.dto.BannerInfo;
import banner.jpa.BannerJpaRepository;
import exception.InplaceException;
import exception.code.BannerErrorCode;
import influencer.jpa.InfluencerJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerService { // TODO

    private final BannerS3Repository bannerS3Repository;
    private final BannerJpaRepository bannerRepository;
    private final InfluencerJpaRepository influencerRepository;

    public void uploadBanner(BannerCommand.Create command) {
        var imgPath = bannerS3Repository.uploadBanner(command.imageFile());
        Long influencerId = null;
        if (!command.isMain()) {
            influencerId = influencerRepository.findIdByName(command.imgName())
                .orElseThrow(() -> InplaceException.of(BannerErrorCode.INFLUENCER_NOT_FOUND));
        }
        var banner = command.toEntity(imgPath, influencerId);
        bannerRepository.save(banner);
    }

    public List<BannerInfo.Detail> getBanners() {
        var now = LocalDateTime.now();
        var banners = bannerRepository.findActiveBanner(now);

        return banners.stream()
            .sorted((a, b) -> Boolean.compare(b.getIsFixed(), a.getIsFixed()))
            .map(BannerInfo.Detail::from)
            .toList();
    }

    public void deleteBanner(Long id) {
        var banner = bannerRepository.findById(id)
            .orElseThrow(() -> InplaceException.of(BannerErrorCode.NOT_FOUND));
        bannerS3Repository.deleteBanner(banner.getImgPath());
        bannerRepository.delete(banner);
    }
}
