package com.dvc.RtLogistics.actuator.health;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Endpoint(id = "test-id")
@Component
public class CustomEndpoint {

    @ReadOperation
    public String test(){
        return ":)";
    }
}
