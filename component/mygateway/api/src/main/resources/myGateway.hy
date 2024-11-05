metadata = {
    "java.package": "org.adaptive.myhydraapp.mygateway"
}

import { MyCluster EchoService } from "classpath:myCluster.hy"
import { UniqueId } from "classpath:platform/common-types.hy"
import { AccessControl } from "classpath:platform/annotations.hy"

type CorrelationIdResponse = {
    correlationId: UniqueId
}

service CorrelationService = {
    @AccessControl(permission: 0)
    correlationEcho(): CorrelationIdResponse
}

web-gateway MyGateway = {
    connectsTo: MyCluster
    services: {
        EchoService
        CorrelationService
    }
}


