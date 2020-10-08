package pl.pfranczak.j2bills2.monolith.authentication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
	Optional<UserAccount> findByEmail(String email);
}
