package com.djem.cayenne;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.Constants;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.apache.cayenne.di.Key;
import org.apache.cayenne.resource.ResourceLocator;

@ApplicationScoped
public class DatasourceProducer
{
  @Produces
  @RequestScoped
  public ObjectContext objectContext()
  {
    ObjectContext context = cayenneRuntime.newContext(objectContext);

    // Make sure we do not cache any objects from previous rounds
    DataContext channel = (DataContext) context.getChannel();
    channel.invalidateObjects(channel.getObjectStore().registeredNodes());

    return context;
  }
  private ServerRuntime cayenneRuntime;
  private ObjectContext objectContext;

  @PostConstruct
  public void  init(){
    cayenneRuntime = ServerRuntime.builder()
        .addConfig("cayenne-project.xml")
        .dataSource(DataSourceBuilder.url("jdbc:mysql://localhost:3306/cayenne")
                        .driver(com.mysql.cj.jdbc.Driver.class.getName())
                        .userName("root")
                        .password("root")
                        .pool(1,3).build()
        )
//        .addModule(binder ->{
//          binder.bind(ResourceLocator.class).toInstance(new ClassLoaderResourceLocatorFix());
//          binder.bind(Key.get(ResourceLocator.class, Constants.SERVER_RESOURCE_LOCATOR)).toInstance(new ClassLoaderResourceLocatorFix());
//          binder.bind(ResourceLocator.class).to(ClassLoaderResourceLocatorFix.class);
//          binder.bind(Key.get(ResourceLocator.class, Constants.SERVER_RESOURCE_LOCATOR)).to(ClassLoaderResourceLocatorFix.class);
//        })
        .build();
    objectContext  = cayenneRuntime.newContext();
  }
}
