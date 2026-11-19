
package com.assignment.transporter.dto.request;

import java.util.List;

public class InputRequest {

    private List<LaneRequest> lanes;
    private List<TransporterRequest> transporters;

    public List<LaneRequest> getLanes() {
        return lanes;
    }

    public void setLanes(List<LaneRequest> lanes) {
        this.lanes = lanes;
    }

    public List<TransporterRequest> getTransporters() {
        return transporters;
    }

    public void setTransporters(List<TransporterRequest> transporters) {
        this.transporters = transporters;
    }
}
