Feature: Correlation IDs
  Background:
    Given a running my-cluster and my-gateway

  Scenario: Returns the correlation ID in the message body
    When the web session Session1 sends a correlationEcho request RequestA
    Then the web session Session1 gets correlationEcho response for RequestA