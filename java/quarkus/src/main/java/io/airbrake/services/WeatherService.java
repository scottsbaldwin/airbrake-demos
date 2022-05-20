package io.airbrake.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface WeatherService {
    @GET
    @Path("date")
    Response GetDate();

    @GET
    @Path("locations")
    @Produces(MediaType.APPLICATION_JSON)
    Response GetLocations();

    @GET
    @Path("weather/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    Response GetWeather(@PathParam("location") String location);
}
