package pl.pfranczak.j2bills2.monolith.repository.category;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Long>{
	List<SubCategory> findByOwnerOrderByCategory(UserAccount owner);
	List<SubCategory> findByOwnerAndCategoryOrderByCategory(UserAccount owner, Category category);
	List<SubCategory> findByOwnerAndActiveTrueOrderByCategory(UserAccount owner);
	SubCategory findByIdAndOwner(Long id, UserAccount owner);
	SubCategory findByOwnerAndName(UserAccount owner, String name);
	SubCategory findByOwnerAndNameAndCategory(UserAccount owner, String name, Category category);
}
