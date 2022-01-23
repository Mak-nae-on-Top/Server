package com.maknaeontop.communication;

import com.maknaeontop.Beacon;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireAlarmServer {
    @PostMapping("/fireAlarmServer")
    public String receive(@RequestBody Beacon[] beaconList){
        return "test";
    }

}
