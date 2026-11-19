
package com.assignment.transporter.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class AssignmentResponse {

    private String status;
    private BigDecimal totalCost;
    private List<AssignmentDetailResponse> assignments;
    private List<Long> selectedTransporters;

    public AssignmentResponse() {
    }

    public AssignmentResponse(String status, BigDecimal totalCost,
            List<AssignmentDetailResponse> assignments,
            List<Long> selectedTransporters) {

        this.status = status;
        this.totalCost = totalCost;
        
        this.assignments = assignments;
        this.selectedTransporters = selectedTransporters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public List<AssignmentDetailResponse> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<AssignmentDetailResponse> assignments) {
        this.assignments = assignments;
    }

    public List<Long> getSelectedTransporters() {
        return selectedTransporters;
    }

    public void setSelectedTransporters(List<Long> selectedTransporters) {
        this.selectedTransporters = selectedTransporters;
    }
}
