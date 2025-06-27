package ru.vistar.geoeditor.core.dto;

import lombok.Data;
import ru.vistar.geoeditor.core.model.GeoObjectType;
import java.util.List;

@Data
public class GeoObjectData {
    private Long id;
    
    private String name;
    
    private GeoObjectType type;
    
    private List<GeoCoordsDto> coords;
} 