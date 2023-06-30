package model.params;

import java.io.Serializable;

public record AirwayParams(float maxSpeed, String beginning, String end) implements Serializable { }
