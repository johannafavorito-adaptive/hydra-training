package org.adaptive.myhydraapp.mycluster.service;

import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import org.adaptive.myhydraapp.mycluster.EchoRecordRepository;
import org.adaptive.myhydraapp.mycluster.codecs.EchoRecordCodec;
import org.adaptive.myhydraapp.mycluster.entities.EchoRequest;
import org.adaptive.myhydraapp.mycluster.entities.MutableEchoRecord;
import org.adaptive.myhydraapp.mycluster.entities.MutableEchoResponse;
import org.adaptive.myhydraapp.mycluster.entities.MutableLastMessage;
import org.adaptive.myhydraapp.mycluster.services.EchoService;
import org.adaptive.myhydraapp.mycluster.services.EchoServiceClientProxy;

public class MyEchoService implements EchoService {

    private final EchoServiceClientProxy clientProxy;
    private final EchoRecordRepository repository;

    public MyEchoService(EchoServiceClientProxy clientProxy, EchoRecordRepository repository) {
        this.clientProxy = clientProxy;
        this.repository = repository;
    }

    @Override
    public void echo(UniqueId correlationId, EchoRequest echoRequest) {
        try (final MutableEchoResponse response = clientProxy.acquireEchoResponse()) {
            try (MutableEchoRecord echo = repository.create()) {
                echo.message(echoRequest.message());
            }
            response.message(echoRequest.message());
            clientProxy.onEchoResponse(correlationId, response);
        }
    }

    @Override
    public void lastMessage(UniqueId correlationId) {
        try (final MutableLastMessage response = clientProxy.acquireLastMessage()) {
            int nextId = repository.count();
            EchoRecordCodec lastMessage = repository.get(nextId);
            response.response().message(lastMessage.message());
            clientProxy.onLastMessageResponse(correlationId, response);
        }
    }

    @Override
    public void last3Messages(UniqueId correlationId) {
        int count = repository.count();
        for (int j = Math.max(count - 2, 1); j <= count; j++) {
            try (final MutableEchoResponse response = clientProxy.acquireEchoResponse()) {
                response.message(repository.get(j).message());
                clientProxy.onLast3MessagesResponse(correlationId, response);
            }
        }
        clientProxy.onLast3MessagesResponseCompleted(correlationId);
    }

    @Override
    public void cancelLast3Messages(UniqueId correlationId) {
    }

}
