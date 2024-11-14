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
import team7.inplace.admin.application.BannerService;

@RestController
@Slf4j
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @PostMapping()
    public void saveLogo(BannerRequest.Create request) {
        log.info("saveLogo: {}", request);
        bannerService.uploadLogo(request.toCommand());
    }

    @GetMapping()
    public ResponseEntity<List<BannerResponse.Info>> getLogos() {
        var activeLogos = bannerService.getLogos();

        var response = activeLogos.stream()
                .map(BannerResponse.Info::from)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
