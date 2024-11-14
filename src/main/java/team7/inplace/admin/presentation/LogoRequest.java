package team7.inplace.admin.presentation;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import team7.inplace.admin.application.command.LogoCommand;

public class LogoRequest {
    public record Create(
            String imageName,
            MultipartFile imageFile,
            LocalDate startDate,
            LocalDate endDate,
            Boolean isFixed
    ) {
        public LogoCommand.Create toCommand() {
            return new LogoCommand.Create(
                    imageName,
                    imageFile,
                    startDate.atStartOfDay(),
                    endDate.atStartOfDay(),
                    isFixed
            );
        }
    }
}
