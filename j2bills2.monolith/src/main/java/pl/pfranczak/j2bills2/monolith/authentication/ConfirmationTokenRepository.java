package pl.pfranczak.j2bills2.monolith.authentication;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
	Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
