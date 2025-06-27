package ru.vistar.geoeditor.core.exception;

public class GeoObjectNotFoundException extends RuntimeException {
    public GeoObjectNotFoundException(Long id) {
        super("Geo object with id " + id + " not found");
    }
}
