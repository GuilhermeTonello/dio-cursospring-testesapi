package curso.dio.spring.estoquedecerveja.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import curso.dio.spring.estoquedecerveja.enums.TipoDeCerveja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CervejaDTO {
	
	private Long id;
	
	@NotBlank
	@Size(min = 1, max = 50)
	private String nome;
	
	@NotBlank
	@Size(min = 1, max = 70)
	private String marca;
	
	@NotNull
	@Max(value = 100)
	private Integer max;

	@NotNull
	@Max(value = 100)
	private Integer quantidade;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoDeCerveja tipo;

}
