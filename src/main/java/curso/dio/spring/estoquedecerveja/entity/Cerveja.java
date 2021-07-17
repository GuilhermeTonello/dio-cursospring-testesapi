package curso.dio.spring.estoquedecerveja.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import curso.dio.spring.estoquedecerveja.enums.TipoDeCerveja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cerveja {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	@Column(nullable = false)
	private String marca;
	
	@Column(nullable = false)
	private int max;

	@Column(nullable = false)
	private int quantidade;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoDeCerveja tipo;
	
}
