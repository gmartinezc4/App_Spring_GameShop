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

import com.gameshopapp.model.Juegos;
import com.gameshopapp.model.User;
import com.gameshopapp.repository.IJuegosRepository;
import com.gameshopapp.repository.IUserRepository;

@Controller
@RequestMapping("/GameShop/user")
public class IniciadaSesionController {

private final org.slf4j.Logger logg = LoggerFactory.getLogger(User.class);
	
	@Autowired
	private IJuegosRepository juegosRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	private User usuario;
	
	@GetMapping("/homeUser")
	public String mostrarHomeUser(Model model) {
		List<Juegos> juegosBbdd = juegosRepository.findAll();
		model.addAttribute("juego", juegosBbdd);
		
		return "homeUserLoged";
	}
	
	@GetMapping("/login")
	public String mostrarFormularioLogin() {
		return "login";
	}
	
	@ModelAttribute("usuario")
	public User newUser() {
		return new User();
	}
	
	@PostMapping("/login/comprobate")
	public String iniciarSession(@ModelAttribute("usuario") User user) {
		List<User> usuariosBbdd = userRepository.findAll();
		
		for(User u: usuariosBbdd) {
			if(u.getEmail().toString().equalsIgnoreCase(user.getEmail().toString()) && 
					u.getPassword().toString().equalsIgnoreCase(user.getPassword().toString())) {
				usuario = u;
				return "redirect:/GameShop/user/homeUser";
			}
		}
		
		return "redirect:/GameShop/login";
	}
	
	@GetMapping("/juego/{id}")
	public String verJuego(@PathVariable Integer id, Model model) {
		model.addAttribute("juego", juegosRepository.getOne(id));
		model.addAttribute("user", usuario);
		
		return "detalleJuego";
	}
	
	@GetMapping("/perfil")
	public String verPrefil(Model model) {
		model.addAttribute("user", usuario);
		
		return "perfil";
	}
	
	@PostMapping("/perfil/save")
	public String savePrefil(User user) {
		userRepository.save(user);	
		usuario = user;
		
		return "redirect:/GameShop/user/perfil";
	}
	
}
