package com.djem;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.CayenneRuntime;
import org.apache.cayenne.configuration.server.MainCayenneServerModuleProvider;
import org.apache.cayenne.configuration.server.ServerModule;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.apache.cayenne.di.spi.ModuleProvider;
import org.apache.cayenne.query.ObjectSelect;

import com.djem.model.Shipment;
import org.apache.cayenne.reflect.generic.DefaultValueComparisonStrategyFactory;
import org.apache.cayenne.reflect.generic.ValueComparisonStrategyFactory;

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
  @Path("count2")
  @Produces(MediaType.TEXT_PLAIN)
  public Response count2(){
    CayenneRuntime cayenneRuntime = ServerRuntime.builder()
//            .disableModulesAutoLoading() //Either add addModule(new ServerModule()) or add .disableModulesAutoLoading(). One of them are MANDATORY
            .addConfig("cayenne-project.xml")
            .dataSource(DataSourceBuilder.url("jdbc:postgresql://localhost:5435/postgres")
                    .driver(org.postgresql.Driver.class.getName())
                    .userName("root")
                    .password("root")
                    .pool(1,3).build()
            )
            .addModule(new ServerModule())
            .build();
    long count= ObjectSelect.query(Shipment.class)
        .selectCount(cayenneRuntime.newContext());
    return Response.ok(count).build();
  }
  @GET
  public Response getShipments(){
    List<Shipment> shipments = ObjectSelect.query(Shipment.class)
        .select(objectContext);

    return Response.ok(shipments).build();
  }
}
