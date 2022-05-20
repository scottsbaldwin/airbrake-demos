package io.airbrake.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.airbrake.javabrake.Airbrake;
import io.airbrake.javabrake.Notice;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<CustomException> {
    @Override
    public Response toResponse(CustomException e) {
        Notice n = Airbrake.buildNotice(e);
        n.setParam("weatherResourceVersion", "v4");
        Map<String, String> u = new HashMap<String, String>();
        u.put("id", "456");
        u.put("name", "Joe Global Bloggs");
        u.put("email", "joe.global@example.com");
        u.put("username", "joeglobalbloggs");
        n.setContext("user", u);
        n.setParam("statusCode", Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        Airbrake.send(n);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
