package pl.pfranczak.j2bills2.monolith.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.repository.UserRepository;
import pl.pfranczak.j2bills2.monolith.repository.category.CategoryRepository;
import pl.pfranczak.j2bills2.monolith.service.CrudServiceImpl;

@Service
public class CategoryService extends CrudServiceImpl<Category, Long>{
	
	CategoryRepository categoryRepository;
	UserRepository userRepository;

	public CategoryService(CategoryRepository repository, UserRepository userRepository) {
		super.setRepository(repository);
		this.categoryRepository = repository;
	}
	
	@Override
	public List<Category> getAll() {
		return categoryRepository.findByOwner(getOwner());
	}
	
	@Override
	public Category get(Long id) {
		return categoryRepository.findByIdAndOwner(id, getOwner());
	}	
	
	@Override
	public void create(Category category) {
		category.setOwner(getOwner());
		category.setActive(true);
		super.create(category);
	}
	
	@Override
	public void update(Category categor) {
		categor.setOwner(getOwner());
		super.update(categor);
	}

	
}
