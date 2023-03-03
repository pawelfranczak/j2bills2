package pl.pfranczak.j2bills2.monolith.service.category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;
import pl.pfranczak.j2bills2.monolith.repository.category.SubCategoryRepository;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class SubCategoryService extends CrudServiceImpl<SubCategory, Long>{
	
	SubCategoryRepository subCategoryRepository;
	UserRepository userRepository;

	public SubCategoryService(SubCategoryRepository repository, UserRepository userRepository) {
		super.setRepository(repository);
		this.subCategoryRepository = repository;
	}
	
	@Override
	public List<SubCategory> getAll() {
		return subCategoryRepository.findByOwnerOrderByCategory(getOwner());
	}
	
	public List<SubCategory> getAllWithActiveCategory() {
		List<SubCategory> subCategories = subCategoryRepository.findByOwnerAndActiveTrueOrderByCategory(getOwner());
		List<SubCategory> subCategoriesWithActiveCategory = subCategories.stream().filter(subCategory -> subCategory.getCategory().isActive()).collect(Collectors.toList());
		return subCategoriesWithActiveCategory;
	}
	
	public List<SubCategory> getAllByCategory(Category category) {
		return subCategoryRepository.findByOwnerAndCategoryOrderByCategory(getOwner(), category);
	}
	
	@Override
	public SubCategory get(Long id) {
		return subCategoryRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(SubCategory subCategory) {
		SubCategory subCategoryExist = subCategoryRepository.findByOwnerAndNameAndCategory(getOwner(), subCategory.getName(), subCategory.getCategory());
		
		if (subCategoryExist != null && subCategoryExist.getCategory().getId() == subCategory.getCategory().getId()) {
			return;
		}
		
		subCategory.setOwner(getOwner());
		subCategory.setActive(true);
		super.create(subCategory);
	}
	
	@Override
	public void update(SubCategory category) {
		category.setOwner(getOwner());
		super.update(category);
	}

	
}
