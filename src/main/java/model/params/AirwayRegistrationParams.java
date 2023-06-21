package model.params;

import java.io.Serializable;
import java.util.List;

public record AirwayRegistrationParams(List<String> route, String name, Float maxSpeed) implements Serializable { }
