package com.gameshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gameshopapp.model.Comentarios;

@Repository
public interface IComentariosRepository extends JpaRepository<Comentarios, Integer> {

}
