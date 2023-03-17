package com.gameshopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gameshopapp.model.DatosBancarios;


@Repository
public interface IDatosBancariosRepository extends JpaRepository<DatosBancarios, Integer> {

}
