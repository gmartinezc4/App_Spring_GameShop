package com.gameshopapp.controller;

import java.util.ArrayList;
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


@Controller
@RequestMapping("/GameShop/user")
public class IniciadaSesionController {
	
	@Autowired
	private IJuegosRepository juegosRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IJuegosFavoritosRepository juegosFavoritosRepository;
	
	@Autowired
	private IJuegosReservadosRepository juegosReservadosRepository;
	
	@Autowired
	private IDatosBancariosRepository datosBancariosRepository;
	
	@Autowired
	private IComentariosRepository comentariosRepository;
	
	private User usuario = new User();
	private JuegosFavoritos juegoFav = new JuegosFavoritos();
	private JuegosReservados juegoReservado = new JuegosReservados();
	private DatosBancarios datosBancarios;
	private Comentarios comentarios = new Comentarios();
	
	boolean yaEsFav = false;
	boolean yaEstaReservado = false;

	
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
	public String iniciarSession(@ModelAttribute("usuario") User user, RedirectAttributes redirectAttrs) {
		if(user.getEmail().toString().equalsIgnoreCase("admin@admin.com") && 
				user.getPassword().toString().equalsIgnoreCase("Adminadmin123")) {
			return "redirect:/GameShop/admin";
		}
		
		List<User> usuariosBbdd = userRepository.findAll();
		
		for(User u: usuariosBbdd) {
			if(u.getEmail().toString().equalsIgnoreCase(user.getEmail().toString()) && 
					u.getPassword().toString().equalsIgnoreCase(user.getPassword().toString())) {
				usuario = u;
				return "redirect:/GameShop/user/homeUser";
			}
		}
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Email o contraseña incorrectos")
        .addFlashAttribute("clase", "success");
		
		return "redirect:/GameShop/user/login";
	}
	
	@GetMapping("/juegoElegido")
	public String verJuegoElegido(Model model) {
		
		return "detalleJuegoUser";
	}
	
	@GetMapping("/juego/{id}")
	public String verJuego(@PathVariable Integer id, Model model) {
		model.addAttribute("juego", juegosRepository.getOne(id));
		model.addAttribute("user", usuario);
		model.addAttribute("estaReservado", yaEstaReservado);	
		
		ArrayList<Comentarios> ComentariosJuego = new ArrayList<Comentarios>();
		
		List<Comentarios> comentarios = comentariosRepository.findAll();
		
		for(Comentarios coments: comentarios) {
			if(coments.getIdJuego().equals(id)) {
				ComentariosJuego.add(coments);
			}
		}
		
		model.addAttribute("comentarios", ComentariosJuego);
		
		return "detalleJuegoUser";
	}
	
	
	@GetMapping("/añadirFavorito/{id}")
	public String añadirAFavoritos(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		model.addAttribute("juego", juegosRepository.getOne(id));
		model.addAttribute("user", usuario);
		model.addAttribute("estaReservado", yaEstaReservado);
		
		yaEsFav = false;
		int idJuegoFav = 0;
		juegoFav = new JuegosFavoritos();
		
		List<JuegosFavoritos> juegosFavUser =  juegosFavoritosRepository.findAll();
		
		for(JuegosFavoritos j: juegosFavUser) {
			if(j.getIdJuego().equals(id) && j.getIdUser().equals(usuario.getId())) {
				yaEsFav = true;
				idJuegoFav = j.getId();
			}
		}
		
		if(yaEsFav) {
			juegoFav.setIdJuego(id);
			juegoFav.setIdUser(usuario.getId());
			
			juegosFavoritosRepository.deleteById(idJuegoFav);
			
			yaEsFav = false;
			
			redirectAttrs
	        .addFlashAttribute("mensaje", "Juego quitado de favoritos")
	        .addFlashAttribute("clase", "success");
		}else {
			juegoFav.setIdJuego(id);
			juegoFav.setIdUser(usuario.getId());

			juegosFavoritosRepository.save(juegoFav);

			yaEsFav = true;
			
			redirectAttrs
	        .addFlashAttribute("mensaje", "Juego añadido a favoritos")
	        .addFlashAttribute("clase", "success");
		}
		
		return "redirect:/GameShop/user/juegoFav";
	}
	
	@GetMapping("/juegoFav")
	public String verDetalleJuegoConFav(Model model) {
		model.addAttribute("juego", juegosRepository.getOne(juegoFav.getIdJuego()));
		model.addAttribute("user", usuario);
		
		return "detalleJuegoUser";
	}
	
