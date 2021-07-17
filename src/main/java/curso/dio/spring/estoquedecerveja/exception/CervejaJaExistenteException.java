package curso.dio.spring.estoquedecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CervejaJaExistenteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CervejaJaExistenteException(String cervejaNome) {
		super("Cerveja " + cervejaNome + " já existe!");
	}
	
}
