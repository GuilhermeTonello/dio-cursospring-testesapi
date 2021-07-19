package curso.dio.spring.estoquedecerveja.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import curso.dio.spring.estoquedecerveja.builder.CervejaDTOBuilder;
import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.entity.Cerveja;
import curso.dio.spring.estoquedecerveja.exception.CervejaEstoqueExcedidoException;
import curso.dio.spring.estoquedecerveja.exception.CervejaJaExistenteException;
import curso.dio.spring.estoquedecerveja.exception.CervejaNaoEncontradaException;
import curso.dio.spring.estoquedecerveja.mapper.CervejaMapper;
import curso.dio.spring.estoquedecerveja.repository.CervejaRepository;

@ExtendWith(MockitoExtension.class)
class CervejaServiceTest {

	@Mock
	private CervejaRepository cervejaRepository;

	@InjectMocks
	private CervejaService cervejaService;

	@Test
	void quandoCervejaForInformadaEntaoDeveSerCriada() throws CervejaJaExistenteException {
		// given
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaSalvaEsperada = CervejaMapper.getInstance().toModel(cervejaDTO);

		// when
		when(cervejaRepository.findByNome(cervejaDTO.getNome()))
			.thenReturn(Optional.empty());
		when(cervejaRepository.save(cervejaSalvaEsperada))
			.thenReturn(cervejaSalvaEsperada);

		// then
		CervejaDTO cervejaDTOSalva = cervejaService.save(cervejaDTO);

		// assertEquals(cervejaDTO.getId(), cervejaDTOSalva.getId());
		assertThat(cervejaDTOSalva.getId(), is(equalTo(cervejaDTO.getId()))); // assertThat(atual, esperado);
		assertThat(cervejaDTOSalva.getNome(), is(equalTo(cervejaDTO.getNome())));
		assertThat(cervejaDTOSalva.getQuantidade(), is(equalTo(cervejaDTO.getQuantidade())));

		// assertEquals(esperado, atual);
		// assertEquals(cervejaDTO.getNome(), cervejaDTOSalva.getNome());

		// assertThat(cervejaDTOSalva.getQuantidade(), is(greaterThan(2)));
	}

	@Test
	void quandoCervejaJaFoiCriadaUmaExceptionDeveSerLancada() throws CervejaJaExistenteException {
		CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaDuplicada = CervejaMapper.getInstance().toModel(cervejaDTOEsperada);
		
		when(cervejaRepository.findByNome(cervejaDTOEsperada.getNome()))
			.thenReturn(Optional.of(cervejaDuplicada));
		
		assertThrows(CervejaJaExistenteException.class, () -> cervejaService.save(cervejaDTOEsperada));
	}
	
	@Test
	void quandoNomeDaCervejaInformadaForValidoRetornarCerveja() throws CervejaNaoEncontradaException {
		// given
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		// when
		when(cervejaRepository.findByNome(cervejaEsperada.getNome()))
			.thenReturn(Optional.of(cervejaEsperada));
		
		// then
		CervejaDTO cervejaEncontradaDTO = cervejaService.findByNome(cervejaEsperadaDTO.getNome());
		assertThat(cervejaEncontradaDTO, is(equalTo(cervejaEsperadaDTO)));
	}
	
	@Test
	void quandoNomeDaCervejaInformadaNaoForValidoRetornarErro() {
		// given
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		// when
		when(cervejaRepository.findByNome(cervejaEsperadaDTO.getNome()))
			.thenReturn(Optional.empty());
		
		// then
		assertThrows(CervejaNaoEncontradaException.class, () -> cervejaService.findByNome(cervejaEsperadaDTO.getNome()));
	}
	
	@Test
	void quandoListaDeCervejasForChamadaRetornarUmaListaComTodasAsCervejas() {
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		when(cervejaRepository.findAll())
			.thenReturn(Collections.singletonList(cervejaEsperada));
		
		List<CervejaDTO> listaCervejasDTO = cervejaService.findAll();
		
		assertThat(listaCervejasDTO, is(not(empty())));
		assertThat(listaCervejasDTO.get(0), is(equalTo(cervejaEsperadaDTO)));
	}
	
	@Test
	void quandoListaDeCervejasForChamadaRetornarUmaListaVazia() {
		when(cervejaRepository.findAll())
			.thenReturn(Collections.emptyList());
		
		List<CervejaDTO> listaCervejasDTO = cervejaService.findAll();
		
		assertThat(listaCervejasDTO, is(empty()));
	}
	
	@Test
	void quandoUmIdValidoForInformadoUmaCervejaDeveSerDeletada() throws CervejaNaoEncontradaException {
		// given
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		// when
		when(cervejaRepository.findById(cervejaEsperada.getId()))
			.thenReturn(Optional.of(cervejaEsperada));
		
		doNothing()
			.when(cervejaRepository).deleteById(cervejaEsperada.getId());
		
		// then
		cervejaService.deleteById(cervejaEsperadaDTO.getId());
		
		verify(cervejaRepository, times(1))
			.findById(cervejaEsperada.getId());
		
		verify(cervejaRepository, times(1))
			.deleteById(cervejaEsperada.getId());
	}
	
	@Test
	void quandoIncrementarForChamadoIncrementarCerveja() throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		when(cervejaRepository.findById(cervejaEsperada.getId()))
			.thenReturn(Optional.of(cervejaEsperada));
		
		when(cervejaRepository.save(cervejaEsperada))
			.thenReturn(cervejaEsperada);
		
		int quantidadeParaIncrementar = 10;
		int quantidadeEsperadaAposIncremento = cervejaEsperadaDTO.getQuantidade() + quantidadeParaIncrementar;
		CervejaDTO cervejaIncrementadaDTO = cervejaService.increment(cervejaEsperadaDTO.getId(), quantidadeParaIncrementar);
		
		assertThat(quantidadeEsperadaAposIncremento, equalTo(cervejaIncrementadaDTO.getQuantidade()));
		assertThat(quantidadeEsperadaAposIncremento, lessThan(cervejaEsperadaDTO.getMax()));
	}
	
	@Test
	void quandoIncrementarForChamadoComQuantidadeMaiorQueOMaximoRetornarErro() throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		when(cervejaRepository.findById(cervejaEsperada.getId()))
			.thenReturn(Optional.of(cervejaEsperada));
		
		int quantidadeParaIncrementar = 500;
		
		assertThrows(CervejaEstoqueExcedidoException.class, () -> cervejaService.increment(cervejaEsperadaDTO.getId(), quantidadeParaIncrementar));
	}
	
	@Test
	void quandoIncrementarForChamadoComIdNaoValidoRetornarErro() throws CervejaNaoEncontradaException, CervejaEstoqueExcedidoException {
		CervejaDTO cervejaEsperadaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		Cerveja cervejaEsperada = CervejaMapper.getInstance().toModel(cervejaEsperadaDTO);
		
		when(cervejaRepository.findById(cervejaEsperada.getId()))
			.thenReturn(Optional.empty());
		
		int quantidadeParaIncrementar = 500;
		
		assertThrows(CervejaNaoEncontradaException.class, () -> cervejaService.increment(cervejaEsperadaDTO.getId(), quantidadeParaIncrementar));
	}

}
