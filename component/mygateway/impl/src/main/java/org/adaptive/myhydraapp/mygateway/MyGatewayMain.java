package org.adaptive.myhydraapp.mygateway;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.config.Configurer;
import com.weareadaptive.hydra.platform.web.bindings.WebBinding;
import org.adaptive.myhydraapp.mygateway.components.MyGateway;
import org.adaptive.myhydraapp.mygateway.components.MyGatewayBootstrapper;
import org.adaptive.myhydraapp.mygateway.service.CorrelationServiceImpl;
import org.adaptive.myhydraapp.mygateway.service.EchoSubscriptionServiceImpl;

public class MyGatewayMain
{
    public static final MyGatewayBootstrapper BOOTSTRAPPER = context ->
    {
        context.channelToWebSockets().registerEchoService(context.channelToCluster().getEchoServiceProxy());
        context.channelToCluster().registerEchoServiceClient(context.channelToWebSockets().getEchoServiceClientProxy());

        final CorrelationServiceImpl correlationService = new CorrelationServiceImpl(context.channelToWebSockets().getCorrelationServiceClientProxy());
        context.channelToWebSockets().registerCorrelationService(correlationService);

        final EchoSubscriptionServiceImpl echoSubscriptionService = new EchoSubscriptionServiceImpl(context.channelToWebSockets().getEchoSubscriptionServiceClientProxy());
        context.channelToWebSockets().registerEchoSubscriptionService(echoSubscriptionService);
        context.channelToCluster().registerEchoServiceClient(echoSubscriptionService);
    };

    public static void main(String[] args) {
        final Logger log = LoggerFactory.getNotThreadSafeLogger(MyGatewayMain.class);
        log.info("Starting MyGateway").log();

        final Configurer<WebBinding> configurer = MyGateway.gateway(BOOTSTRAPPER);
        try (WebBinding webBinding = configurer.run())
        {
            webBinding.awaitShutdown();
        }
    }
}