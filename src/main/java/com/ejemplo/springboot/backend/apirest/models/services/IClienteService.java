package com.ejemplo.springboot.backend.apirest.models.services;

import java.util.List;

import com.ejemplo.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	// Interface creada para definir los métodos que va a utilizar el Servicio Cliente
	
	
	public List<Cliente> findAll();
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);

}
