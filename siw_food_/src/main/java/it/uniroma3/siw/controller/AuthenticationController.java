package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.ImageService;
import it.uniroma3.siw.validator.CuocoValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private CuocoService cuocoService;

    @Autowired
	private CuocoValidator cuocoValidator;
    
    @Autowired
    private ImageService imageService;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("cuoco", new Cuoco());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE))
				return "admin/indexAdmin.html";
			else
				return "default/indexDefault.html";
		}
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) 
            return "admin/indexAdmin.html";
    	if(credentials.getRole().equals(Credentials.DEFAULT_ROLE))
    		return "default/indexDefault.html";
        return "index.html";
    }

	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("cuoco") Cuoco cuoco,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model, @RequestParam("image") MultipartFile image) {

		this.cuocoValidator.validate(cuoco, userBindingResult);
		this.imageService.validate(image, userBindingResult);
		// se cuoco e credential hanno entrambi contenuti validi, memorizza Cuoco e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
        	String imageName = this.imageService.storeImage(image);
			cuoco.setUrlImage(imageName);
            cuocoService.saveUser(cuoco);
            credentials.setCuoco(cuoco);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("cuoco", cuoco);
            return "registrationSuccessful";
        }
        return "formRegisterUser.html";
    }
}