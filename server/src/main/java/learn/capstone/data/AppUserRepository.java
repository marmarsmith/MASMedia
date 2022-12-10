package learn.capstone.data;

import learn.capstone.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query(value = "CALL set_known_good_state();", nativeQuery = true)
    void setKnownGoodState();
}
