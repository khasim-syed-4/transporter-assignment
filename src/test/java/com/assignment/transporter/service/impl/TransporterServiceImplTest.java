
package com.assignment.transporter.service.impl;
import com.assignment.transporter.dto.request.InputRequest;
import static org.mockito.Mockito.when;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.assignment.transporter.dto.request.AssignmentRequest;

class TransporterServiceImplTest {

    @Test
    void shouldRejectInvalidAssignmentRequest() {

        TransporterServiceImpl service = new TransporterServiceImpl(
                mock(com.assignment.transporter.repository.LaneRepository.class),
                mock(com.assignment.transporter.repository.TransporterRepository.class),
                mock(com.assignment.transporter.repository.TransporterQuoteRepository.class)
        );

        AssignmentRequest request = new AssignmentRequest();
        request.setMaxTransporters(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.assignTransporters(request)
        );
    }
    

@Test
void shouldRejectAssignmentWhenNoDataIsAvailable() {

    com.assignment.transporter.repository.LaneRepository laneRepo =
            mock(com.assignment.transporter.repository.LaneRepository.class);

    com.assignment.transporter.repository.TransporterRepository transporterRepo =
            mock(com.assignment.transporter.repository.TransporterRepository.class);

    com.assignment.transporter.repository.TransporterQuoteRepository quoteRepo =
            mock(com.assignment.transporter.repository.TransporterQuoteRepository.class);

    when(laneRepo.findAll()).thenReturn(Collections.emptyList());
    when(quoteRepo.findAll()).thenReturn(Collections.emptyList());

    TransporterServiceImpl service =
            new TransporterServiceImpl(laneRepo, transporterRepo, quoteRepo);

    AssignmentRequest request = new AssignmentRequest();
    request.setMaxTransporters(2);

    assertThrows(
            IllegalArgumentException.class,
            () -> service.assignTransporters(request)
    );
}


@Test
void shouldRejectEmptyInputData() {

    TransporterServiceImpl service = new TransporterServiceImpl(
            mock(com.assignment.transporter.repository.LaneRepository.class),
            mock(com.assignment.transporter.repository.TransporterRepository.class),
            mock(com.assignment.transporter.repository.TransporterQuoteRepository.class)
    );

    InputRequest request = new InputRequest();

    assertThrows(
            IllegalArgumentException.class,
            () -> service.saveInput(request)
    );
}


    
}
