package ru.vistar.geoeditor.controller.common;

import lombok.Value;

@Value(staticConstructor = "of")
public class ConstraintViolation {
    String field;

    String message;
}

