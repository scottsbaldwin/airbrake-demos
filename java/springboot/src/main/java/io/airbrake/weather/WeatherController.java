package io.airbrake.weather;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
public class WeatherController {
    @GetMapping("/")
    public String index() {
        return "This is the weather API proxy";
    }

    @GetMapping("date")
    public String date() {
        WeatherService svc = new WeatherService();
        return svc.GetDate();
    }

    @GetMapping("locations")
    public String locations() {
        WeatherService svc = new WeatherService();
        return svc.GetLocations();
    }

    @GetMapping("weather/{location}")
    public String weather(@PathVariable String location) throws Exception {
        WeatherService svc = new WeatherService();
        return svc.GetWeather(location);
    }
}
