package com.djem.cayenne;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.types.ValueObjectTypeRegistry;
import org.apache.cayenne.configuration.Constants;
import org.apache.cayenne.configuration.server.ServerModule;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.apache.cayenne.di.Inject;
import org.apache.cayenne.di.Key;
import org.apache.cayenne.reflect.generic.DefaultValueComparisonStrategyFactory;
import org.apache.cayenne.reflect.generic.ValueComparisonStrategyFactory;
import org.apache.cayenne.resource.ResourceLocator;

@ApplicationScoped
public class DatasourceProducer
{
  @Produces
  @RequestScoped
  public ObjectContext objectContext()
  {
//    org.apache.cayenne.configuration.server.MainCayenneServerModuleProvider;

    ObjectContext context = cayenneRuntime.newContext(objectContext);

    // Make sure we do not cache any objects from previous rounds
    DataContext channel = (DataContext) context.getChannel();
    channel.invalidateObjects(channel.getObjectStore().registeredNodes());

    return context;
  }
  private ServerRuntime cayenneRuntime;
  private ObjectContext objectContext;

//  @Inject ValueObjectTypeRegistry valueObjectTypeRegistry;
  @PostConstruct
  public void  init(){
    cayenneRuntime = ServerRuntime.builder()
        .disableModulesAutoLoading()
        .addConfig("cayenne-project.xml")
        .dataSource(DataSourceBuilder.url("jdbc:mysql://localhost:3308/cayenne")
                        .driver(com.mysql.cj.jdbc.Driver.class.getName())
                        .userName("root")
                        .password("root")
                        .pool(1,3).build()
        )
            .addModule(new ServerModule())
            .addModule(binder ->{
              //binder.bind(DataDomainFlushActionFactory.class).to(DefaultDataDomainFlushActionFactory.class);
//          binder.bind(ValueComparisonStrategyFactory.class).toInstance(new DefaultValueComparisonStrategyFactory(valueObjectTypeRegistry));
//          binder.bind(ResourceLocator.class).toInstance(new ClassLoaderResourceLocatorFix());
//          binder.bind(Key.get(ResourceLocator.class, Constants.SERVER_RESOURCE_LOCATOR)).toInstance(new ClassLoaderResourceLocatorFix());
        })
        .build();
    objectContext  = cayenneRuntime.newContext();
  }
}
