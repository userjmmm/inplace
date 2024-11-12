package team7.inplace.search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.search.persistence.InfluencerSearchRepository;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final InfluencerSearchRepository influencerSearchRepository;

    @GetMapping("/influencer")
    public void searchInfluencer(@RequestParam("value") String value) {
        System.out.println(influencerSearchRepository.searchAutoCompletions(value));
    }

}
