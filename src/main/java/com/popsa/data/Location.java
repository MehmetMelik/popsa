package com.popsa.data;

import java.util.List;

public record Location(String title, String id, String resultType, String houseNumberType, Address address,
                       Position position, List<Position> access, int distance, MapView mapView) {
}
