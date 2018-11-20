package com.ejemplo.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.ejemplo.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{
	
	// Esta interface extiende de CrudRepository para poder utilizar todos sus métodos
	// y también se pueden crear métodos propios
	

}
