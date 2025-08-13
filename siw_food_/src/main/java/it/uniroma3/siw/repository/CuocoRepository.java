package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cuoco;

public interface CuocoRepository extends CrudRepository<Cuoco, Long> {

	public List<Cuoco> findCuochiByCognome(String cognome);

	public Cuoco findByCognome(String cognome);

	public boolean existsByNomeAndCognome(String nome, String cognome);

	public Cuoco findByNomeAndCognome(String nome, String cognome);

	@Query("SELECT cred.cuoco FROM Credentials cred WHERE cred.username = :username")
    public Cuoco findByUsername(@Param("username") String username);

}
