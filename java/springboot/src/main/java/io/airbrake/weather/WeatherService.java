package io.airbrake.weather;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public String GetDate() {
        String url = "https://airbrake.github.io/weatherapi/date";
        String resp = this.restTemplate.getForObject(url, String.class);
        long l = Long.parseLong(resp.trim());
        Date d = new Date(l * 1000);
        return d.toString();
    }

    public String GetLocations() {
        String url = "https://airbrake.github.io/weatherapi/locations";
        String resp = this.restTemplate.getForObject(url, String.class);
        return resp;
    }

    public String GetWeather(String location) throws Exception {
        String url = "https://airbrake.github.io/weatherapi/weather/{location}";
        ResponseEntity<String> resp = new ResponseEntity<>(HttpStatus.OK);
        try {
            resp = this.restTemplate.getForEntity(url, String.class, location);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().is4xxClientError()) {
                System.out.printf("Something went wrong, please check your request and try again, status code = %d\n",
                        ex.getRawStatusCode());
            } else if (ex.getStatusCode().is5xxServerError()) {
                System.out.printf("The server barfed, status code = %d\n", ex.getRawStatusCode());
            } else {
                throw new Exception("There was a problem retrieving weather for " + location, ex);
            }
        }
        return resp.getBody();
    }
}
