package com.upiiz.entrenadores.services;

import com.upiiz.entrenadores.entities.Entrenador;
import com.upiiz.entrenadores.repositories.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntrenadorService {

    @Autowired
    private EntrenadorRepository repository;

    public List<Entrenador> findAll() {
        return repository.findAll();
    }

    public Optional<Entrenador> findById(Long id) {
        return repository.findById(id);
    }

    public Entrenador save(Entrenador entrenador) {
        return repository.save(entrenador);
    }

    public Entrenador update(Long id, Entrenador entrenadorDetalles) {
        return repository.findById(id).map(entrenador -> {
            entrenador.setNombre(entrenadorDetalles.getNombre());
            entrenador.setEspecialidad(entrenadorDetalles.getEspecialidad());
            entrenador.setExperiencia(entrenadorDetalles.getExperiencia());
            entrenador.setCorreo(entrenadorDetalles.getCorreo());
            return repository.save(entrenador);
        }).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}