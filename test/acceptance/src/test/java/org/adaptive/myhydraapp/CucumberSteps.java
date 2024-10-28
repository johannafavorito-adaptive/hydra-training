package org.adaptive.myhydraapp;

import com.google.common.truth.Truth;
import com.weareadaptive.hydra.cucumber.Expector;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.adaptive.myhydraapp.mycluster.entities.MutableEchoRequest;
import org.adaptive.myhydraapp.mycluster.services.EchoServiceProxy;
import org.adaptive.myhydraapp.mygateway.components.ClientToMyGatewayChannel;
import org.adaptive.myhydraapp.mygateway.components.MyGatewaySessionDriver;

import java.util.List;

public class CucumberSteps
{
    private final Deployment deployment;
    private final Expector expector;

    public CucumberSteps(final Deployment deployment, final Expector expector)
    {
        this.deployment = deployment;
        this.expector = expector;
    }

    @When("my session {mySession} sends an echo request {uniqueId}: {string}")
    public void the_web_session_sends_an_echo_request_request_a(final MyGatewaySessionDriver session, final UniqueId correlationId, final String message)
    {
        final EchoServiceProxy echoServiceProxy = session.services().getEchoServiceProxy();
        try (final MutableEchoRequest request = echoServiceProxy.acquireEchoRequest())
        {
            request.message(message);
            echoServiceProxy.echo(correlationId, request);
        }
    }

    @And("my session {mySession} gets echo response for {uniqueId}: {string}")
    public void the_web_session_gets_echo_response_for_request_a(final MyGatewaySessionDriver session, final UniqueId correlationId, final String message)
    {
        final ClientToMyGatewayChannel services = session.services();

        expector.expect(services.getEchoServiceClientRecorder().echoResponse())
            .withCorrelationId(correlationId)
            .toHaveCompleted()
            .and()
            .toBeASingle("expect echo response", response -> Truth.assertThat(response.message()).isEqualTo(message));
    }

    @Then("my session {mySession} expects the most recent echo response to be:")
    public void my_session_expects_the_most_recent_echo_response_to_be(final MyGatewaySessionDriver session, final DataTable expectedResponse) {
        final EchoServiceProxy echoServiceProxy = session.services().getEchoServiceProxy();
        UniqueId correlationId = echoServiceProxy.allocateCorrelationId();
        echoServiceProxy.lastMessage(correlationId);

        final ClientToMyGatewayChannel services = session.services();

        expector.expect(services.getEchoServiceClientRecorder().lastMessageResponse())
                .withCorrelationId(correlationId)
                .toHaveCompleted()
                .toContainExactly(expectedResponse);
    }

    @When("the {componentNames} component is started")
    @When("the {componentNames} components are started")
    @Given("a running {componentNames}")
    public void startComponents(final List<String> componentNames)
    {
        componentNames.forEach(componentName ->
        {
            switch (componentName)
            {
                case "my-cluster":
                    deployment.getMyCluster();
                    break;

                case "my-gateway":
                    deployment.getMyGateway();
                    break;

                default:
                    throw new IllegalArgumentException("Unknown component '" + componentName + "'");
            }
        });
    }

    @When("the cluster is snapshotted and the system shutdown")
    public void snapshotAndShutdown()
    {
        deployment.snapshotAndShutdown();
    }

    @ParameterType("[a-zA-Z0-9_-]+")
    public MyGatewaySessionDriver mySession(final String sessionName)
    {
        final MyGatewaySessionDriver session = deployment.getMyGateway().session(sessionName);
        if (!session.isConnected())
        {
            session.connect();
        }
        return session;
    }

}
