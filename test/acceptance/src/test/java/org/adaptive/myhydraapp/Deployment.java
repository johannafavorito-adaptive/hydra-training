package org.adaptive.myhydraapp;

import com.weareadaptive.hydra.cucumber.deployment.HydraPlatformDeployment;
import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;
import org.adaptive.myhydraapp.mygateway.components.MyGatewayDriver;

public interface Deployment extends HydraPlatformDeployment
{
    ClusterDriver getMyCluster();

    MyGatewayDriver getMyGateway();

    void snapshotAndShutdown();
}