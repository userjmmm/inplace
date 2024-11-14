package team7.inplace.admin.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.admin.application.LogoService;

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

    @GetMapping()
    public ResponseEntity<List<LogoResponse.Info>> getLogos() {
        var activeLogos = logoService.getLogos();

        var response = activeLogos.stream()
                .map(LogoResponse.Info::from)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
