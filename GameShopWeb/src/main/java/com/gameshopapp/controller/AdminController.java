package com.gameshopapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gameshopapp.model.Comentarios;
import com.gameshopapp.model.DatosBancarios;
import com.gameshopapp.model.Juegos;
import com.gameshopapp.model.JuegosFavoritos;
import com.gameshopapp.model.JuegosReservados;
import com.gameshopapp.model.User;
import com.gameshopapp.repository.IComentariosRepository;
import com.gameshopapp.repository.IDatosBancariosRepository;
import com.gameshopapp.repository.IJuegosFavoritosRepository;
import com.gameshopapp.repository.IJuegosRepository;
import com.gameshopapp.repository.IJuegosReservadosRepository;
import com.gameshopapp.repository.IUserRepository;

//controller de la clase producto

@Controller
@RequestMapping("/GameShop/admin") //path donde vamos a apuntar después de la url para poder acceder al recurso (http:localhot:8080/Nebri-Fit)
public class AdminController {

	//instanciamos la interface, a trabes de este objeto vamos a poder usar todos los metodos de JpaRepository
	
	@Autowired
	private IUserRepository userRepository; //el new lo hace spring framework automaticamente
	
	@Autowired
	private IDatosBancariosRepository datosBancariosRepository;

	@Autowired
	private IJuegosRepository juegosRepository;
	
	@Autowired
	private IJuegosFavoritosRepository juegosFavoritosRepository;
	
	@Autowired
	private IJuegosReservadosRepository juegosReservadosRepository;
	
	@Autowired
	private IComentariosRepository comentariosRepository;
	
	private Comentarios comentarios = new Comentarios();
	
	//una forma de ir mostrando lo que hace el logg, en vez de usar un System.out.println()
	private final org.slf4j.Logger logg = LoggerFactory.getLogger(User.class);
	
	@GetMapping("") 
	public String home(Model model) {	
		List<Juegos> juegosBbdd = juegosRepository.findAll();
		model.addAttribute("juego", juegosBbdd);
		return "homeAdmin"; 
	} 
	
	@GetMapping("/bbddUsers")
	public String bbdd(Model model) {	//el objeto model permite llevar info desde el controlador hasta la vista
		model.addAttribute("user", userRepository.findAll()); //a través del repository traemos todos los users de la bbdd y lo guardamos en la variable fichaUser	
		return "bbdd";
	}
	
	@GetMapping("/juego/{id}")
	public String verJuego(@PathVariable Integer id, Model model) {
		model.addAttribute("juego", juegosRepository.getOne(id));
		
		ArrayList<Comentarios> ComentariosJuego = new ArrayList<Comentarios>();
		
		List<Comentarios> comentarios = comentariosRepository.findAll();
		
		for(Comentarios coments: comentarios) {
			if(coments.getIdJuego().equals(id)) {
				ComentariosJuego.add(coments);
			}
		}
		
		model.addAttribute("comentarios", ComentariosJuego);
		
		return "detalleJuegoAdmin";
	}
	
	@GetMapping("/editarJuego/{id}")
	public String editarJuego(@PathVariable Integer id, Model model) {
		model.addAttribute("juego", juegosRepository.getOne(id));
		
		
		return "editJuego";
	}
	
	@PostMapping("/saveModifiJuego")
	public String saveModifiClase(Juegos modifiJuego) {
		juegosRepository.save(modifiJuego);
		return "redirect:/GameShop/admin";
	}
	
	@GetMapping("/borrarJuego/{id}")
	public String borrarJuego(@PathVariable Integer id) {
		List<JuegosFavoritos> juegosFavRepository = juegosFavoritosRepository.findAll();
		List<JuegosReservados> juegosResRepository = juegosReservadosRepository.findAll();
		List<Comentarios> TodosComentarios = comentariosRepository.findAll();
		
		for(JuegosFavoritos jf: juegosFavRepository) {
			if(jf.getIdJuego().equals(id)) {
				juegosFavoritosRepository.deleteById(jf.getId());
			}
		}
		
		for(JuegosReservados jr: juegosResRepository) {
			if(jr.getIdJuego().equals(id)) {
				juegosReservadosRepository.deleteById(jr.getId());
			}
		}
		
		for(Comentarios c: TodosComentarios) {
			if(c.getIdJuego().equals(id)) {
				comentariosRepository.deleteById(c.getId());
			}
		}
		
		juegosRepository.deleteById(id);
		
		return "redirect:/GameShop/admin";
	}
	
