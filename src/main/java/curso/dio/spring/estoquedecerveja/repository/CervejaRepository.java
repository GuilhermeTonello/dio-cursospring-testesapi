package curso.dio.spring.estoquedecerveja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import curso.dio.spring.estoquedecerveja.entity.Cerveja;

public interface CervejaRepository extends JpaRepository<Cerveja, Long> {

	Optional<Cerveja> findByNome(String nome);
	
}
