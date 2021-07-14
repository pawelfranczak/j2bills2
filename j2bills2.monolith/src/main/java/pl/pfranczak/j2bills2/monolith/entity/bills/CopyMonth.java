package pl.pfranczak.j2bills2.monolith.entity.bills;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CopyMonth {
	
	@Min(2020)
	Long sourceYear;
	@Min(1)
	Long sourceMonth;
	@Min(2020)
	Long targerYear;
	@Min(1)
	Long targetMonth;
	
}
