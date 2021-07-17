package curso.dio.spring.estoquedecerveja.builder;

import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.enums.TipoDeCerveja;
import lombok.Builder;

@Builder
public class CervejaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Brahma";

    @Builder.Default
    private String brand = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private TipoDeCerveja type = TipoDeCerveja.LAGER;

    public CervejaDTO toCervejaDTO() {
        return new CervejaDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }

}
