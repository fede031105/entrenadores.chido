package com.upiiz.entrenadores.controllers;

import com.upiiz.entrenadores.entities.Entrenador;
import com.upiiz.entrenadores.services.EntrenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/entrenadores")
@Tag(name = "Entrenadores", description = "API RESTful para gestión de entrenadores con HATEOAS (Nivel 3 Richardson)")
public class EntrenadorController {

    @Autowired
    private EntrenadorService service;

    @GetMapping
    @Operation(summary = "Obtener todos los entrenadores")
    public ResponseEntity<CollectionModel<EntityModel<Entrenador>>> getAll() {
        List<EntityModel<Entrenador>> entrenadores = service.findAll().stream()
                .map(entrenador -> EntityModel.of(entrenador,
                        linkTo(methodOn(EntrenadorController.class).getById(entrenador.getId())).withSelfRel(),
                        linkTo(methodOn(EntrenadorController.class).update(entrenador.getId(), null)).withRel("update"),
                        linkTo(methodOn(EntrenadorController.class).delete(entrenador.getId())).withRel("delete")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Entrenador>> collectionModel = CollectionModel.of(entrenadores,
                linkTo(methodOn(EntrenadorController.class).getAll()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un entrenador por ID")
    public ResponseEntity<EntityModel<Entrenador>> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(entrenador -> {
                    EntityModel<Entrenador> model = EntityModel.of(entrenador);
                    model.add(linkTo(methodOn(EntrenadorController.class).getById(id)).withSelfRel());
                    model.add(linkTo(methodOn(EntrenadorController.class).update(id, null)).withRel("update"));
                    model.add(linkTo(methodOn(EntrenadorController.class).delete(id)).withRel("delete"));
                    model.add(linkTo(methodOn(EntrenadorController.class).getAll()).withRel("all-entrenadores"));
                    return ResponseEntity.ok(model);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo entrenador")
    public ResponseEntity<EntityModel<Entrenador>> create(@RequestBody Entrenador entrenador) {
        Entrenador nuevo = service.save(entrenador);
        EntityModel<Entrenador> model = EntityModel.of(nuevo,
                linkTo(methodOn(EntrenadorController.class).getById(nuevo.getId())).withSelfRel(),
                linkTo(methodOn(EntrenadorController.class).update(nuevo.getId(), null)).withRel("update"),
                linkTo(methodOn(EntrenadorController.class).delete(nuevo.getId())).withRel("delete"),
                linkTo(methodOn(EntrenadorController.class).getAll()).withRel("all-entrenadores"));

        return ResponseEntity.created(linkTo(methodOn(EntrenadorController.class).getById(nuevo.getId())).toUri()).body(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un entrenador existente")
    public ResponseEntity<EntityModel<Entrenador>> update(@PathVariable Long id, @RequestBody Entrenador entrenador) {
        Entrenador actualizado = service.update(id, entrenador);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<Entrenador> model = EntityModel.of(actualizado,
                linkTo(methodOn(EntrenadorController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(EntrenadorController.class).delete(id)).withRel("delete"),
                linkTo(methodOn(EntrenadorController.class).getAll()).withRel("all-entrenadores"));

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un entrenador por ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}