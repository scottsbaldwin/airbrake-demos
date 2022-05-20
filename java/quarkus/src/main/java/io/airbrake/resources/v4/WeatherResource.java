package io.airbrake.resources.v4;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.airbrake.exception.CustomException;
import io.airbrake.exception.InvalidLocationException;
import io.airbrake.services.WeatherService;

@Path("/api/v4")
public class WeatherResource {
    @Inject
    @RestClient
    WeatherService weatherService;

    @GET
    @Path("date")
    @Produces(MediaType.TEXT_PLAIN)
    public String date() {
        Response r = weatherService.GetDate();
        return r.readEntity(String.class);
    }

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    public String locations() {
        Response r = weatherService.GetLocations();
        return r.readEntity(String.class);
    }

    @GET
    @Path("weather/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(
        name = "weather",
        description = "Timer for requests to v4 weather"
    )
    public Response weather(String location) throws InvalidLocationException, CustomException, JsonProcessingException {
        // Gets the valid locations for the weather service
        String locationsJson = locations();
        ObjectMapper mapper = new ObjectMapper();
        List<String> locations = Arrays.asList(mapper.readValue(locationsJson, String[].class));

        // Validate that the requested location is supported
        // BUT let the location name of "bypass" through
        if(!locations.contains(location) && !"bypass".equals(location)) {
            String msg = String.format("%s is not a valid location", location);
            throw new InvalidLocationException(msg);
        }

        // Get the weather for the location
        try {
            Response r = weatherService.GetWeather(location);
            return Response.status(Response.Status.OK)
                                .entity(r.readEntity(String.class))
                                .build();
        } catch (Exception e) {
            throw new CustomException("Weather service returned an error, what should I do?", e);
        }
    }
}