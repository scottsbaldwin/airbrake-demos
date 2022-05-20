package io.airbrake.resources.v3;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.airbrake.services.WeatherService;

@Path("/api/v3")
public class WeatherResource {
    private static final Logger LOG = Logger.getLogger(WeatherResource.class);

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
        description = "Timer for requests to v3 weather"
    )
    public Response weather(String location) throws Exception {
        try {
            // Gets the valid locations for the weather service
            String locationsJson = locations();
            ObjectMapper mapper = new ObjectMapper();
            List<String> locations = Arrays.asList(mapper.readValue(locationsJson, String[].class));

            // Validate that the requested location is supported
            if(!locations.contains(location)) {
                String msg = String.format("%s is not a valid location", location);
                return Response.status(Response.Status.NOT_FOUND)
                                .type(MediaType.TEXT_PLAIN_TYPE)
                                .entity(msg)
                                .build();
            }

            // Get the weather for the location
            Response r = weatherService.GetWeather(location);
            if(r.getStatus() != HttpStatus.SC_OK) {
                String msg = String.format("Weather service returned %d, what should I do?", r.getStatus());
                // TODO: log4j isn't working
                LOG.error(msg);
                throw new Exception(msg);
            }
            
            return Response.status(Response.Status.OK)
                                .entity(r.readEntity(String.class))
                                .build();
        } catch (Exception ex) {
            String msg = "Something went wrong!";
            // TODO: log4j isn't working
            LOG.error(msg, ex);
            throw new Exception(msg, ex);
        }
    }
}