package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.validator.IngredienteValidator;
import jakarta.validation.Valid;

@Controller
public class IngredienteController {
	
	@Autowired 
	private IngredienteService ingredienteService;
	@Autowired 
	private IngredienteValidator ingredienteValidator;
	@Autowired
	private CuocoService cuocoService;
	
	/* Mapping per la parte utente registrato */
	
	@GetMapping(value="/default/indexIngrediente")
	public String indexIngredienteCuoco() {
		return "default/indexIngrediente.html";
	}
	
	@GetMapping(value="/default/formNewIngrediente")
	public String formNewIngredienteCuoco(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "default/formNewIngrediente.html";
	}

	@PostMapping("/default/ingrediente")
	public String newIngredienteCuoco(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, 
			Principal principal, BindingResult bindingResult, Model model) {
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
			String currentUsername = principal.getName();
			Cuoco currentCuoco = this.cuocoService.findByUsername(currentUsername);
			ingrediente.setCuoco(currentCuoco);
			this.ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "ingrediente.html";
		}
		else
			return "default/formNewIngrediente.html";
	}
	
	@GetMapping(value="/default/manageIngredienti")
	public String manageIngredientiCuoco(Model model, Principal principal) {
		String currentUsername = principal.getName();
		Cuoco currentCuoco = cuocoService.findByUsername(currentUsername);
		List<Ingrediente> ingredienti = ingredienteService.findByCuoco(currentCuoco);
		model.addAttribute("ingredienti", ingredienti);
		return "default/manageIngredienti.html";
	}
	
	
	@GetMapping(value="/default/eliminaIngrediente/{id}")
	public String eliminaIngredienteCuoco(@PathVariable("id") Long id) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		// Per ogni ricetta associata all'ingrediente, rimuovi l'ingrediente
		for (Ricetta ricetta : ingrediente.getRicette()) {
		    ricetta.getIngredienti().remove(ingrediente);
		}
		this.ingredienteService.deleteById(id);
		return "redirect:/default/manageIngredienti";
	}
	
	/* Mapping per la parte amministratore */
	@GetMapping(value="/admin/formNewIngrediente")
	public String formNewIngrediente(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "admin/formNewIngrediente.html";
	}
	
	@GetMapping(value="/admin/indexIngrediente")
	public String indexIngrediente() {
		return "admin/indexIngrediente.html";
	}

	@PostMapping("/admin/ingrediente")
	public String newIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
		this.ingredienteValidator.validate(ingrediente, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "ingrediente.html";
		}
		else
			return "admin/formNewIngrediente.html";
	}
	
	@GetMapping(value="/admin/manageIngredienti")
	public String manageIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "admin/manageIngredienti.html";
	}
	
	
	@GetMapping(value="/admin/eliminaIngrediente/{id}")
	public String eliminaIngrediente(@PathVariable("id") Long id) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		// Per ogni ricetta associata all'ingrediente, rimuovi l'ingrediente
		for (Ricetta ricetta : ingrediente.getRicette()) {
		    ricetta.getIngredienti().remove(ingrediente);
		}
		this.ingredienteService.deleteById(id);
		return "redirect:/admin/manageIngredienti";
	}

	
	/*  Mapping per la parte accessibile a qualsiasi utente */
	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", this.ingredienteService.findById(id));
		return "ingrediente.html"; 
	}

	@GetMapping("/ingrediente")
	public String showIngredienti(Model model) {
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		return "ingredienti.html"; 
	}

	@GetMapping("/formSearchIngredienti")
	public String formSearchIngredienti() {
		return "formSearchIngredienti.html";
	}

	@PostMapping("/searchIngredienti")
	public String searchIngredienti(Model model, @RequestParam String nome) {
		model.addAttribute("ingredienti", this.ingredienteService.findIngredientiByNome(nome));
		return "foundIngredienti.html";
	}
	
	

}
