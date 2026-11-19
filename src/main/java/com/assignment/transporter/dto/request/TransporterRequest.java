
package com.assignment.transporter.dto.request;

import java.util.List;

public class TransporterRequest {

    private Long id;
    private String name;
    private List<TransporterQuoteRequest> laneQuotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TransporterQuoteRequest> getLaneQuotes() {
        return laneQuotes;
    }

    public void setLaneQuotes(List<TransporterQuoteRequest> laneQuotes) {
        this.laneQuotes = laneQuotes;
    }
}
