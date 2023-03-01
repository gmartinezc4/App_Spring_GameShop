package com.gameshopapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
	@Id //indicamos que es un id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //usamos una estrategia para auto-incrementar cada vez que hagamos un nuevo producto
	private Integer id;
	private String nombre;
	private String apellido;
	private String sexo;
	private String telefono;
	private String email;
	private String password;
	private String passwordRepeat;
	private Integer idDatosBancarios;
	
	
	public User(Integer id, String nombre, String apellido, String sexo, String telefono, String email,
			String password, String passwordRepeat, Integer idDatosBancarios) { //Collection<Rol> roles
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
		this.idDatosBancarios = idDatosBancarios;
	}

	public User(String nombre, String apellido, String sexo, String telefono, String email, 
			String contraseña, String passwordRepeat, Integer idDatosBancarios) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.telefono = telefono;
		this.email = email;
		this.password = contraseña;
		this.passwordRepeat = passwordRepeat;
		this.idDatosBancarios = idDatosBancarios;
	}

	public User() {
		
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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String contraseña) {
		this.password = contraseña;
	}
	
	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

	public Integer getIdDatosBancarios() {
		return idDatosBancarios;
	}

	public void setIdDatosBancarios(Integer idDatosBancarios) {
		this.idDatosBancarios = idDatosBancarios;
	}

	@Override
	public String toString() {
		return "FichaUser [nombre=" + nombre + ", apellido=" + apellido + ", sexo=" + sexo + ", telefono=" + telefono
				+ ", email=" + email + "]";
	}
	
	
}
