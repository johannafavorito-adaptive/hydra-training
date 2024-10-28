Feature: Echo
  Background:
    Given a running my-cluster and my-gateway
  Scenario: Expect responses to Echo requests
    When my session foo sends an echo request RequestA: "Hello"
    And my session bar sends an echo request RequestB: "Hi"
    And my session mat sends an echo request RequestC: "Yo"
    Then my session foo gets echo response for RequestA: "Hello"
    And my session bar gets echo response for RequestB: "Hi"
    And my session mat gets echo response for RequestC: "Yo"

#  Scenario: Returns NoResponse when there is no latest Echo message
#    Then my session foo expects the most recent echo response to be:
#      | Type        |
#      | NoResponse  |

  Scenario: Returns latest Echo message
    When my session foo sends an echo request RequestA: "Hello"
    And my session foo gets echo response for RequestA: "Hello"
    Then my session foo expects the most recent echo response to be:
      | Type      | Message |
      | Response  | Hello   |

    When my session foo sends an echo request RequestB: "World"
    And my session foo gets echo response for RequestB: "World"
    Then my session foo expects the most recent echo response to be:
      | Type      | Message |
      | Response  | World   |
    And my session bar expects the most recent echo response to be:
      | Type      | Message |
      | Response  | World   |

    When the cluster is snapshotted and the system shutdown
    And the my-cluster and my-gateway components are started
    Then my session foo expects the most recent echo response to be:
      | Type      | Message |
      | Response  | World   |
