package com.github.buuhsmead.ocp.hello.ejb.service;

import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.wildfly.clustering.group.Node;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This service will regularly query the singleton service and log where the singleton service is running. For the sake
 * of illustration, the value returned by the singleton service {@link SingletonService#getValue()} is the node where it
 * is currently running.
 *
 * @author Radoslav Husar
 */
public class HelloQueryingService implements Service<Void> {

    private Logger LOG = Logger.getLogger(this.getClass());
    private ScheduledExecutorService executor;

    @Override
    public void start(StartContext context) throws StartException {
        LOG.info("Querying service is starting.");

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {

            @SuppressWarnings("unchecked")
            ServiceController<Node> service = (ServiceController<Node>) context.getController().getServiceContainer()
                    .getService(HelloServiceActivator.SINGLETON_SERVICE_NAME);
            try {
                Node node = service.awaitValue(5, TimeUnit.SECONDS);

                LOG.infof("Singleton service is running on node '%s'.", node);
            } catch (InterruptedException | TimeoutException | IllegalStateException e) {
                LOG.warn("Failed to query singleton service.");
            }

        }, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop(StopContext context) {
        LOG.info("Querying service is stopping.");

        executor.shutdown();
    }

    @Override
    public Void getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }

}