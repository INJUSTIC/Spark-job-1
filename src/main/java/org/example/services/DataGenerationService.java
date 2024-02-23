package org.example.services;

import org.example.model.CountryData;
import org.example.model.GeoPosition;
import org.example.model.PersonData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerationService {
    private final String[] namePool;
    private final CountryData[] countryDataPool;

    public DataGenerationService() {
        namePool = new String[] {
                "Adam", "Agnieszka", "Aleksander", "Alicja", "Andrzej", "Anna", "Bartosz",
                "Beata", "Czesław", "Danuta", "Damian", "Dominika", "Edward", "Elżbieta",
                "Eugeniusz", "Ewa", "Franciszek", "Gabriela", "Grzegorz", "Halina", "Henryk",
                "Irena", "Ireneusz", "Izabela", "Jan", "Joanna", "Józef", "Julia", "Justyna",
                "Karol", "Katarzyna", "Kazimierz", "Kinga", "Krzysztof", "Lucyna", "Łukasz",
                "Małgorzata", "Marek", "Marta", "Michał", "Monika", "Natalia", "Piotr", "Renata",
                "Robert", "Sylwia", "Tomasz", "Urszula", "Wojciech", "Zofia"
        };

        //pula krajów z informacją dla dalniejszej losowej generacji
        countryDataPool = new CountryData[] {
                new CountryData("Poland", true, "PL", 49.00, 54.84, 14.12, 24.15),
                new CountryData("Germany", true, "DE", 47.27, 55.06, 5.87, 15.04),
                new CountryData("France", true, "FR", 41.37, 51.12, -5.14, 9.56),
                new CountryData("Spain", true, "ES", 27.64, 43.79, -18.17, 4.32),
                new CountryData("Sweden", true, "SE", 55.34, 69.06, 11.11, 24.17),
                new CountryData("Italy", true, "IT", 35.49, 47.09, 6.63, 18.53),
                new CountryData("Ukraine", true, "UA", 44.38, 52.37, 22.14, 40.16),
                new CountryData("Japan", false, "JP", 24.40, 45.52, 122.93, 153.98),
                new CountryData("China", false, "CN", 18.16, 53.56, 73.62, 134.77),
                new CountryData("Canada", false, "CA", 41.68, 83.11, -141.01, -52.62),
                new CountryData("United States", false, "US", 24.40, 49.38, -125.00, -66.93),
                new CountryData("Brazil", false, "BR", -33.75, 5.27, -74.01, -34.80),
                new CountryData("India", false, "IN", 6.75, 35.50, 68.19, 97.41),
                new CountryData("Australia", false, "AU", -43.66, -10.05, 113.09, 153.63)
        };
    }

    public List<PersonData> generateRandomPersonDataList(int size) {
        Random random = new Random();
        List<PersonData> personDataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PersonData personDataObject = new PersonData();
            personDataObject.set_type("Position" + i);
            personDataObject.set_id(random.nextLong(1, Long.MAX_VALUE));
            personDataObject.setKey("key" + i);
            personDataObject.setName(getRandomName());
            personDataObject.setIata_airport_code("IataAirportCode" + i);
            personDataObject.setType("location" + i);
            personDataObject.setLocation_id(random.nextLong(1, Long.MAX_VALUE));
            personDataObject.setCountryData(getRandomCountryData());
            personDataObject.setFullName(personDataObject.getName() + ", " + personDataObject.getCountryData().getCountry());
            personDataObject.setGeoPosition(getRandomGeoPosition(personDataObject.getCountryData()));
            personDataObject.setCoreCountry(random.nextBoolean());
            personDataObject.setDistanceInKm(random.nextInt(5, 5000));
            personDataList.add(personDataObject);
        }
        return personDataList;
    }

    private String getRandomName() {
        Random random = new Random();
        int nameIndex = random.nextInt(0, namePool.length);
        return namePool[nameIndex];
    }

    private CountryData getRandomCountryData() {
        Random random = new Random();
        int countryDataIndex = random.nextInt(0, countryDataPool.length);
        return countryDataPool[countryDataIndex];
    }

    private GeoPosition getRandomGeoPosition(CountryData locationData) {
        Random random = new Random();
        double randomLatitude = random.nextDouble(locationData.getMinLatitude(), locationData.getMaxLatitude());
        double randomLongitude = random.nextDouble(locationData.getMinLongitude(), locationData.getMaxLongitude());
        return new GeoPosition(randomLatitude, randomLongitude);
    }
}
