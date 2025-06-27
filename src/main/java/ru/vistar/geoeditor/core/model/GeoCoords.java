package ru.vistar.geoeditor.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoCoords {
    private double lat;
    private double lon;
}
