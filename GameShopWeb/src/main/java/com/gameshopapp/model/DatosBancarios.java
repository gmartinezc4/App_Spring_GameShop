package com.gameshopapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DatosBancarios")
public class DatosBancarios {

	@Id //indicamos que es un id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer idUser;
	private String numeroTarjeta;
	private String nombreTarjeta;
	private String fechaCaducidad;
	private Integer cvv;
	
	public DatosBancarios(Integer idUser, String numeroTarjeta, String nombreTarjeta, String fechaCaducidad, Integer cvv) {
		super();
		this.idUser = idUser;
		this.numeroTarjeta = numeroTarjeta;
		this.nombreTarjeta = nombreTarjeta;
		this.fechaCaducidad = fechaCaducidad;
		this.cvv = cvv;
	}
	
	public DatosBancarios() {
		
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

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getNombreTarjeta() {
		return nombreTarjeta;
	}

	public void setNombreTarjeta(String nombreTarjeta) {
		this.nombreTarjeta = nombreTarjeta;
	}

	public String getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return "DatosBancarios [id=" + id + ", idUser=" + idUser + ", numeroTarjeta=" + numeroTarjeta
				+ ", nombreTarjeta=" + nombreTarjeta + ", fechaCaducidad=" + fechaCaducidad + ", cvv=" + cvv + "]";
	}
	
	
	
	
}
