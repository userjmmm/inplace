package qrcode.query;

import annotation.Facade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import place.query.PlaceQueryService;
import util.AuthorizationUtil;

@Facade
@Slf4j
@RequiredArgsConstructor
public class QRCodeQueryFacade {

    private final QrCodeQueryService qrCodeCommandService;
    private final PlaceQueryService placeService;

    public byte[] getQRCode(Long placeId, Integer width, Integer height) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var place = placeService.getPlaceInfo(userId, placeId);

        return qrCodeCommandService.generateQRCode(place.kakaoPlaceId(), width, height);
    }

}
