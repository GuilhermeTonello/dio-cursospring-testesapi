package curso.dio.spring.estoquedecerveja.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.entity.Cerveja;
import curso.dio.spring.estoquedecerveja.exception.CervejaEstoqueExcedidoException;
import curso.dio.spring.estoquedecerveja.exception.CervejaJaExistenteException;
import curso.dio.spring.estoquedecerveja.exception.CervejaNaoEncontradaException;
import curso.dio.spring.estoquedecerveja.mapper.CervejaMapper;
import curso.dio.spring.estoquedecerveja.repository.CervejaRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaService {
	
	private final CervejaRepository cervejaRepository;
	
	public CervejaDTO save(CervejaDTO cervejaDTO) throws CervejaJaExistenteException {
		verifyIfIsAlreadyRegistered(cervejaDTO.getNome());
		Cerveja cerveja = CervejaMapper.getInstance().toModel(cervejaDTO);
		Cerveja cervejaSalva = cervejaRepository.save(cerveja);
		return CervejaMapper.getInstance().toDTO(cervejaSalva);
	}
	
	public CervejaDTO findByNome(String nome) throws CervejaNaoEncontradaException {
		Cerveja cerveja = cervejaRepository.findByNome(nome)
				.orElseThrow(() -> new CervejaNaoEncontradaException(nome));
		return CervejaMapper.getInstance().toDTO(cerveja);
	}
	
	public List<CervejaDTO> findAll() {
		return cervejaRepository.findAll()
				.stream()
				.map(CervejaMapper.getInstance()::toDTO)
				.collect(Collectors.toList());
	}
	
	public void deleteById(Long id) throws CervejaNaoEncontradaException {
		verifyIfExists(id);
		cervejaRepository.deleteById(id);
	}
	

    private void verifyIfIsAlreadyRegistered(String name) throws CervejaJaExistenteException {
        Optional<Cerveja> optSavedBeer = cervejaRepository.findByNome(name);
        if (optSavedBeer.isPresent()) {
            throw new CervejaJaExistenteException(name);
        }
    }

    private Cerveja verifyIfExists(Long id) throws CervejaNaoEncontradaException {
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new CervejaNaoEncontradaException(id));
    }

    public CervejaDTO increment(Long id, int quantityToIncrement) throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
    	Cerveja beerToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantidade();
        if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
            beerToIncrementStock.setQuantidade(beerToIncrementStock.getQuantidade() + quantityToIncrement);
            Cerveja incrementedBeerStock = cervejaRepository.save(beerToIncrementStock);
            return CervejaMapper.getInstance().toDTO(incrementedBeerStock);
        }
        throw new CervejaEstoqueExcedidoException(id, quantityToIncrement);
    }

}
