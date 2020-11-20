package pl.pfranczak.j2bills2.monolith.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movement {
	
	@NotNull
	private User user;
	
	@NotNull
	private Account sourceAccount;

	@NotNull
	private Account targetAccount;
	
	@NotNull
	@NotBlank
	private String description;
	
	@NotNull
	private BigDecimal value;
	
	
}
