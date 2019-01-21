
package com.github.buuhsmead.ocp.hello.ejb.service;

import org.jboss.logging.Logger;
import org.jboss.msc.service.*;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;
import org.wildfly.clustering.singleton.SingletonDefaultRequirement;
import org.wildfly.clustering.singleton.SingletonPolicy;


public class HelloServiceActivator implements ServiceActivator {


    private final Logger LOG = Logger.getLogger(ServiceActivator.class);

    public static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.parse("com.github.buuhsmead.ocp.hello.ejb.service.primary-only");
    public static final ServiceName QUERYING_SERVICE_NAME  = ServiceName.parse("com.github.buuhsmead.ocp.hello.ejb.service.primary-only.querying");


    public void activate(ServiceActivatorContext serviceActivatorContext) throws ServiceRegistryException {
        try {
            SingletonPolicy policy = (SingletonPolicy) serviceActivatorContext
                    .getServiceRegistry()
                    .getRequiredService(ServiceName.parse(SingletonDefaultRequirement.SINGLETON_POLICY.getName()))
                    .awaitValue();

            InjectedValue<Group> group = new InjectedValue<>();

            Service<Node> service = new HelloSingletonService(group);

            policy.createSingletonServiceBuilder(SINGLETON_SERVICE_NAME, service)
                    .build(serviceActivatorContext.getServiceTarget())
                    .addDependency(ServiceName.parse("org.wildfly.clustering.default-group"), Group.class, group)
                    .install();

            serviceActivatorContext.getServiceTarget()
                    .addService(QUERYING_SERVICE_NAME, new HelloQueryingService())
                    .setInitialMode(ServiceController.Mode.ACTIVE)
                    .install();

            LOG.info("Singleton and querying services activated.");
        } catch (InterruptedException e) {
            throw new ServiceRegistryException(e);
        }
    }

}
