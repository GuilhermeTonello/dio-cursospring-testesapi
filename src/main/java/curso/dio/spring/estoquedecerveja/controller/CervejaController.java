package curso.dio.spring.estoquedecerveja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.dto.QuantidadeDTO;
import curso.dio.spring.estoquedecerveja.exception.CervejaEstoqueExcedidoException;
import curso.dio.spring.estoquedecerveja.exception.CervejaJaExistenteException;
import curso.dio.spring.estoquedecerveja.exception.CervejaNaoEncontradaException;
import curso.dio.spring.estoquedecerveja.service.CervejaService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/cerveja")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {
	
	private final CervejaService cervejaService;
	
	@GetMapping("{nome}")
	@ResponseStatus(code = HttpStatus.OK)
	public CervejaDTO findByNome(@PathVariable("nome") String nome) throws CervejaNaoEncontradaException {
		return cervejaService.findByNome(nome);
	}
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<CervejaDTO> findAll() {
		return cervejaService.findAll();
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO createBeer(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervejaJaExistenteException {
        return cervejaService.save(cervejaDTO);
    }
    

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws CervejaNaoEncontradaException {
    	cervejaService.deleteById(id);
    }

    @PatchMapping("{id}/increment")
    public CervejaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) 
    		throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
        return cervejaService.increment(id, quantidadeDTO.getQuantidade());
    }

}
