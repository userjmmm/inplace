package my.inplace.infra.influencer.jpa;

import my.inplace.domain.influencer.Influencer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InfluencerJpaRepository extends JpaRepository<Influencer, Long> {

    @Override
    @Query("SELECT i FROM Influencer i WHERE i.deleteAt IS NULL AND i.hidden = false AND i.id IN :longs")
    List<Influencer> findAllById(Iterable<Long> longs);

    @Query("SELECT i FROM Influencer i WHERE i.deleteAt IS NULL AND i.hidden = false AND i.name IN :names")
    List<Influencer> findByNameIn(List<String> names);

    @Query("SELECT i.name FROM Influencer i WHERE i.deleteAt IS NULL AND i.hidden = false")
    List<String> findAllInfluencerNames();

    @Query("SELECT i.id FROM Influencer i WHERE i.name = :name")
    Optional<Long> findIdByName(String name);

    List<Influencer> findAllByOrderByUpdateAtAsc();
}