	@GetMapping("/delete/{id}") 
	public String delete(@PathVariable Integer id) {
		User user = userRepository.getOne(id); //para coger el producto de la bbdd
		userRepository.delete(user); 

		List<JuegosFavoritos> juegosFavRepository = juegosFavoritosRepository.findAll();
		List<JuegosReservados> juegosResRepository = juegosReservadosRepository.findAll();
		List<DatosBancarios> datosBancarios = datosBancariosRepository.findAll();
		
		for(JuegosFavoritos jf: juegosFavRepository) {
			if(jf.getIdUser().equals(id)) {
				juegosFavoritosRepository.deleteById(jf.getId());
			}
		}
		
		for(JuegosReservados jr: juegosResRepository) {
			if(jr.getIdUser().equals(id)) {
				juegosReservadosRepository.deleteById(jr.getId());
			}
		}
		
		for(DatosBancarios d: datosBancarios) {
			if(d.getIdUser().equals(user.getId())) {
				datosBancariosRepository.delete(d);
			}
		}
	
		return "redirect:/GameShop/admin/bbddUsers";
	}
	
	@GetMapping("/datosBancarios/{id}")
	public String mostrarDatosBancarios(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		User usuario = userRepository.getOne(id);
		
		if(usuario.getIdDatosBancarios() != null) {
			model.addAttribute("user", usuario);
			model.addAttribute("datosBancarios", datosBancariosRepository.getOne(usuario.getIdDatosBancarios()));
			return "verDatosBancariosAdmin";
		}else {
			redirectAttrs
	        .addFlashAttribute("mensaje", "El usuario no tiene datos bancarios")
	        .addFlashAttribute("clase", "success");
			
			return "redirect:/GameShop/admin/bbddUsers";
		}
	}
	
	@GetMapping("/verJuegosFavUser/{id}")
	public String verJuegosFavUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		List<Juegos> juegos = juegosRepository.findAll();
		List<JuegosFavoritos> juegosFavUser = juegosFavoritosRepository.findAll();
		
		ArrayList<Juegos> misJuegosFav = new ArrayList<Juegos>();
	
		for(JuegosFavoritos jf: juegosFavUser) {
			if(jf.getIdUser().equals(id)) {
				for(Juegos j: juegos) {
					if(j.getId().equals(jf.getIdJuego())) {
						misJuegosFav.add(j);
					}
				}
			}
		}

		if(misJuegosFav.size() == 0) {
			redirectAttrs
	        .addFlashAttribute("mensaje", "El usuario no tiene juegos Favoritos")
	        .addFlashAttribute("clase", "success");
			
			return "redirect:/GameShop/admin/bbddUsers";
		}else {
			model.addAttribute("juegosFav", misJuegosFav);
			model.addAttribute("user", userRepository.getOne(id));
			return "verJuegosFavAdmin";
		}	
		
	}
	
	@GetMapping("/verJuegosResUser/{id}")
	public String verJuegosReservadosUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		List<Juegos> juegos = juegosRepository.findAll();
		List<JuegosReservados> juegosResUser = juegosReservadosRepository.findAll();
		
		ArrayList<Juegos> misJuegosRes = new ArrayList<Juegos>();
	
		for(JuegosReservados jf: juegosResUser) {
			if(jf.getIdUser().equals(id)) {
				for(Juegos j: juegos) {
					if(j.getId().equals(jf.getIdJuego())) {
						misJuegosRes.add(j);
					}
				}
			}
		}

		if(misJuegosRes.size() == 0) {
			redirectAttrs
	        .addFlashAttribute("mensaje", "El usuario no tiene juegos Reservados")
	        .addFlashAttribute("clase", "success");
			
			return "redirect:/GameShop/admin/bbddUsers";
		}else {
			model.addAttribute("juegosRes", misJuegosRes);
			model.addAttribute("user", userRepository.getOne(id));
			return "verJuegosReservadosAdmin";
		}	
		
	}
	
	@GetMapping("/borrarReservaUser/{idUser}/{idJuego}")
	public String borrarJuegoReservadoUser(@PathVariable Integer idUser, @PathVariable Integer idJuego, Model model, RedirectAttributes redirectAttrs) {
		List<JuegosReservados> juegosResUser = juegosReservadosRepository.findAll();
		
		for(JuegosReservados jr : juegosResUser) {
			if(jr.getIdJuego().equals(idJuego) && jr.getIdUser().equals(idUser)) {
				juegosReservadosRepository.deleteById(jr.getId());
				
				redirectAttrs
		        .addFlashAttribute("mensaje", "Eliminado correctamente")
		        .addFlashAttribute("clase", "success");
				
				return "redirect:/GameShop/admin/bbddUsers";
			}
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Error")
        .addFlashAttribute("clase", "success");
		
		return "redirect:/GameShop/admin/bbddUsers";	
	}
	
	@GetMapping("/borrarComentario/{id}")
	public String borrarJuegoReservadoUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		List<Comentarios> TodosComentarios = comentariosRepository.findAll();
		
		for(Comentarios c: TodosComentarios) {
			if(c.getId().equals(id)) {
				comentariosRepository.deleteById(id);
			}
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Comentario borrado correctamente")
        .addFlashAttribute("clase", "success");
		
		return "redirect:/GameShop/admin";
	} 
	
}

