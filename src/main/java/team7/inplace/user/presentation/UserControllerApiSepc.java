package team7.inplace.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserControllerApiSepc {
    @Operation(
            summary = "유저 닉네임 변경",
            description = "RequestParameter로 받은 유저 닉네임으로 닉네임 변경"
    )
    ResponseEntity<Void> updateNickname(
            @RequestParam String nickname
    );
}