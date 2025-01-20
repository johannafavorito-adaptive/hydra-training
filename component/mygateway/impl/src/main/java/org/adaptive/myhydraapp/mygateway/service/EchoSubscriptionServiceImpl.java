package org.adaptive.myhydraapp.mygateway.service;

import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import org.adaptive.myhydraapp.mycluster.entities.EchoResponse;
import org.adaptive.myhydraapp.mycluster.services.EchoServiceClient;
import org.adaptive.myhydraapp.mygateway.services.EchoSubscriptionService;
import org.adaptive.myhydraapp.mygateway.services.EchoSubscriptionServiceClientProxy;

import java.util.HashSet;
import java.util.Set;

public class EchoSubscriptionServiceImpl implements EchoSubscriptionService, EchoServiceClient {

    private final EchoSubscriptionServiceClientProxy clientProxy;
    private Set<UniqueId> subscribers = new HashSet<>();

    public EchoSubscriptionServiceImpl(EchoSubscriptionServiceClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public void subscribe(UniqueId correlationId) {
        subscribers.add(correlationId.allocateClone());
    }

    @Override
    public void cancelSubscribe(UniqueId correlationId) {
        subscribers.remove(correlationId);
    }

    @Override
    public void onBroadcastEvents(EchoResponse echoResponse) {
        subscribers.forEach(correlationId -> clientProxy.onSubscribeResponse(correlationId, echoResponse));
    }
}
