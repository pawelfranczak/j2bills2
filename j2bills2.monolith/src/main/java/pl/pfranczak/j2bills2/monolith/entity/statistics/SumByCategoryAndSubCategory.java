package pl.pfranczak.j2bills2.monolith.entity.statistics;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SumByCategoryAndSubCategory {

	private Category category;
	private SubCategory subCategory;
	private BigDecimal value;
	
}
