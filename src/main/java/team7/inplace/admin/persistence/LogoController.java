package team7.inplace.admin.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.admin.application.LogoService;
import team7.inplace.admin.presentation.LogoRequest;

@RestController
@Slf4j
@RequestMapping("/logo")
@RequiredArgsConstructor
public class LogoController {
    private final LogoService logoService;

    @PostMapping()
    public void saveLogo(LogoRequest.Create request) {
        log.info("saveLogo: {}", request);
        logoService.uploadLogo(request.toCommand());
    }
}
