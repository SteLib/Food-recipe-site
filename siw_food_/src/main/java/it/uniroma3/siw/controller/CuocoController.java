package it.uniroma3.siw.controller;


import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.CuocoValidator;
import jakarta.validation.Valid;

@Controller
public class CuocoController {

	@Autowired 
	private CuocoService cuocoService;
	@Autowired 
	private CuocoValidator cuocoValidator;
	@Autowired
	private RicettaService ricettaService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private CredentialsService credentialsService;

	/* Mapping amministratore */
	@GetMapping(value="/admin/formNewCuoco")
	public String formNewCuoco(Model model) {
		model.addAttribute("cuoco", new Cuoco());
		return "admin/formNewCuoco.html";
	}

	@GetMapping(value="/admin/indexCuoco")
	public String indexCuoco() {
		return "admin/indexCuoco.html";
	}

	@GetMapping(value="/admin/manageCuochi")
	public String manageCuochi(Model model) {
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "admin/manageCuochi.html";
	}

	@GetMapping(value="/admin/formUpdateCuoco/{id}")
	public String formUpdateCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.cuocoService.findById(id));
		return "admin/formUpdateCuoco.html";
	}

	@GetMapping(value="/admin/eliminaCuoco/{id}")
	public String eliminaCuoco(@PathVariable("id") Long id) {
		Credentials credentials = this.credentialsService.findByCuocoId(id);
		Cuoco cuoco = this.cuocoService.findById(id);
		String imageName = cuoco.getUrlImage();
		this.imageService.deleteImage(imageName);
		this.cuocoService.deleteById(id);
		if(credentials!=null)
			this.credentialsService.deleteById(credentials.getId());
		return "redirect:/admin/manageCuochi";
	}

	@PostMapping("/admin/cuoco")
	public String newCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco, 
			BindingResult bindingResult, @RequestParam("image") MultipartFile image, Model model) {
		this.cuocoValidator.validate(cuoco, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			cuoco.setUrlImage(imageName);
			this.cuocoService.save(cuoco);
			model.addAttribute("cuoco", cuoco);
			return "cuoco.html";
		}
		else
			return "admin/formNewCuoco.html";
	}

	@GetMapping("/admin/updateRicette/{id}")
	public String updateRicette(@PathVariable("id") Long id, Model model) {
		List<Ricetta> ricetteToAdd = this.ricetteToAdd(id);
		model.addAttribute("ricetteToAdd", ricetteToAdd);
		model.addAttribute("cuoco", this.cuocoService.findById(id));
		return "admin/ricetteToAdd.html";
	}

	private List<Ricetta> ricetteToAdd(Long cuocoId) {
		List<Ricetta> ricetteToAdd = new ArrayList<>();

		for(Ricetta r : this.ricettaService.findRicetteNotInCuoco(cuocoId)) {
			ricetteToAdd.add(r);
		}
		return ricetteToAdd;
	}

	@GetMapping(value="/admin/addRicettaToCuoco/{ricettaId}/{cuocoId}")
	public String addRicettaToCuoco(@PathVariable("ricettaId") Long ricettaId, 
			@PathVariable("cuocoId") Long cuocoId, Model model) {
		Cuoco cuoco = this.cuocoService.findById(cuocoId);
		Ricetta ricetta = this.ricettaService.findById(ricettaId);

		List<Ricetta> ricette = cuoco.getRicette();
		ricette.add(ricetta);
		ricetta.setCuoco(cuoco);
		this.ricettaService.save(ricetta);
		this.cuocoService.save(cuoco);

		List<Ricetta> ricetteToAdd = ricetteToAdd(cuocoId);

		model.addAttribute("cuoco", cuoco);
		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ricetteToAdd", ricetteToAdd);

		return "admin/ricetteToAdd.html";
	}

	@GetMapping(value="/admin/removeRicettaFromCuoco/{ricettaId}/{cuocoId}")
	public String removeRicettaFromCuoco(@PathVariable("ricettaId") Long ricettaId,
			@PathVariable("cuocoId") Long cuocoId, Model model) {
		Cuoco cuoco = this.cuocoService.findById(cuocoId);
		Ricetta ricetta = this.ricettaService.findById(ricettaId);
		List<Ricetta> ricette = cuoco.getRicette();
		ricette.remove(ricetta);
		ricetta.setCuoco(null);
		this.ricettaService.save(ricetta);
		this.cuocoService.save(cuoco);

		List<Ricetta> ricetteToAdd = ricetteToAdd(cuocoId);

		model.addAttribute("cuoco", cuoco);
		model.addAttribute("ricetta", ricetta);
		model.addAttribute("ricetteToAdd", ricetteToAdd);

		return "admin/ricetteToAdd.html";
	}

	/* Mapping utente occasionale */
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable("id") Long id, Model model) {
		model.addAttribute("cuoco", this.cuocoService.findById(id));
		return "cuoco.html"; 
	}

	@GetMapping("/cuoco")
	public String showCuochi(Model model) {
		List<Cuoco> cuochi = (List<Cuoco>) this.cuocoService.findAll();

		model.addAttribute("cuochi", cuochi);
		model.addAttribute("cuochiCount", cuochi.size());
		return "cuochi.html"; 
	}

	@GetMapping("/formSearchCuochi")
	public String formSearchCuochi() {
		return "formSearchCuochi.html";
	}

	@PostMapping("/searchCuochi")
	public String searchCuochi(Model model, @RequestParam String cognome) {
		model.addAttribute("cuochi", this.cuocoService.findCuochiByCognome(cognome));
		return "foundCuochi.html";
	}

}
