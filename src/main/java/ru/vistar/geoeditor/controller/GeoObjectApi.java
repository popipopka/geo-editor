package ru.vistar.geoeditor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vistar.geoeditor.core.dto.GeoObjectData;
import ru.vistar.geoeditor.core.dto.GeoObjectPayload;

import java.util.List;

@Validated
@RequestMapping(value = "/api/geo-objects", produces = "application/json")
@Tag(name = "Географические объекты", description = "API для работы с географическими объектами")
public interface GeoObjectApi {

    @Operation(
            summary = "Получить все географические объекты",
            description = "Возвращает список всех географических объектов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список объектов")
            }
    )
    @GetMapping
    ResponseEntity<List<GeoObjectData>> getAll();

    @Operation(
            summary = "Получить географический объект по ID",
            description = "Возвращает географический объект по указанному ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Найденная обложка"),
                    @ApiResponse(responseCode = "404", description = "Объект не найден", content = @Content)

            }
    )
    @GetMapping("/{id}")
    ResponseEntity<GeoObjectData> getById(@Positive @PathVariable Long id);

    @Operation(
            summary = "Создать новый географический объект",
            description = "Создает новый географический объект",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Объект успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса", content = @Content)
            }
    )
    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody GeoObjectPayload payload, UriComponentsBuilder uriBuilder);

    @Operation(
            summary = "Обновить географический объект",
            description = "Обновляет существующий географический объект с указанным ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объект успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса запроса", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Объект не найден", content = @Content)
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<Void> update(@Positive @PathVariable Long id, @Valid @RequestBody GeoObjectPayload payload);

    @Operation(
            summary = "Удалить географический объект",
            description = "Удаляет географический объект по указанному ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Объект успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Объект не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Positive @PathVariable Long id);
}