package com.djem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import com.djem.model.Shipment;

@Path("shipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentResource
{

  @Inject
  ObjectContext objectContext;

  @GET
  @Path("hello")
  @Produces(MediaType.TEXT_PLAIN)
  public Response sayHello(){
    return Response.ok("Hello World").build();
  }
  @GET
  @Path("count")
  @Produces(MediaType.TEXT_PLAIN)
  public Response count(){
    long count= ObjectSelect.query(Shipment.class)
        .selectCount(objectContext);
    return Response.ok(count).build();
  }
  @GET
  public Response getShipments(){
    List<Shipment> shipments = ObjectSelect.query(Shipment.class)
        .select(objectContext);

    return Response.ok(shipments).build();
  }
}
