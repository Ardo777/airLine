package com.example.airlineproject.util;

import com.example.airlineproject.entity.City;
import com.example.airlineproject.entity.Country;
import com.example.airlineproject.repository.CityRepository;
import com.example.airlineproject.repository.CountryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CitiesAndCountries {

    @Value("${api.url}")
    private  String URL;

    private final RestTemplate restTemplate;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;


    public void getAllCountriesAndCities() {
        try {
            String jsonString = retrieveDataFromRemoteService();
            processData(jsonString);
            ResponseEntity.ok("Data saved successfully");
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e);
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    private String retrieveDataFromRemoteService() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        return responseEntity.getBody();
    }

    private void processData(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode dataArray = rootNode.get("data");

        if (dataArray != null && dataArray.isArray()) {
            for (JsonNode dataNode : dataArray) {
                processCityAndCountry(dataNode);
            }
        }
    }

    private void processCityAndCountry(JsonNode dataNode) {
        String city = dataNode.get("city").asText();
        String country = dataNode.get("country").asText();

        log.info("Processing city: {} in country: {}", city, country);

        Country existingCountry = countryRepository.findByName(country);
        if (existingCountry == null) {
            existingCountry = countryRepository.save(Country.builder().name(country).build());
            log.info("Saved new country: {}", existingCountry.getName());
        }
        cityRepository.save(City.builder().name(city).country(existingCountry).build());
        log.info("Saved city: {} in country: {}", city, country);
    }

}