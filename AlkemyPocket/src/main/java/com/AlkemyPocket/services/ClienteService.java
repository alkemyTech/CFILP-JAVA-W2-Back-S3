package com.AlkemyPocket.services;

import com.AlkemyPocket.model.Cliente;
import com.AlkemyPocket.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> obtenerTodos(){
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }
}
