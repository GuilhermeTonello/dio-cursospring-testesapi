package curso.dio.spring.estoquedecerveja.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantidadeDTO {
	
    @NotNull
    @Max(100)
    private Integer quantidade;

}
