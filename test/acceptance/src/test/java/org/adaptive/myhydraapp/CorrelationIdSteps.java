package org.adaptive.myhydraapp;

import com.google.common.truth.Truth;
import com.weareadaptive.hydra.cucumber.Expector;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.adaptive.myhydraapp.mygateway.components.ClientToMyGatewayChannel;
import org.adaptive.myhydraapp.mygateway.components.MyGatewaySessionDriver;
import org.adaptive.myhydraapp.mygateway.services.CorrelationServiceProxy;

public class CorrelationIdSteps {
    private final Deployment deployment;
    private final Expector expector;

    public CorrelationIdSteps(Deployment deployment, Expector expector) {
        this.deployment = deployment;
        this.expector = expector;
    }

    @When("the web session {mySession} sends a correlationEcho request {uniqueId}")
    public void theWebSessionSessionSendsACorrelationEchoRequestRequestA(final MyGatewaySessionDriver session, final UniqueId correlationId) {
        final CorrelationServiceProxy correlationServiceProxy = session.services().getCorrelationServiceProxy();
        correlationServiceProxy.correlationEcho(correlationId);
    }

    @Then("the web session {mySession} gets correlationEcho response for {uniqueId}")
    public void theWebSessionSessionGetsCorrelationEchoResponseForRequestA(final MyGatewaySessionDriver session, final UniqueId correlationId) {
//        final ClientToMyGatewayChannel services = session.services();
//
//        expector.expect(services.getCorrelationServiceClientRecorder().correlationEchoResponse())
//                .withCorrelationId(correlationId)
//                .toHaveCompleted()
//                .and()
//                .toBeASingle("expect correlationId", response -> Truth.assertThat(response.correlationId()).isEqualTo(correlationId));
    }

    @Then("the web session {mySession} expects the last correlation ID to be from {uniqueId}")
    public void theWebSessionSessionExpectsTheLastCorrelationIDToBeFromRequestA(final MyGatewaySessionDriver session, final UniqueId correlationId) {
        final CorrelationServiceProxy correlationServiceProxy = session.services().getCorrelationServiceProxy();
        correlationServiceProxy.lastCorrelationEcho(correlationId);

        final ClientToMyGatewayChannel services = session.services();

        expector.expect(services.getCorrelationServiceClientRecorder().lastCorrelationEchoResponse())
                .withCorrelationId(correlationId)
                .toHaveCompleted()
                .and()
                .toBeASingle("expect correlationId", response -> Truth.assertThat(response.asLastCorrelationId().correlationId()).isEqualTo(correlationId));
    }
}
