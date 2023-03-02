package pl.pfranczak.j2bills2.monolith.controller.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import pl.pfranczak.j2bills2.monolith.entity.Journal;
import pl.pfranczak.j2bills2.monolith.entity.category.Category;
import pl.pfranczak.j2bills2.monolith.entity.category.SubCategory;
import pl.pfranczak.j2bills2.monolith.entity.category.SumByCategoryAndSubCategory;
import pl.pfranczak.j2bills2.monolith.service.JournalService;
import pl.pfranczak.j2bills2.monolith.service.notification.NotificationService;

@AllArgsConstructor
@Controller
@RequestMapping("${category}")
public class SumsController {
	
	private NotificationService notificationService;
	private JournalService journalService;

	@GetMapping("${sum_by_category_and_sub_category}/{year}/{month}")	
	public ModelAndView showAll(@PathVariable("year") Long year, @PathVariable("month") Long month) {
		ModelAndView modelAndView = new ModelAndView("category/sum_by_category_and_sub_category");
		List<Journal> journalsEntries = journalService.getAllForYearMonth(year, month);
		
		LinkedHashMap<Category, Map<SubCategory, BigDecimal>> map = new LinkedHashMap<>();
		LinkedHashMap<Category, BigDecimal> mapWithoutSubCategory = new LinkedHashMap<>();
		
		List<SumByCategoryAndSubCategory> sumByCategoryAndSubCategories = new
				ArrayList<SumByCategoryAndSubCategory>();
		BigDecimal sumWithoutCategory = BigDecimal.ZERO;
		
		for (Journal entry : journalsEntries) {
			Category entryCategory = entry.getCategory();
			SubCategory entrySubCategory = entry.getSubCategory();
			BigDecimal entryValue = entry.getValue();
			if (entryCategory == null) {
				sumWithoutCategory = sumWithoutCategory.add(entryValue);
			} else {
				
				if (entrySubCategory != null) {
					if (map.containsKey(entryCategory)) {
						if (map.get(entryCategory).containsKey(entrySubCategory)) {
							BigDecimal subCategoryValue = map.get(entryCategory).get(entrySubCategory);
							subCategoryValue = subCategoryValue.add(entryValue);
							map.get(entryCategory).put(entrySubCategory, subCategoryValue);
						} else {
							map.get(entryCategory).put(entrySubCategory, entryValue);
						}
					} else {
						LinkedHashMap<SubCategory, BigDecimal> subMap = new LinkedHashMap<>();
						subMap.put(entrySubCategory, entryValue);
						map.put(entryCategory, subMap);
					}
					
				} else {
					if (mapWithoutSubCategory.containsKey(entryCategory)) {
						BigDecimal subCategoryValue = mapWithoutSubCategory.get(entryCategory);
						subCategoryValue = subCategoryValue.add(entryValue);
						mapWithoutSubCategory.put(entryCategory, subCategoryValue);
					} else {
						mapWithoutSubCategory.put(entryCategory, entryValue);
					}
				}
			}
		}
		
		for (Entry<Category, Map<SubCategory, BigDecimal>> mapEntrySet : map.entrySet()) {
			for (Entry<SubCategory, BigDecimal> subMapEntrySet : mapEntrySet.getValue().entrySet()) {
				SumByCategoryAndSubCategory sumByCategoryAndSubCategory = new SumByCategoryAndSubCategory();
				sumByCategoryAndSubCategory.setCategory(mapEntrySet.getKey());
				sumByCategoryAndSubCategory.setSubCategory(subMapEntrySet.getKey());
				sumByCategoryAndSubCategory.setValue(subMapEntrySet.getValue());
				sumByCategoryAndSubCategories.add(sumByCategoryAndSubCategory);
			}
		}
		
		for (Entry<Category, BigDecimal> mapEntrySet : mapWithoutSubCategory.entrySet()) {
			SumByCategoryAndSubCategory sumByCategoryAndSubCategory = new SumByCategoryAndSubCategory();
			sumByCategoryAndSubCategory.setCategory(mapEntrySet.getKey());
			sumByCategoryAndSubCategory.setValue(mapEntrySet.getValue());
			sumByCategoryAndSubCategories.add(sumByCategoryAndSubCategory);
		}
		
		SumByCategoryAndSubCategory sumByCategoryAndSubCategory = new SumByCategoryAndSubCategory();
		sumByCategoryAndSubCategory.setValue(sumWithoutCategory);
		sumByCategoryAndSubCategories.add(sumByCategoryAndSubCategory);
		
		modelAndView.addObject("sumByCategoryAndSubCategories", sumByCategoryAndSubCategories);
		long countOfActiveNotification = notificationService.getCountOfActiveNotification();
		modelAndView.addObject("countOfActiveNotification", countOfActiveNotification+"");
		return modelAndView;
	}

}
