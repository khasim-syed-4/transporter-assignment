
package com.assignment.transporter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.transporter.service.TransporterService;
import com.assignment.transporter.dto.request.InputRequest;
import com.assignment.transporter.dto.request.AssignmentRequest;
import com.assignment.transporter.dto.response.InputResponse;
import com.assignment.transporter.dto.response.AssignmentResponse;

@RestController
@RequestMapping("/api/v1/transporters")
public class TransporterController {

    private final TransporterService transporterService;

    public TransporterController(TransporterService transporterService) {
        this.transporterService = transporterService;
    }

    // API 1: Save input data
    @PostMapping("/input")
    public InputResponse saveInput(@RequestBody InputRequest request) {
        return transporterService.saveInput(request);
    }

    // API 2: Assign transporters
    @PostMapping("/assignment")
    public AssignmentResponse assignTransporters(
            @RequestBody AssignmentRequest request) {

        return transporterService.assignTransporters(request);
    }
}
