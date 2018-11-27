package com.ejemplo.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.springboot.backend.apirest.models.entity.Cliente;
import com.ejemplo.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteService.findAll();
		
	}
	
	/*
	@GetMapping("/clientes/{id}")
	public Cliente show(@PathVariable Long id) {
		return clienteService.findById(id);
	}*/
	// Modificaciones en Get para manejar errores
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id){
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta a la Base de Datos!");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@RequestBody Cliente cliente) { 
		// Se envía el Cliente en formato JSON dentro del cuerpo del request, por eso se tiene que anotar con 
		// el Requestbody, para que spring lo pueda mapear.
		
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			clienteNew = clienteService.save(cliente);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la Base de Datos!");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con éxito!");
		response.put("cliente", clienteNew);
		
		return  new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);		
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id ) {
		//Obtenemos el Cliente de la Base de Datos
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteActualizado = null;
		Map<String, Object> response = new HashMap<String,Object>();
		
		if(clienteActual == null) {
			response.put("mensaje", "Error: no se pudo editar el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {		
		//Modificamos el clienteActual, con los enviados por parámetro
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setEmail(cliente.getEmail());
				
		clienteActualizado = clienteService.save(clienteActual);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el cliente en la Base de Datos!");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);			
		}
		response.put("mensaje", "El cliente ha sido actualizado con éxito!");
		response.put("cliente", clienteActualizado);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<String,Object>();
		try {
			clienteService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el cliente de la Base de Datos!");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);	
			
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con éxito!.");
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	

}
