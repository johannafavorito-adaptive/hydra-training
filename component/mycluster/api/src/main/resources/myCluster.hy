metadata = {
    "java.package": "org.adaptive.myhydraapp.mycluster"
}

import { AccessControl } from "classpath:platform/prelude.hy"

type EchoRequest = {
    message: string
}

type EchoResponse = {
    message: string
}

type LastMessage =
    | NoResponse
    | Response of EchoResponse

service EchoService = {
    @AccessControl(permission: 0)
    echo(EchoRequest): EchoResponse

    @AccessControl(permission: 0)
    lastMessage(): LastMessage
}

cluster MyCluster = {
    services: {
        EchoService
    }
}

table EchoRecord = {
    primary key id: auto int
    message: char[512]
}

database ClusterDatabase = {
    EchoRecord
}
