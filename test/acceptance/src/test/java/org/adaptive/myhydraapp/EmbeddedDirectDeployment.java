package org.adaptive.myhydraapp;

import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;
import org.adaptive.myhydraapp.mycluster.components.testing.cucumber.MyClusterEmbeddedDirectDeployment;
import org.adaptive.myhydraapp.mygateway.MyGatewayMain;
import org.adaptive.myhydraapp.mycluster.MyClusterMain;
import org.adaptive.myhydraapp.mygateway.components.MyGatewayDriver;
import org.adaptive.myhydraapp.mygateway.components.testing.cucumber.MyGatewayEmbeddedDirectDeployment;
import org.agrona.CloseHelper;

public class EmbeddedDirectDeployment implements Deployment
{
    private static final String CONFIG_RESOURCE_NAME = "functional-test";
    private final MyClusterEmbeddedDirectDeployment clusterDeployment;
    private final MyGatewayEmbeddedDirectDeployment gatewayDeployment;

    public EmbeddedDirectDeployment(
        final MyClusterEmbeddedDirectDeployment clusterDeployment,
        final MyGatewayEmbeddedDirectDeployment gatewayDeployment
    )
    {
        this.clusterDeployment = clusterDeployment;
        this.gatewayDeployment = gatewayDeployment;
    }

    @Override
    public ClusterDriver getMyCluster()
    {
        return clusterDeployment.getMyCluster(CONFIG_RESOURCE_NAME, MyClusterMain.BOOTSTRAPPER);
    }

    @Override
    public MyGatewayDriver getMyGateway()
    {
        return gatewayDeployment.getMyGateway(CONFIG_RESOURCE_NAME, MyGatewayMain.BOOTSTRAPPER);
    }

    @Override
    public void snapshotAndShutdown() {
        clusterDeployment.snapshot();
        close();
    }

    @Override
    public void close()
    {
        CloseHelper.closeAll(clusterDeployment, gatewayDeployment);
    }

}
