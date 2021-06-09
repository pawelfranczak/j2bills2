package pl.pfranczak.j2bills2.monolith.entity.bills;

import java.math.BigDecimal;
import java.time.Month;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pfranczak.j2bills2.monolith.authentication.UserAccount;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillsOfMonth {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "owner_id")
	private UserAccount owner;
	
	@NotNull
	@NotBlank	
	private String name;
	
	private String description;
	
	@NotNull
	private Month month;
	
	@NotNull
	private Long year;
	
	@NotNull
	private BigDecimal amount;
	
	@Min(1)
	@Max(31)
	private Byte dueDay;
	
	private Boolean paid;
	
}
