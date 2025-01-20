package org.adaptive.myhydraapp;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;
import org.adaptive.myhydraapp.mygateway.components.MyGatewayDriver;
import org.adaptive.myhydraapp.mygateway.components.testing.cucumber.MyGatewayExistingDeployment;

public class ExistingDeployment implements Deployment {

    private static final String CONFIG_RESOURCE_NAME = "functional-tests";
    private final Logger log = LoggerFactory.getNotThreadSafeLogger(ExistingDeployment.class);
    private final MyGatewayExistingDeployment gatewayDeployment;

    public ExistingDeployment(MyGatewayExistingDeployment gatewayDeployment) {
        this.gatewayDeployment = gatewayDeployment;
    }

    @Override
    public ClusterDriver getMyCluster() {
        log.info("Skipping start cluster in existing mode").log();
        return null;
    }

    @Override
    public MyGatewayDriver getMyGateway() {
        return gatewayDeployment.getMyGateway(CONFIG_RESOURCE_NAME);
    }

    @Override
    public void snapshotAndShutdown() {
        throw new RuntimeException("Cannot snapshot the system in Existing mode");
    }
}
