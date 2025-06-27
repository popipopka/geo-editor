package ru.vistar.geoeditor.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoObject {
    private Long id;

    private String name;

    private GeoObjectType type;

    private List<GeoCoords> coords;
}
