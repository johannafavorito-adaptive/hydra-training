package org.adaptive.myhydraapp.mygateway.service;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.allocated.AllocatedUniqueId;
import com.weareadaptive.hydra.platform.commontypes.entities.MutableUniqueId;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import org.adaptive.myhydraapp.mygateway.entities.MutableCorrelationIdResponse;
import org.adaptive.myhydraapp.mygateway.entities.MutableLastCorrelationIdResponse;
import org.adaptive.myhydraapp.mygateway.services.CorrelationService;
import org.adaptive.myhydraapp.mygateway.services.CorrelationServiceClientProxy;

public class CorrelationServiceImpl implements CorrelationService {

    private final Logger log = LoggerFactory.getNotThreadSafeLogger(CorrelationService.class);
    private final CorrelationServiceClientProxy clientProxy;
    private final MutableUniqueId lastCorrelationId = new AllocatedUniqueId();

    public CorrelationServiceImpl(CorrelationServiceClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public void correlationEcho(UniqueId correlationId) {
        log.info("Setting last correlation ID to: ").append(correlationId).log();
        try (final MutableCorrelationIdResponse response = clientProxy.acquireCorrelationIdResponse()) {
            correlationId.copyTo(lastCorrelationId);
            clientProxy.onCorrelationEchoResponse(correlationId, response);
        }
    }

    @Override
    public void lastCorrelationEcho(UniqueId correlationId) {
        log.info("Returning last correlation ID of: ").append(lastCorrelationId).log();
        try (final MutableLastCorrelationIdResponse response = clientProxy.acquireLastCorrelationIdResponse()) {
            response.lastCorrelationId().correlationId().copyFrom(lastCorrelationId);
            clientProxy.onLastCorrelationEchoResponse(correlationId, response);
        }
    }

}
