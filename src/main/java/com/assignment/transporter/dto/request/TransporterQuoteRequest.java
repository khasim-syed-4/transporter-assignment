
package com.assignment.transporter.dto.request;

import java.math.BigDecimal;

public class TransporterQuoteRequest {

    private Long laneId;
    private BigDecimal quote;

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public BigDecimal getQuote() {
        return quote;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }
}
