package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.service.CuocoService;

@Component
public class CuocoValidator implements Validator{
	
	@Autowired
	private CuocoService cuocoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Cuoco.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Cuoco cuoco = (Cuoco)target;
		if(cuoco.getNome()!=null && cuoco.getCognome()!=null 
				&& cuocoService.existsByNomeAndCognome(cuoco.getNome(), cuoco.getCognome()))
		errors.reject("cuoco.duplicate");
	}
	

}
