package pl.pfranczak.j2bills2.monolith.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(targetEntity = UserAccount.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "owner_id")
	private UserAccount owner;
	
	@NotNull
	@NotBlank
	@Column(unique=true)
	private String name;
	
	@NotNull
	@NotBlank
	private String description;
	
	private BigDecimal balance;
	
	private boolean active;
	
	@Column(nullable=false, columnDefinition = "bool DEFAULT false")
	private boolean takeIntoTotalSum;

}
