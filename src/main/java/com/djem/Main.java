package com.djem;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.apache.cayenne.query.ObjectSelect;

import com.djem.model.Shipment;
public class Main {

    public static void main(String[] args) {
        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .addConfig("cayenne-project.xml")
            .dataSource(DataSourceBuilder.url("jdbc:mysql://localhost:3306/cayenne")
                            .driver(com.mysql.cj.jdbc.Driver.class.getName())
                            .userName("root")
                            .password("root")
                            .pool(1,3).build()
            )
                .build();
        insertToTable(cayenneRuntime);
    }

    private static void insertToTable(ServerRuntime cayenneRuntime)
    {
        ObjectContext context = cayenneRuntime.newContext();
        Shipment shipment = context.newObject(Shipment.class);
        shipment.setName("Pablo Picasso");
        context.commitChanges();
    }
}
