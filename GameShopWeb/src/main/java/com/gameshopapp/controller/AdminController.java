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

import com.gameshopapp.model.DatosBancarios;
import com.gameshopapp.model.Juegos;
import com.gameshopapp.model.JuegosFavoritos;
import com.gameshopapp.model.JuegosReservados;
import com.gameshopapp.model.User;
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
	
	// falta hacer los comentarios en los juegos y moderarlos el admin 
	
//	
//	@GetMapping("/tarifasAdmin")
//	public String mostrarTarifasAdmin(Model model) {	//el objeto model permite llevar info desde el controlador hasta la vista
//		model.addAttribute("tarifas", tarifaRepository.findAll());
//		return "tarifasAdmin";
//	}
//	

//	
//	@GetMapping("/añadirTarifa")
//	public String añadirTarifa() {		
//		return "añadirTarifa";
//	}
//	
//	@PostMapping("/saveNewTarifa")
//	public String saveNewTarifa(Tarifas newTarifa) {
//		tarifaRepository.save(newTarifa);
//		return "redirect:/Nebri-Fit/admin/tarifasAdmin";
//	}
//	
//	@GetMapping("/modificarTarifa/{id}")
//	public String modificarTarifa(@PathVariable Integer id, Model model) {		
//		model.addAttribute("tarifa", tarifaRepository.getOne(id));
//		return "editTarifa";
//	}
//	
//	@PostMapping("/saveModifiTarifa")
//	public String saveModifiTarifa(Tarifas modifiTarifa) {
//		tarifaRepository.save(modifiTarifa);
//		return "redirect:/Nebri-Fit/admin/tarifasAdmin";
//	}
//	
//	@GetMapping("/eliminarTarifa/{id}")
//	public String eliminarTarifa(@PathVariable Integer id) {		
//		List<BbddUsersTarifas> bbddUserTarifas = bbddUserTarifasRepository.findAll();
//		List<DatosBancarios> datosBancarios = datosBancariosRepository.findAll();
//		List<BbddUsersClases> bbddUserClases = bbddUserClasesRepository.findAll();
//		
//		for(BbddUsersTarifas t: bbddUserTarifas) {
//			FichaUser usuario = userRepository.getOne(t.getIdUser());
//			if(t.getIdTarifa().equals(id)) {
//				//borramos la tarifa
//				bbddUserTarifasRepository.deleteById(t.getId());
//				
//				//borramos los datos bancarios del usuario que tenia esa tarifa
//				for(DatosBancarios d: datosBancarios) {
//					if(d.getIdUser().equals(t.getIdUser())) {
//						datosBancariosRepository.delete(d);
//					}
//				}
//				usuario.setIdDatosBancarios(null);
//				userRepository.save(usuario);
//				
//				//borramos las clases del usuario que tenia esa tarifa
//				for(BbddUsersClases c: bbddUserClases) {
//					if(c.getIdUser().equals(t.getIdUser()))
//						bbddUserClasesRepository.deleteById(c.getId());
//				}
//			}
//				
//				
//		}
//		
//		tarifaRepository.deleteById(id);
//		
//		return "redirect:/Nebri-Fit/admin/tarifasAdmin";
//	}
//	
//	@GetMapping("/clasesAdmin")
//	public String mostrarClasesAdmin(Model model) {	//el objeto model permite llevar info desde el controlador hasta la vista
//		model.addAttribute("clasesGym", clasesGymRepository.findAll());
//		return "clasesGymAdmin";
//	}
//	
//	@GetMapping("/verClases/{id}")
//	public String verClases(@PathVariable Integer id, Model model) {
//		List<ClasesGym> clases = clasesGymRepository.findAll();
//		List<BbddUsersClases> bbddUserClases = bbddUserClasesRepository.findAll();
//		
//		ArrayList<ClasesGym> misClases = new ArrayList<ClasesGym>();
//	
//		for(BbddUsersClases c: bbddUserClases) {
//			if(c.getIdClase().equals(id)) {
//				for(ClasesGym cg: clases) {
//					if(cg.getId().equals(c.getIdClase())) {
//						misClases.add(cg);
//					}
//				}
//			}
//		}
//
//		model.addAttribute("misClasesGym", misClases);
//		return "misClasesAdmin";
//		
//	}
//	
//	@GetMapping("/añadirClase")
//	public String añadirClase() {		
//		return "añadirClase";
//	}
//	
//	@PostMapping("/saveNewClase")
//	public String saveNewClase(ClasesGym newClase) {
//		clasesGymRepository.save(newClase);
//		return "redirect:/Nebri-Fit/admin/clasesAdmin";
//	}
//	
//	@GetMapping("/modificarClase/{id}")
//	public String modificarClase(@PathVariable Integer id, Model model) {		
//		model.addAttribute("clase", clasesGymRepository.getOne(id));
//		return "editClase";
//	}
//	
//	@PostMapping("/saveModifiClase")
//	public String saveModifiClase(ClasesGym modifiClase) {
//		clasesGymRepository.save(modifiClase);
//		return "redirect:/Nebri-Fit/admin/clasesAdmin";
//	}
//	
//	@GetMapping("/eliminarClase/{id}")
//	public String eliminarClase(@PathVariable Integer id) {		
//		
//		List<BbddUsersClases> bbddUserClases = bbddUserClasesRepository.findAll();
//		
//		for(BbddUsersClases c: bbddUserClases) {
//			if(c.getIdClase().equals(id))
//				bbddUserClasesRepository.deleteById(c.getId());
//		}
//		
//		clasesGymRepository.deleteById(id);
//		
//		return "redirect:/Nebri-Fit/admin/clasesAdmin";
//	}
	
}

