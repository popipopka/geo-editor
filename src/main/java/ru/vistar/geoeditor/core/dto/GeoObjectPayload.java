package ru.vistar.geoeditor.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.vistar.geoeditor.core.model.GeoObjectType;
import java.util.List;

@Data
public class GeoObjectPayload {
    @NotBlank
    @Size(min = 1, max = 64)
    private String name;

    @NotNull
    private GeoObjectType type;

    @NotEmpty
    private List<GeoCoordsDto> coords;
}