package team7.inplace.admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.admin.application.command.LogoCommand;
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
}
