package learn.capstone.data;

import learn.capstone.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query(value = "CALL set_known_good_state();", nativeQuery = true)
    void setKnownGoodState();
}
