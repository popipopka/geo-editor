package ru.vistar.geoeditor.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.vistar.geoeditor.core.model.GeoObjectType;

import java.util.List;

@Data
@Schema(description = "Данные для создания или обновления географического объекта")
public class GeoObjectPayload {

    @NotBlank
    @Size(min = 1, max = 64)
    @Schema(description = "Название географического объекта", example = "Московский Кремль")
    private String name;

    @NotNull
    @Schema(description = "Тип географического объекта")
    private GeoObjectType type;

    @NotEmpty
    private List<GeoCoordsData> coords;
}