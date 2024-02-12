package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeoPosition {
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "{" +
                "latitude=" + latitude +
                "; longitude=" + longitude +
                '}';
    }
}
