package team7.inplace.search.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import team7.inplace.search.application.dto.AutoCompletionInfo;

@Tag(name = "검색 API입니다.")
public interface SearchControllerApiSpec {

    @Operation(summary = "추천 검색어를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "검색어 조회 성공")
    ResponseEntity<List<AutoCompletionInfo>> searchKeywords(String value);
}
