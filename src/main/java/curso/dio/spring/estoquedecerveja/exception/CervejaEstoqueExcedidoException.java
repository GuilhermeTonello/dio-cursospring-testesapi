package curso.dio.spring.estoquedecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CervejaEstoqueExcedidoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CervejaEstoqueExcedidoException(Long id, int quantidadeParaIncrementar) {
		super("O valor informado de " + quantidadeParaIncrementar + " excede o limite da cerveja " + id);
	}
	
}
