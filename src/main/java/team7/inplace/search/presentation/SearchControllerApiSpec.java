package team7.inplace.search.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import team7.inplace.influencer.presentation.dto.InfluencerResponse;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.video.presentation.dto.VideoResponse;

@Tag(name = "검색 API입니다.")
public interface SearchControllerApiSpec {

    @Operation(summary = "추천 검색어를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "검색어 조회 성공")
    ResponseEntity<List<AutoCompletionInfo>> searchKeywords(String value);

    @Operation(summary = "비디오를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "비디오 검색 성공")
    ResponseEntity<List<VideoResponse>> searchVideo(String value);

    @Operation(summary = "인플루언서를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "인플루언서 검색 성공")
    ResponseEntity<List<InfluencerResponse>> searchInfluencer(String value);
}
