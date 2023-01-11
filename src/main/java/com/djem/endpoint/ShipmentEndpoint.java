package com.djem.endpoint;

import com.djem.dto.ShipmentDto;
import com.djem.model.Shipment;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.CayenneRuntime;
import org.apache.cayenne.configuration.server.ServerModule;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SelectById;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("shipments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShipmentEndpoint {

    @Inject
    ObjectContext objectContext;

    @GET
    @Path("count-maria")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countShipmentsOnMaria() {
        long count = ObjectSelect.query(Shipment.class)
                .selectCount(objectContext);
        return Response.ok(count).build();
    }

    @GET
    @Path("count-postgres")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countShipmentOnPostgres() {
        CayenneRuntime cayenneRuntime = ServerRuntime.builder()
//            .disableModulesAutoLoading() //Either add addModule(new ServerModule()) or add .disableModulesAutoLoading(). One of them are MANDATORY
                .addConfig("cayenne-project.xml")
                .dataSource(DataSourceBuilder.url("jdbc:postgresql://localhost:5435/cayenne")
                        .driver(org.postgresql.Driver.class.getName())
                        .userName("root")
                        .password("root")
                        .pool(1, 3).build()
                )
                .addModule(new ServerModule())
                .build();
        long count = ObjectSelect.query(Shipment.class)
                .selectCount(cayenneRuntime.newContext());
        return Response.ok(count).build();
    }

    @GET
    @Path("{id}")
    public Response getSingleShipment(@PathParam("id") Long id) {
        Shipment shipment = SelectById.query(Shipment.class, id)
                .selectOne(objectContext);
        if(shipment==null) throw new NotFoundException("Id "+id+" does not found!");
        ShipmentDto dto= new ShipmentDto(shipment.getName());
        return Response.ok(dto).build();
    }

    @POST
    public Response createShipment(ShipmentDto dto)
    {
        Shipment shipment = objectContext.newObject(Shipment.class);
        shipment.setName(dto.getName());
        objectContext.commitChanges();
        long id=Cayenne.longPKForObject(shipment);
        return Response.created(URI.create("shipments/"+ id)).build();
    }
}
