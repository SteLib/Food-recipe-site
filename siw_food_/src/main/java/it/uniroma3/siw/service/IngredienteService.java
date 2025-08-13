package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.repository.IngredienteRepository;

@Service
public class IngredienteService {
	
	@Autowired
	private IngredienteRepository ingredienteRepository;

	public boolean existsByNome(String nome) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.existsByNome(nome);
	}

	public Ingrediente findByNome(String nome) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findByNome(nome);
	}

	public void save(Ingrediente ingrediente) {
		// TODO Auto-generated method stub
		this.ingredienteRepository.save(ingrediente);
	}

	public Ingrediente findById(Long id) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findById(id).get();
	}

	public Iterable<Ingrediente> findAll() {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findAll();
	}

	public List<Ingrediente> findIngredientiByNome(String nome) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findIngredientiByNome(nome);
	}

	public Iterable<Ingrediente> findIngredientiNotInRicetta(Long ricettaId) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findIngredientiNotInRicetta(ricettaId);
	}
	
	public void deleteById(Long id) {
		this.ingredienteRepository.deleteById(id);
	}
	
	public List<Ingrediente> findByCuoco(Cuoco currentCuoco) {
		// TODO Auto-generated method stub
		return this.ingredienteRepository.findByCuoco(currentCuoco);
	}

}
