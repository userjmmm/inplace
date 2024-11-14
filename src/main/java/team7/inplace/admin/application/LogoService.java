package team7.inplace.admin.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.admin.application.command.LogoCommand;
import team7.inplace.admin.application.dto.LogoInfo;
import team7.inplace.admin.application.dto.LogoInfo.Detail;
import team7.inplace.admin.persistence.LogoRepository;
import team7.inplace.admin.persistence.LogoS3Repository;

@Service
@RequiredArgsConstructor
public class LogoService {
    private final LogoS3Repository logoS3Repository;
    private final LogoRepository logoRepository;

    public void uploadLogo(LogoCommand.Create command) {
        var imgPath = logoS3Repository.uploadLogo(command.imgName(), command.imageFile());
        var logo = command.toEntity(imgPath);
        logoRepository.save(logo);
    }

    public List<Detail> getLogos() {
        var now = LocalDateTime.now();
        var logos = logoRepository.findActiveLogos(now);

        return logos.stream()
                .sorted((a, b) -> Boolean.compare(b.getIsFixed(), a.getIsFixed()))
                .map(LogoInfo.Detail::from)
                .toList();
    }
}
