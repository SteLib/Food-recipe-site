package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Long> {

	public boolean existsByNome(String nome);

	public Ingrediente findByNome(String nome);

	public List<Ingrediente> findIngredientiByNome(String nome);

	@Query(value="select * "
			+ "from ingrediente a "
			+ "where a.id not in "
			+ "(select ingredienti_id "
			+ "from ricetta_ingredienti "
			+ "where ricetta_ingredienti.ricette_id = :ricettaId)", nativeQuery=true)
	public Iterable<Ingrediente> findIngredientiNotInRicetta(@Param("ricettaId") Long id);
	
	public List<Ingrediente> findByCuoco(Cuoco currentCuoco);

}
