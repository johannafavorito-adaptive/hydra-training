package org.adaptive.myhydraapp.mycluster;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.engine.bindings.ClusterNodeBinding;
import io.aeron.cluster.MillisecondClusterClock;
import org.adaptive.myhydraapp.mycluster.components.MyCluster;
import org.adaptive.myhydraapp.mycluster.components.MyClusterBootstrapper;
import org.adaptive.myhydraapp.mycluster.service.MyEchoService;

public class MyClusterMain
{

    public static final MyClusterBootstrapper BOOTSTRAPPER = context -> {
        final ClusterDatabase database = new ClusterDatabase();
        context.registerDatabase(database);

        final MyEchoService echoService = new MyEchoService(context.channelToClients().getEchoServiceClientProxy(), database.echoRecord());
        context.channelToClients().registerEchoService(echoService);
    };

    public static void main(String[] args) {
        final Logger log = LoggerFactory.getNotThreadSafeLogger(MyClusterMain.class);
        log.info("Starting MyCluster").log();

        try (ClusterNodeBinding binding = MyCluster.node(BOOTSTRAPPER, new MillisecondClusterClock()).run())
        {
            binding.awaitShutdown();
        }
    }

}
