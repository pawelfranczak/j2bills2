package pl.pfranczak.j2bills2.monolith.repository.category;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{
	List<Category> findByOwner(UserAccount owner);
	List<Category> findByOwnerAndActiveTrue(UserAccount owner);
	Category findByIdAndOwner(Long id, UserAccount owner);
	Category findByOwnerAndName(UserAccount owner, String name);
}
