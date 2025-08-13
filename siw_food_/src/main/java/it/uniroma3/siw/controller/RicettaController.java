package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.RicettaValidator;
import jakarta.validation.Valid;

@Controller
public class RicettaController {

	@Autowired 
	private RicettaService ricettaService;
	@Autowired 
	private RicettaValidator ricettaValidator;
	@Autowired 
	private CuocoService cuocoService;
	@Autowired 
	private IngredienteService ingredienteService;
	@Autowired
	private ImageService imageService;


	/* Mapping per utente registrato*/

	@GetMapping(value="/default/indexRicetta")
	public String indexRicettaCuoco() {
		return "default/indexRicetta.html";
	}

	@GetMapping(value="/default/formNewRicetta")
	public String formNewRicettaCuoco(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		return "default/formNewRicetta.html";
	}

	@PostMapping(value="/default/ricetta")
	public String newRicettaCuoco(@Valid @ModelAttribute("ricetta") Ricetta ricetta,
			Principal principal, @RequestParam("image") MultipartFile image,
			BindingResult bindingResult, Model model) {
		this.ricettaValidator.validate(ricetta, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			String currentUsername = principal.getName();
			Cuoco currentCuoco = this.cuocoService.findByUsername(currentUsername);
			ricetta.setCuoco(currentCuoco);
			ricetta.setUrlImage(imageName);
			this.ricettaService.save(ricetta);
			model.addAttribute("ricetta", ricetta);
			return "ricetta.html";
		}
		else {
			return "default/formNewRicetta.html";
		}
	}

	@GetMapping(value="/default/manageRicette")
	public String manageRicetteDiUnCuoco(Model model, Principal principal) {
		String currentUsername = principal.getName();
		Cuoco currentCuoco = cuocoService.findByUsername(currentUsername);
		List<Ricetta> ricette = ricettaService.findByCuoco(currentCuoco);
		model.addAttribute("ricette", ricette);
		return "default/manageRicette.html";
	}

	@GetMapping(value="/default/eliminaRicetta/{id}")
	public String rimuoviRicettaDiUnCuoco(@PathVariable Long id, Principal principal) {
		String currentUsername = principal.getName();
		Cuoco currentCuoco = cuocoService.findByUsername(currentUsername);
		Ricetta ricetta = ricettaService.findById(id);
		if (ricetta != null && ricetta.getCuoco().equals(currentCuoco)) {
			String imageName = ricetta.getUrlImage();
		    this.imageService.deleteImage(imageName);
			this.ricettaService.deleteById(id);
		}
		return "redirect:/default/manageRicette";
	}

	@GetMapping("/default/updateIngredienti/{id}")
	public String updateIngredientiCuoco(@PathVariable("id") Long id, Model model) {

		List<Ingrediente> ingredientiToAdd = this.ingredientiToAdd(id);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);
		model.addAttribute("ricetta", this.ricettaService.findById(id));

