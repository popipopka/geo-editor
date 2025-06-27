package ru.vistar.geoeditor.controller;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vistar.geoeditor.core.dto.GeoObjectData;
import ru.vistar.geoeditor.core.dto.GeoObjectPayload;
import java.util.List;

@Validated
@RequestMapping(value = "/api/geo-objects", produces = "application/json")
public interface GeoObjectApi {
    @GetMapping
    ResponseEntity<List<GeoObjectData>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<GeoObjectData> getById(@PositiveOrZero @PathVariable Long id);

    @PostMapping
    ResponseEntity<Void> create(@RequestBody GeoObjectPayload payload, UriComponentsBuilder uriBuilder);

    @PutMapping("/{id}")
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody GeoObjectPayload payload);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
} 