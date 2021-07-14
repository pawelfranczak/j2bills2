package pl.pfranczak.j2bills2.monolith.entity.bills;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CopyMonth {
	
	Long sourceYear;
	Long sourceMonth; 
	Long targerYear;
	Long targetMonth;
	
}
