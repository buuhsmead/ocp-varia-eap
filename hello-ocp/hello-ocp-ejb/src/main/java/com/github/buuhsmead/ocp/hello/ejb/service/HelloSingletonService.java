package com.github.buuhsmead.ocp.hello.ejb.service;


import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;

class HelloSingletonService implements Service<Node> {

    private Logger LOG = Logger.getLogger(this.getClass());

    private InjectedValue<Group> group;

    HelloSingletonService(InjectedValue<Group> group) {
        this.group = group;
    }

    @Override
    public void start(StartContext context) throws StartException {
        LOG.infof("Singleton service is starting on node '%s'.", this.group.getValue().getLocalNode());
    }

    @Override
    public void stop(StopContext context) {
        LOG.infof("Singleton service is stopping on node '%s'.", this.group.getValue().getLocalNode());
    }

    @Override
    public Node getValue() throws IllegalStateException, IllegalArgumentException {
        return this.group.getValue().getLocalNode();
    }
}
