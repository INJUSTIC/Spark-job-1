package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonData {
    private String _type;
    private long _id;
    private String key;
    private String name;
    private String fullName;
    private long location_id;
    private String iata_airport_code;
    private String type;
    private CountryData countryData;
    private GeoPosition geoPosition;
    private boolean coreCountry;
    private int distanceInKm;
}

