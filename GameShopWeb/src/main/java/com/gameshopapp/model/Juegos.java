package com.gameshopapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="juegos")
public class Juegos {
	@Id //indicamos que es un id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //usamos una estrategia para auto-incrementar cada vez que hagamos un nuevo producto
	private Integer id;
	private String img;
	private String nombre;
	private String empresa;
	private String descripcion;
	private Integer stock;
	private Double precio;
	
	
	public Juegos(String img, Integer id, String nombre, String empresa, String descripcion, Integer stock, Double precio) {
		super();
		this.img = img;
		this.id = id;
		this.nombre = nombre;
		this.empresa = empresa;
		this.descripcion = descripcion;
		this.stock = stock;
		this.precio = precio;
	}
	
	public Juegos(String img, String nombre, String empresa, String descripcion, Integer stock, Double precio) {
		super();
		this.img = img;
		this.nombre = nombre;
		this.empresa = empresa;
		this.descripcion = descripcion;
		this.stock = stock;
		this.precio = precio;
	}

	public Juegos() {
		super();
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	

	@Override
	public String toString() {
		return "Juegos [id=" + id + ", nombre=" + nombre + ", empresa=" + empresa + ", descripcion=" + descripcion
				+ ", stock=" + stock + ", precio=" + precio + "]";
	}
	
	
}
