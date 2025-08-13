package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.RicettaRepository;

@Service
public class RicettaService {

	@Autowired
	private RicettaRepository ricettaRepository;

	public Ricetta findById(Long id) {
		return ricettaRepository.findById(id).get();
	}
	
	public Iterable<Ricetta> findAll() { 
		return ricettaRepository.findAll();
	}

	public Ricetta save(Ricetta ricetta) {
		return ricettaRepository.save(ricetta);
	}

	public List<Ricetta> findRicetteByNome(String nome) {
		return ricettaRepository.findRicetteByNome(nome);
	}
	
	

	public boolean existsByNome(String nome) {
		return ricettaRepository.existsByNome(nome);
	}

	public Ricetta findByNome(String nome) {
		return ricettaRepository.findByNome(nome);
	}

	public void deleteById(Long id) {
		ricettaRepository.deleteById(id);
	}

	public Iterable<Ricetta> findRicetteNotInCuoco(Long cuocoId) {
		// TODO Auto-generated method stub
		return this.ricettaRepository.findRicetteNotInCuoco(cuocoId);
	}

	public List<Ricetta> findByCuoco(Cuoco currentCuoco) {
		// TODO Auto-generated method stub
		return this.ricettaRepository.findByCuoco(currentCuoco);
	}

}
