package com.assignment.transporter.dto.request;

public class AssignmentRequest {
	private int maxTransporters;
	
	public AssignmentRequest() {
	
	}

	public AssignmentRequest(int maxTransporters) {
		super();
		this.maxTransporters = maxTransporters;
	}
	
	public int getMaxTransporters() {
		return maxTransporters;
	}

	public void setMaxTransporters(int maxTransporters) {
		this.maxTransporters = maxTransporters;
	}

	
	

}
