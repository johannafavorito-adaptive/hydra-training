package org.adaptive.myhydraapp.mygateway.service;

import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import org.adaptive.myhydraapp.mygateway.entities.MutableCorrelationIdResponse;
import org.adaptive.myhydraapp.mygateway.services.CorrelationService;
import org.adaptive.myhydraapp.mygateway.services.CorrelationServiceClientProxy;

public class CorrelationServiceImpl implements CorrelationService {

    private final CorrelationServiceClientProxy clientProxy;

    public CorrelationServiceImpl(CorrelationServiceClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public void correlationEcho(UniqueId correlationId) {
        try (final MutableCorrelationIdResponse response = clientProxy.acquireCorrelationIdResponse()) {
            response.correlationId().copyFrom(correlationId);
            clientProxy.onCorrelationEchoResponse(correlationId, response);
        }
    }

}
