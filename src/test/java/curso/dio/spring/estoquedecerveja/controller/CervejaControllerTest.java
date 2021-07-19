package curso.dio.spring.estoquedecerveja.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import curso.dio.spring.estoquedecerveja.builder.CervejaDTOBuilder;
import curso.dio.spring.estoquedecerveja.dto.CervejaDTO;
import curso.dio.spring.estoquedecerveja.exception.CervejaNaoEncontradaException;
import curso.dio.spring.estoquedecerveja.service.CervejaService;
import curso.dio.spring.estoquedecerveja.util.JsonConvertionUtils;

@ExtendWith(MockitoExtension.class)
class CervejaControllerTest {
	
	private MockMvc mockMvc;
	
	@Mock
	private CervejaService cervejaService;
	
	@InjectMocks
	private CervejaController cervejaController;

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(cervejaController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()) //aceita objetos paginados
				.setViewResolvers((s, locale) -> new MappingJackson2JsonView()) //retorno em json
				.build();
	}
	
	@Test
	void whenPostIsCalledThenABeerIsCreated() throws Exception {
		// given
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
	
		// when
		when(cervejaService.save(cervejaDTO))
			.thenReturn(cervejaDTO);
		
		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/cerveja")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConvertionUtils.asJsonString(cervejaDTO)))
					.andExpect(MockMvcResultMatchers.status().isCreated())
					.andExpect(MockMvcResultMatchers.jsonPath("$.nome", is(cervejaDTO.getNome())));
	}
	
	@Test
	void whenPostIsCalledWithoutRequiredFieldThenErrorIsReturned() throws Exception {
		// given
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		cervejaDTO.setMarca(null);
		
		// then
		mockMvc
			.perform(MockMvcRequestBuilders.post("/api/v1/cerveja")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonConvertionUtils.asJsonString(cervejaDTO)))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	void quandoGetForInvocadoComNomeValidoInformadoEntaoRetornarStatusOk() throws Exception {
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		when(cervejaService.findByNome(cervejaDTO.getNome()))
			.thenReturn(cervejaDTO);
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/cerveja/" + cervejaDTO.getNome())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.nome", is(cervejaDTO.getNome())));
	}
	
	@Test
	void quandoGetForInvocadoComNomeNaoValidoInformadoEntaoRetornarStatusNotFound() throws Exception {
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		when(cervejaService.findByNome(cervejaDTO.getNome()))
			.thenThrow(new CervejaNaoEncontradaException(cervejaDTO.getNome()));
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/cerveja/" + cervejaDTO.getNome())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	void quandoGetForInvocadoParaRetornarUmaListaDeCervejasRetornarStatusOk() throws Exception {
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		when(cervejaService.findAll())
			.thenReturn(Collections.singletonList(cervejaDTO));
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/cerveja/")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$[0].nome", is(cervejaDTO.getNome())));
	}
	
	@Test
	void quandoGetForInvocadoParaRetornarUmaListaDeCervejasVaziaRetornarStatusOk() throws Exception {
		when(cervejaService.findAll())
			.thenReturn(Collections.emptyList());
		
		mockMvc
			.perform(MockMvcRequestBuilders.get("/api/v1/cerveja/")
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	void quandoDeleteForInvocadoComIdValidoRetornarStatusNoContent() throws Exception {
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		doNothing()
			.when(cervejaService)
			.deleteById(cervejaDTO.getId());
		
		mockMvc
			.perform(MockMvcRequestBuilders.delete("/api/v1/cerveja/" + cervejaDTO.getId())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void quandoDeleteForInvocadoComIdNaoValidoRetornarStatusNotFound() throws Exception {
		CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().toCervejaDTO();
		
		doThrow(CervejaNaoEncontradaException.class)
			.when(cervejaService)
			.deleteById(cervejaDTO.getId());
		
		mockMvc
			.perform(MockMvcRequestBuilders.delete("/api/v1/cerveja/" + cervejaDTO.getId())
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
}
