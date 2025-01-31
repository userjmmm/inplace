package team7.inplace.influencer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team7.inplace.config.annotation.CustomRepositoryTest;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.persistence.InfluencerRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CustomRepositoryTest
public class InfluencerRepositoryTest {

    @Autowired
    private InfluencerRepository influencerRepository;

    @Test
    public void findAllTest() {
        Influencer influencer4 = new Influencer("influencer4", "imgUrl1", "job1");
        Influencer influencer5 = new Influencer("influencer5", "imgUrl2", "job2");

        influencerRepository.save(influencer4);
        influencerRepository.save(influencer5);

        List<Influencer> savedInfluencers = influencerRepository.findAll();

        assertThat(savedInfluencers.get(0)).usingRecursiveComparison().isEqualTo(influencer4);
        assertThat(savedInfluencers.get(1)).usingRecursiveComparison().isEqualTo(influencer5);
    }

}