	@GetMapping("/añadirReserva/{id}")
	public String añadirAReservados(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
		
		if(usuario.getIdDatosBancarios() != null) {	
			yaEstaReservado = false;
			int idJuegoReservado = id;
			juegoReservado = new JuegosReservados();
			
			List<JuegosReservados> juegosReservadosUser =  juegosReservadosRepository.findAll();
			
			for(JuegosReservados j: juegosReservadosUser) {
				if(j.getIdJuego().equals(id) && j.getIdUser().equals(usuario.getId())) {
					yaEstaReservado = true;
					idJuegoReservado = j.getId();
				}
			}
			
			if(yaEstaReservado) {
				juegoReservado.setIdJuego(id);
				juegoReservado.setIdUser(usuario.getId());
				
				juegosReservadosRepository.deleteById(idJuegoReservado);
				
				yaEstaReservado = false;
				
				redirectAttrs
		        .addFlashAttribute("mensaje", "Juego quitado de reservas")
		        .addFlashAttribute("clase", "success");
			}else {
				juegoReservado.setIdJuego(id);
				juegoReservado.setIdUser(usuario.getId());

				juegosReservadosRepository.save(juegoReservado);

				yaEstaReservado = true;
				idJuegoReservado = id;
				
				redirectAttrs
		        .addFlashAttribute("mensaje", "Juego reservado")
		        .addFlashAttribute("clase", "success");
			}
			
			return "redirect:/GameShop/user/juegoReservado";
		}else {
			model.addAttribute("usuario", usuario);
			model.addAttribute("juegoId", id);
			return "datosBancariosJuego";
		}
	}
	
	@GetMapping("/juegoReservado")
	public String verDetalleJuegoaReservados(Model model) {
		model.addAttribute("juego", juegosRepository.getOne(juegoReservado.getIdJuego()));
		model.addAttribute("user", usuario);
		
		return "detalleJuegoUser";
	}
	
	@PostMapping("/saveDatosBancariosJuego/{id}")
	public String guardarMetodoPagoJuego(@PathVariable Integer id, DatosBancarios datos, Model model, RedirectAttributes redirectAttrs) {
		datosBancariosRepository.save(datos);
		
		List<DatosBancarios> datosB = datosBancariosRepository.findAll();
		
		for(DatosBancarios d: datosB) {
			if(d.getIdUser() == usuario.getId()) {
				usuario.setIdDatosBancarios(d.getId());
				userRepository.save(usuario);
				
				datosBancarios = d;
			}
		}
	
		yaEstaReservado = false;
		int idJuegoReservado = id;
		juegoReservado = new JuegosReservados();
		
		List<JuegosReservados> juegosReservadosUser =  juegosReservadosRepository.findAll();
		
		for(JuegosReservados j: juegosReservadosUser) {
			if(j.getIdJuego().equals(id) && j.getIdUser().equals(usuario.getId())) {
				yaEstaReservado = true;
				idJuegoReservado = j.getId();
			}
		}
		
		if(yaEstaReservado) {
			juegoReservado.setIdJuego(id);
			juegoReservado.setIdUser(usuario.getId());
			
			juegosReservadosRepository.deleteById(idJuegoReservado);
			
			yaEstaReservado = false;
			
			redirectAttrs
	        .addFlashAttribute("mensaje", "Juego quitado de reservas")
	        .addFlashAttribute("clase", "success");
		}else {
			juegoReservado.setIdJuego(id);
			juegoReservado.setIdUser(usuario.getId());

			juegosReservadosRepository.save(juegoReservado);

			yaEstaReservado = true;
			idJuegoReservado = id;
			
			redirectAttrs
	        .addFlashAttribute("mensaje", "Juego reservado")
	        .addFlashAttribute("clase", "success");
		}
		
		return "redirect:/GameShop/user/juegoReservado";
	}
	
	@PostMapping("/saveDatosBancarios")
	public String guardarMetodoPago(DatosBancarios datos, Model model) {
		datosBancariosRepository.save(datos);
		datosBancarios = datos;
		
		usuario.setIdDatosBancarios(datos.getId());
		userRepository.save(usuario);
		
		model.addAttribute("user", usuario);
		model.addAttribute("datosBancarios", datos);
		
		return "perfil";
	}
	
	@GetMapping("/anadirMetodoPago")
	public String anadirMetodoPago(Model model) {
		model.addAttribute("usuario", usuario);

		return "datosBancarios";
	}
	