		return "default/ingredientiToAdd.html";
	}

	@GetMapping(value="/default/addIngredienteToRicetta/{ingredienteId}/{ricettaId}")
	public String addIngredienteToRicettaCuoco(@PathVariable("ingredienteId") Long ingredienteId, @PathVariable("ricettaId") Long ricettaId, Model model) {
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		Set<Ingrediente> ingredienti = ricetta.getIngredienti();
		ingredienti.add(ingrediente);
		this.ricettaService.save(ricetta);

		List<Ingrediente> ingredientiToAdd = ingredientiToAdd(ricettaId);

		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);

		return "default/ingredientiToAdd.html";
	}

	@GetMapping(value="/default/removeIngredienteFromRicetta/{ingredienteId}/{ricettaId}")
	public String removeIngredienteFromRicettaCuoco(@PathVariable("ingredienteId") Long ingredienteId, @PathVariable("ricettaId") Long ricettaId, Model model) {
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		Set<Ingrediente> ingredienti = ricetta.getIngredienti();
		ingredienti.remove(ingrediente);
		this.ricettaService.save(ricetta);

		List<Ingrediente> ingredientiToAdd = ingredientiToAdd(ricettaId);

		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);

		return "default/ingredientiToAdd.html";
	}


	/* Mapping utente occasionale */

	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "ricetta.html"; 
	}

	@GetMapping("/ricetta")
	public String showRicette(Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "ricette.html"; 
	}

	@GetMapping("/formSearchRicette")
	public String formSearchRicette() {
		return "formSearchRicette.html";
	}

	@PostMapping("/searchRicette")
	public String searchRicette(Model model, @RequestParam String nome) {
		model.addAttribute("ricette", this.ricettaService.findRicetteByNome(nome));
		return "foundRicette.html";
	}

	/* Mapping amministratore*/

	@GetMapping(value="/admin/indexRicetta")
	public String indexRicetta() {
		return "admin/indexRicetta.html";
	}


	@GetMapping(value="/admin/formNewRicetta")
	public String formNewRicetta(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		return "admin/formNewRicetta.html";
	}

	@PostMapping("/admin/ricetta")
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta, 
			@RequestParam("image") MultipartFile image, BindingResult bindingResult, Model model) {
		this.ricettaValidator.validate(ricetta, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			ricetta.setUrlImage(imageName);
			this.ricettaService.save(ricetta);
			model.addAttribute("ricetta", ricetta);
			return "ricetta.html";
		}
		else {
			return "admin/formNewRicetta.html";
		}
	}

	@GetMapping(value="/admin/manageRicette")
	public String manageRicette(Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "admin/manageRicette.html";
	}

	@GetMapping(value="/admin/eliminaRicetta/{id}")
	public String eliminaRicetta(@PathVariable("id") Long id) {
		Ricetta ricetta = this.ricettaService.findById(id);
		String imageName = ricetta.getUrlImage();
		this.imageService.deleteImage(imageName);
		this.ricettaService.deleteById(id);
		return "redirect:/admin/manageRicette";
	}

	@GetMapping(value="/admin/formUpdateRicetta/{id}")
	public String formUpdateRicetta(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "admin/formUpdateRicetta.html";
	}

	@GetMapping(value="/admin/addCuoco/{id}")
	public String addCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuochi", this.cuocoService.findAll());
		model.addAttribute("ricetta", this.ricettaService.findById(id));
		return "admin/cuochiToAdd.html";
	}

	@GetMapping("/admin/setCuocoToRicetta/{cuocoId}/{ricettaId}")
	public String setCuocoToRicetta(@PathVariable("cuocoId") Long cuocoId, @PathVariable("ricettaId") Long ricettaId, Model model) {

		Cuoco cuoco = this.cuocoService.findById(cuocoId);
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		ricetta.setCuoco(cuoco);
		this.ricettaService.save(ricetta);

		model.addAttribute("ricetta", ricetta);
		return "admin/formUpdateRicetta.html";
	}

	@GetMapping("/admin/updateIngredienti/{id}")
	public String updateIngredienti(@PathVariable("id") Long id, Model model) {

		List<Ingrediente> ingredientiToAdd = this.ingredientiToAdd(id);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);
		model.addAttribute("ricetta", this.ricettaService.findById(id));

		return "admin/ingredientiToAdd.html";
	}

	@GetMapping(value="/admin/addIngredienteToRicetta/{ingredienteId}/{ricettaId}")
	public String addIngredienteToRicetta(@PathVariable("ingredienteId") Long ingredienteId, @PathVariable("ricettaId") Long ricettaId, Model model) {
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		Set<Ingrediente> ingredienti = ricetta.getIngredienti();
		ingredienti.add(ingrediente);
		this.ricettaService.save(ricetta);

		List<Ingrediente> ingredientiToAdd = ingredientiToAdd(ricettaId);

		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);

		return "admin/ingredientiToAdd.html";
	}

	@GetMapping(value="/admin/removeIngredienteFromRicetta/{ingredienteId}/{ricettaId}")
	public String removeIngredienteFromRicetta(@PathVariable("ingredienteId") Long ingredienteId, @PathVariable("ricettaId") Long ricettaId, Model model) {
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		Ingrediente ingrediente = this.ingredienteService.findById(ingredienteId);
		Set<Ingrediente> ingredienti = ricetta.getIngredienti();
		ingredienti.remove(ingrediente);
		this.ricettaService.save(ricetta);

		List<Ingrediente> ingredientiToAdd = ingredientiToAdd(ricettaId);

		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ingredientiToAdd", ingredientiToAdd);

		return "admin/ingredientiToAdd.html";
	}

	private List<Ingrediente> ingredientiToAdd(Long ricettaId) {
		List<Ingrediente> ingredientiToAdd = new ArrayList<>();

		for (Ingrediente a : ingredienteService.findIngredientiNotInRicetta(ricettaId)) {
			ingredientiToAdd.add(a);
		}
		return ingredientiToAdd;
	}

}
