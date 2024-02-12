package org.example.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CountryData {
    private String country;
    private boolean inEurope;
    private String countryCode;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    @Override
    public String toString() {
        return "{" +
                "country='" + country + '\'' +
                "; inEurope=" + inEurope +
                "; countryCode='" + countryCode + '\'' +
                "; minLatitude=" + minLatitude +
                "; maxLatitude=" + maxLatitude +
                "; minLongitude=" + minLongitude +
                "; maxLongitude=" + maxLongitude +
                '}';
    }
}
