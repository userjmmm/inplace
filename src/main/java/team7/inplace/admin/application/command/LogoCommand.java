package team7.inplace.admin.application.command;

import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import team7.inplace.admin.domain.Logo;

public class LogoCommand {
    public record Create(
            String imgName,
            MultipartFile imageFile,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Boolean isFixed
    ) {
        public Logo toEntity(String imgPath) {
            return Logo.of(imgName, imgPath, startDate, endDate, isFixed);
        }
    }
}
