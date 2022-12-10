package learn.capstone.data;

import learn.capstone.models.Genre;
import learn.capstone.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository

public interface MediaRepository extends JpaRepository<Media, Integer> {
    @Query(value = "CALL set_known_good_state();", nativeQuery = true)
    void setKnownGoodState();

//    List<Media> findByMediaIdIn(Collection<Integer> mediaIds);


}//end
