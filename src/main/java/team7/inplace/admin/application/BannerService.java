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

    public void uploadLogo(BannerCommand.Create command) {
        var imgPath = bannerS3Repository.uploadLogo(command.imgName(), command.imageFile());
        var logo = command.toEntity(imgPath);
        bannerRepository.save(logo);
    }

    public List<Detail> getLogos() {
        var now = LocalDateTime.now();
        var logos = bannerRepository.findActiveLogos(now);

        return logos.stream()
                .sorted((a, b) -> Boolean.compare(b.getIsFixed(), a.getIsFixed()))
                .map(BannerInfo.Detail::from)
                .toList();
    }
}
