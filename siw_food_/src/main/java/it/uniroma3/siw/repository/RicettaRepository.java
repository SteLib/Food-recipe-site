package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;

public interface RicettaRepository extends CrudRepository<Ricetta, Long> {

	public List<Ricetta> findRicetteByNome(String nome);
	
	public Ricetta findByNome(String nome);

	public boolean existsByNome(String nome);
	
	
	@Query(value="select * "
			+ "from ricetta "
			+ "where cuoco_id <> :cuocoId "
			+ "or cuoco_id is NULL", nativeQuery=true)
	public Iterable<Ricetta> findRicetteNotInCuoco(@Param("cuocoId") Long cId);

	public List<Ricetta> findByCuoco(Cuoco currentCuoco);

}
