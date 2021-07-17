package curso.dio.spring.estoquedecerveja.mapper;

import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.entity.Cerveja;

public class CervejaMapper {
	
	private static CervejaMapper instance;
	
    public Cerveja toModel(CervejaDTO cervejaDTO) {
    	Cerveja cerveja = new Cerveja();
    	
    	cerveja.setId(cervejaDTO.getId());
    	cerveja.setMarca(cervejaDTO.getMarca());
    	cerveja.setMax(cervejaDTO.getMax());
    	cerveja.setNome(cervejaDTO.getNome());
    	cerveja.setQuantidade(cervejaDTO.getQuantidade());
    	cerveja.setTipo(cervejaDTO.getTipo());
    	
    	return cerveja;
    }

    public CervejaDTO toDTO(Cerveja cerveja) {
    	CervejaDTO cervejaDTO = new CervejaDTO();
    	
    	cervejaDTO.setId(cerveja.getId());
    	cervejaDTO.setMarca(cerveja.getMarca());
    	cervejaDTO.setMax(cerveja.getMax());
    	cervejaDTO.setNome(cerveja.getNome());
    	cervejaDTO.setQuantidade(cerveja.getQuantidade());
    	cervejaDTO.setTipo(cerveja.getTipo());
    	
    	return cervejaDTO;
    }
    
    public static CervejaMapper getInstance() {
        if (instance == null) {
            instance = new CervejaMapper();
        }
        return instance;
    }
	
}
