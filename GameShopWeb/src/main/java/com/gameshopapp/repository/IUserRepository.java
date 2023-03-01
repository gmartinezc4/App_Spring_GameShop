package com.gameshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gameshopapp.model.User;

//Repository hace que:
//Interfaz encargada de realizar los cambios, en este caso los cambios del usuarioa (como guardar, modificar o eliminar)
//Se conecta a la bbdd y gestiona la info

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>{
	//con la extensión JpaRepository ya tenemos todos los cruds (funciones) 
	
}
