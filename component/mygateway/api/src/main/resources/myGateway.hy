metadata = {
    "java.package": "org.adaptive.myhydraapp.mygateway"
}

import { MyCluster EchoService } from "classpath:myCluster.hy"

web-gateway MyGateway = {
    connectsTo: MyCluster
    services: {
        EchoService
    }
}

