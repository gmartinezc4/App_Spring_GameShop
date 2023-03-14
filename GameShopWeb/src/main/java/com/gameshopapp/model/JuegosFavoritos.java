package com.gameshopapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="juegosFavoritosUser")
public class JuegosFavoritos {
	@Id //indicamos que es un id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer idJuego;
	private Integer idUser;
	
	public JuegosFavoritos(Integer id, Integer idJuego, Integer idUser) {
		super();
		this.id = id;
		this.idJuego = idJuego;
		this.idUser = idUser;
	}
	
	public JuegosFavoritos(Integer idJuego, Integer idUser) {
		super();
		this.idJuego = idJuego;
		this.idUser = idUser;
	}
	
	public JuegosFavoritos() {
		super();
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdJuego() {
		return idJuego;
	}
	
	public void setIdJuego(Integer idJuego) {
		this.idJuego = idJuego;
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	
	
	@Override
	public String toString() {
		return "JuegosFavoritos [id=" + id + ", idJuego=" + idJuego + ", idUser=" + idUser + "]";
	}

}
