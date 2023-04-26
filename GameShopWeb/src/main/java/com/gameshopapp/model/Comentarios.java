package com.gameshopapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Comentarios")
public class Comentarios {

	@Id //indicamos que es un id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer idUser;
	private String comentario;
	private Integer idJuego;
	
	
	public Comentarios() {
		super();
	}
	
	public Comentarios(Integer idUser, String comentario, Integer idJuego) {
		super();
		this.idUser = idUser;
		this.comentario = comentario;
		this.idJuego = idJuego;
	}
	
	public Comentarios(Integer id, Integer idUser, String comentario, Integer idJuego) {
		super();
		this.id = id;
		this.idUser = idUser;
		this.comentario = comentario;
		this.idJuego = idJuego;
	}
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIdUser() {
		return idUser;
	}
	
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer getIdJuego() {
		return idJuego;
	}

	public void setIdJuego(Integer idJuego) {
		this.idJuego = idJuego;
	}

	@Override
	public String toString() {
		return "Comentarios [id=" + id + ", idUser=" + idUser + ", comentario=" + comentario + ", idJuego=" + idJuego
				+ "]";
	}

	
	
	
}