	@GetMapping("/borrarMetodoPago")
	public String borrarMetodoPago(Model model) {
		datosBancariosRepository.deleteById(usuario.getIdDatosBancarios());
		datosBancarios = new DatosBancarios();
		
		usuario.setIdDatosBancarios(null);
		userRepository.save(usuario);

		List<JuegosReservados> juegosReservados = juegosReservadosRepository.findAll();
		
		for(JuegosReservados j: juegosReservados) {
			if(usuario.getId() == j.getIdUser()) {
				juegosReservadosRepository.delete(j);
			}
		}
		
		return "redirect:/GameShop/user/perfil";
	}
	
	@GetMapping("/perfil")
	public String verPrefil(Model model) {
		model.addAttribute("user", usuario);
		
		if(usuario.getIdDatosBancarios() != null) {
			DatosBancarios datosBancariosUser = datosBancariosRepository.getOne(usuario.getIdDatosBancarios());
			model.addAttribute("datosBancarios", datosBancariosUser);
		}
		
		return "perfil";
	}
	
	@PostMapping("/perfil/save")
	public String savePrefil(User user) {
		userRepository.save(user);	
		usuario = user;
		
		return "redirect:/GameShop/user/perfil";
	}
	
	@GetMapping("/JuegosReservados")
	public String verJuegosReservados(Model model) {	
		List<Juegos> juegos = juegosRepository.findAll();
		List<JuegosReservados> juegosReservadosUser = juegosReservadosRepository.findAll();

		ArrayList<Juegos> misJuegos = new ArrayList<Juegos>();
		
		for(JuegosReservados juegosReser: juegosReservadosUser) {
			if(juegosReser.getIdUser().equals(usuario.getId())) {
				for(Juegos jueg: juegos) {
					if(jueg.getId().equals(juegosReser.getIdJuego())) {
						misJuegos.add(jueg);
					}
				}
			}
		}
		
		model.addAttribute("juegosReservados", misJuegos);
		
		return "juegosReservados";
	}
	
	@GetMapping("/borrarJuegoReservado/{id}")
	public String borrarJuegoReservado(@PathVariable Integer id, Model model) {
		List<JuegosReservados> juegosReservados = juegosReservadosRepository.findAll();
		
		for(JuegosReservados j: juegosReservados) {
			if(j.getIdJuego().equals(id)) {
				juegosReservadosRepository.deleteById(j.getId());
			}
		}

		return "redirect:/GameShop/user/JuegosReservados";
	}
	
	
	@GetMapping("/JuegosFavoritos")
	public String verJuegosFavoritos(Model model) {
		List<Juegos> juegos = juegosRepository.findAll();
		List<JuegosFavoritos> juegosFavoritosUser = juegosFavoritosRepository.findAll();

		ArrayList<Juegos> misJuegos = new ArrayList<Juegos>();
		
		for(JuegosFavoritos juegosFav: juegosFavoritosUser) {
			if(juegosFav.getIdUser().equals(usuario.getId())) {
				for(Juegos jueg: juegos) {
					if(jueg.getId().equals(juegosFav.getIdJuego())) {
						misJuegos.add(jueg);
					}
				}
			}
		}
		
		model.addAttribute("juegosFavs", misJuegos);
		
		return "juegosFavoritos";
	}
	
	@GetMapping("/borrarJuegoFavorito/{id}")
	public String borrarJuegoFavorito(@PathVariable Integer id, Model model) {
		List<JuegosFavoritos> juegosFavoritos = juegosFavoritosRepository.findAll();
		
		for(JuegosFavoritos j: juegosFavoritos) {
			if(j.getIdJuego().equals(id)) {
				juegosFavoritosRepository.deleteById(j.getId());
			}
		}

		return "redirect:/GameShop/user/JuegosFavoritos";
	}
	
	@PostMapping("/darseDeBaja")
	public String bajaUser(User user) {
		userRepository.deleteById(usuario.getId());
		usuario = null;
		
		return "redirect:/GameShop";
	}
	
	@GetMapping("/cerrarSesion")
	public String cerrarSesion(User user) {
		usuario = null;
		
		return "redirect:/GameShop";
	}
	
	@PostMapping("/guardarComentario")
	public String guardarComentarioUser(Comentarios comentario, RedirectAttributes redirectAttrs) {
		comentarios.setComentario(comentario.getComentario());
		comentarios.setIdUser(usuario.getId());
		comentarios.setIdJuego(comentario.getIdJuego());
		
		comentariosRepository.save(comentarios);
		
		comentarios = new Comentarios();

		redirectAttrs
        .addFlashAttribute("mensaje", "Comentario añadido")
        .addFlashAttribute("clase", "success");
		
		return "redirect:/GameShop/user/homeUser";
	}
	
}
