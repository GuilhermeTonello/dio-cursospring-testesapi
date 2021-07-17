package curso.dio.spring.estoquedecerveja.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import curso.dio.spring.estoquedecerveja.builder.CervejaDTOBuilder;
import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.entity.Cerveja;
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

}
