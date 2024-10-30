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

  Scenario: Returns latest 3 Echo messages
  # There shouldn't be any messages yet
    Given the web session Session1 expects the most recent 3 echo responses to be:
      | Message |

    When the web session Session1 sends an echo request RequestA: "Hello"
    And the web session Session1 gets echo response for RequestA: "Hello"

    Then the web session Session1 expects the most recent 3 echo responses to be:
      | Message |
      | Hello   |

    When the web session Session1 sends an echo request RequestB: "World"
    And the web session Session1 gets echo response for RequestB: "World"

    Then the web session Session1 expects the most recent 3 echo responses to be:
      | Message |
      | Hello   |
      | World   |

    When the web session Session1 sends an echo request RequestC: "Foo"
    And the web session Session1 gets echo response for RequestC: "Foo"

    Then the web session Session1 expects the most recent 3 echo responses to be:
      | Message |
      | Hello   |
      | World   |
      | Foo     |

    When the web session Session1 sends an echo request RequestD: "Bar"
    And the web session Session1 gets echo response for RequestD: "Bar"

    Then the web session Session1 expects the most recent 3 echo responses to be:
      | Message |
      | World   |
      | Foo     |
      | Bar     |

    When the web session Session1 sends an echo request RequestE: "Bar"
    And the web session Session1 gets echo response for RequestE: "Bar"

    Then the web session Session1 expects the most recent 3 echo responses to be:
      | Message |
      | Foo   |
      | Bar     |
      | Bar     |

  Scenario: Returns All Echo Requests in a Stream
    When the web session Session1 sends an echo stream RequestA:
      | Message |
      | Hello   |
      | World   |
    Then the web session Session1 gets echo response stream for RequestA:
      | Message |
      | Hello   |
      | World   |

  Scenario: Broadcasts messages to all clients
    Given a connected web session 'Session1'
    And a connected web session 'Session2'

    When the web session Session1 sends a broadcast message "Hello"
    And the web session Session2 sends a broadcast message "World"

    Then Session1 expects to receive broadcast messages:
      | Message |
      | Hello   |
      | World   |
    And Session2 expects to receive broadcast messages:
      | Message |
      | Hello   |
      | World   |
