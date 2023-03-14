package com.gameshopapp.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gameshopapp.repository.IJuegosFavoritosRepository;
import com.gameshopapp.repository.IJuegosRepository;
import com.gameshopapp.repository.IUserRepository;
import com.gameshopapp.model.Juegos;
import com.gameshopapp.model.User;




@Controller
@RequestMapping("/GameShop") //path donde vamos a apuntar después de la url para poder acceder al recurso (http:localhot:8080/GameShop)
public class AppController {

	private final org.slf4j.Logger logg = LoggerFactory.getLogger(User.class);
	
	@Autowired
	private IJuegosRepository juegosRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IJuegosFavoritosRepository juegosFavoritosRepository;
	
	private User usuario;
	
	@GetMapping("")
	public String home(Model model) {
		List<Juegos> juegosBbdd = juegosRepository.findAll();
		model.addAttribute("juego", juegosBbdd);
		
		return "home"; 
	} 
	
	@GetMapping("/register")
	public String mostrarFormularioRegister() {
		return "registro";
	}
	
	@ModelAttribute("usuario")
	public User newUser() {
		return new User();
	}
	
	@PostMapping("/register/comprobate")
	public String registro(@ModelAttribute("usuario") User user, RedirectAttributes redirectAttrs) {
		List<User> usuariosBbdd = userRepository.findAll();
		boolean coincide = false;
		
		for(User u: usuariosBbdd) {
			if(u.getEmail().toString().equalsIgnoreCase(user.getEmail().toString())) {
				coincide = true;
			}
		}
		
		if(coincide == false) {
			if(user.getPassword().toString().equals(user.getPasswordRepeat())) {
				userRepository.save(user);
				logg.info("user {}", user.getNombre());
				usuario = user;
				logg.info("user {}", usuario.getNombre());
				return "redirect:/GameShop";
			}else {
				redirectAttrs
	            .addFlashAttribute("mensaje", "Las contraseñas no coinciden")
	            .addFlashAttribute("clase", "success");
			return "redirect:/GameShop/register";
			}	
		}else {		 
			 redirectAttrs
	            .addFlashAttribute("mensaje", "Ya existe un usuario con ese email")
	            .addFlashAttribute("clase", "success");
			return "redirect:/GameShop/register";
		}	
	}
	
	@GetMapping("/juego/{id}")
	public String verJuego(@PathVariable Integer id, Model model) {	
		model.addAttribute("juego", juegosRepository.getOne(id));
		
		return "detalleJuego";
	}
}
