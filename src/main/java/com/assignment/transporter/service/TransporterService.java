package com.assignment.transporter.service;

import com.assignment.transporter.dto.request.AssignmentRequest;
import com.assignment.transporter.dto.request.InputRequest;
import com.assignment.transporter.dto.response.AssignmentResponse;
import com.assignment.transporter.dto.response.InputResponse;

public interface TransporterService {
	InputResponse saveInput(InputRequest request);
	
	AssignmentResponse assignTransporters(AssignmentRequest request);

}
