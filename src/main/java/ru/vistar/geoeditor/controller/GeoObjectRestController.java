package ru.vistar.geoeditor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vistar.geoeditor.core.dto.GeoObjectCreateRequest;
import ru.vistar.geoeditor.core.dto.GeoObjectData;
import ru.vistar.geoeditor.core.dto.GeoObjectUpdateRequest;
import ru.vistar.geoeditor.core.service.GeoObjectService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GeoObjectRestController implements GeoObjectApi {
    private final GeoObjectService service;

    @Override
    public ResponseEntity<List<GeoObjectData>> getAll() {
        var objects = service.getAll();
        return objects.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<GeoObjectData> getById(Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<Void> create(GeoObjectCreateRequest request, UriComponentsBuilder uriBuilder) {
        var id = service.create(request);
        return ResponseEntity
                .created(uriBuilder
                        .pathSegment("api", "geo-objects", "{id}")
                        .build(Map.of("id", id)))
                .build();
    }

    @Override
    public ResponseEntity<Void> update(Long id, GeoObjectUpdateRequest request) {
        service.update(id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
} 