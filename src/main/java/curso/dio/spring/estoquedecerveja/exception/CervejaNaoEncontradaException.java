package curso.dio.spring.estoquedecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CervejaNaoEncontradaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CervejaNaoEncontradaException(String nome) {
		super("Cerveja " + nome + " não encontrada!");
	}
	
	public CervejaNaoEncontradaException(Long id) {
		super("Cerveja " + id + " não encontrada!");
	}

}
