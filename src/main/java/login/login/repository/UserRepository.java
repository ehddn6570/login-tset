package login.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import login.login.domain.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);

}