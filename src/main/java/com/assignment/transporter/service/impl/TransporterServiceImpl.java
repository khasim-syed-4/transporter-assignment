
package com.assignment.transporter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.transporter.dto.request.AssignmentRequest;
import com.assignment.transporter.dto.request.InputRequest;
import com.assignment.transporter.dto.request.LaneRequest;
import com.assignment.transporter.dto.request.TransporterQuoteRequest;
import com.assignment.transporter.dto.request.TransporterRequest;
import com.assignment.transporter.dto.response.AssignmentDetailResponse;
import com.assignment.transporter.dto.response.AssignmentResponse;
import com.assignment.transporter.dto.response.InputResponse;
import com.assignment.transporter.entity.Lane;
import com.assignment.transporter.entity.Transporter;
import com.assignment.transporter.entity.TransporterQuote;
import com.assignment.transporter.repository.LaneRepository;
import com.assignment.transporter.repository.TransporterQuoteRepository;
import com.assignment.transporter.repository.TransporterRepository;
import com.assignment.transporter.service.TransporterService;

@Service
public class TransporterServiceImpl implements TransporterService {

    private final LaneRepository laneRepository;
    private final TransporterRepository transporterRepository;
    private final TransporterQuoteRepository transporterQuoteRepository;

    public TransporterServiceImpl(
            LaneRepository laneRepository,
            TransporterRepository transporterRepository,
            TransporterQuoteRepository transporterQuoteRepository) {

        this.laneRepository = laneRepository;
        this.transporterRepository = transporterRepository;
        this.transporterQuoteRepository = transporterQuoteRepository;
    }

    @Override
    @Transactional
    public InputResponse saveInput(InputRequest request) {

        // STEP 1: Validate the main request

        if (request == null ||
                request.getLanes() == null ||
                request.getLanes().isEmpty() ||
                request.getTransporters() == null ||
                request.getTransporters().isEmpty()) {

            throw new IllegalArgumentException(
                    "Lanes and transporters are required.");
        }

        // STEP 2: Validate lanes

        Set<Long> laneIds = new HashSet<>();
        List<Lane> lanes = new ArrayList<>();

        for (LaneRequest laneRequest : request.getLanes()) {

            if (laneRequest == null ||
                    laneRequest.getId() == null ||
                    laneRequest.getOrigin() == null ||
                    laneRequest.getOrigin().isBlank() ||
                    laneRequest.getDestination() == null ||
                    laneRequest.getDestination().isBlank()) {

                throw new IllegalArgumentException(
                        "Lane ID, origin and destination are required.");
            }

            Long laneId = laneRequest.getId();

            if (!laneIds.add(laneId)) {
                throw new IllegalArgumentException(
                        "Duplicate lane ID: " + laneId);
            }

            if (laneRepository.existsById(laneId)) {
                throw new IllegalArgumentException(
                        "Lane ID already exists: " + laneId);
            }

            Lane lane = new Lane(
                    laneId,
                    laneRequest.getOrigin().trim(),
                    laneRequest.getDestination().trim()
            );

            lanes.add(lane);
        }

        // STEP 3: Validate transporters

        Set<Long> transporterIds = new HashSet<>();
        List<Transporter> transporters = new ArrayList<>();

        for (TransporterRequest transporterRequest :
                request.getTransporters()) {

            if (transporterRequest == null ||
                    transporterRequest.getId() == null ||
                    transporterRequest.getName() == null ||
                    transporterRequest.getName().isBlank() ||
                    transporterRequest.getLaneQuotes() == null ||
                    transporterRequest.getLaneQuotes().isEmpty()) {

                throw new IllegalArgumentException(
                        "Transporter details and lane quotes are required.");
            }

            Long transporterId = transporterRequest.getId();

            if (!transporterIds.add(transporterId)) {
                throw new IllegalArgumentException(
                        "Duplicate transporter ID: " + transporterId);
            }

            if (transporterRepository.existsById(transporterId)) {
                throw new IllegalArgumentException(
                        "Transporter ID already exists: " + transporterId);
            }

            Transporter transporter = new Transporter(
                    transporterId,
                    transporterRequest.getName().trim()
            );

            transporters.add(transporter);
        }

        // STEP 4: Validate all quotes before saving

        Set<String> quoteKeys = new HashSet<>();

        for (TransporterRequest transporterRequest :
                request.getTransporters()) {

            for (TransporterQuoteRequest quoteRequest :
                    transporterRequest.getLaneQuotes()) {

                if (quoteRequest == null ||
                        quoteRequest.getLaneId() == null ||
                        quoteRequest.getQuote() == null ||
                        quoteRequest.getQuote().signum() <= 0) {

                    throw new IllegalArgumentException(
                            "Lane ID and a positive quote are required.");
                }

                if (!laneIds.contains(quoteRequest.getLaneId())) {
                    throw new IllegalArgumentException(
                            "Invalid lane ID: " + quoteRequest.getLaneId());
                }

                String key = transporterRequest.getId()
                        + "-" + quoteRequest.getLaneId();

                if (!quoteKeys.add(key)) {
                    throw new IllegalArgumentException(
                            "Duplicate quote for transporter "
                            + transporterRequest.getId()
                            + " and lane "
                            + quoteRequest.getLaneId());
                }
            }
        }

        // STEP 5: Save lanes and use the saved objects

        List<Lane> savedLanes = laneRepository.saveAll(lanes);

        Map<Long, Lane> laneMap = new HashMap<>();

        for (Lane lane : savedLanes) {
            laneMap.put(lane.getId(), lane);
        }

        // STEP 6: Save transporters and use the saved objects

        List<Transporter> savedTransporters =
                transporterRepository.saveAll(transporters);

        Map<Long, Transporter> transporterMap = new HashMap<>();

        for (Transporter transporter : savedTransporters) {
            transporterMap.put(transporter.getId(), transporter);
        }

        // STEP 7: Create transporter quote records

        List<TransporterQuote> quotes = new ArrayList<>();

        for (TransporterRequest transporterRequest :
                request.getTransporters()) {

            Transporter transporter =
                    transporterMap.get(transporterRequest.getId());

            for (TransporterQuoteRequest quoteRequest :
                    transporterRequest.getLaneQuotes()) {

                Lane lane = laneMap.get(quoteRequest.getLaneId());

                TransporterQuote quote = new TransporterQuote(
                        lane,
                        transporter,
                        quoteRequest.getQuote()
                );

                quotes.add(quote);
            }
        }

        // STEP 8: Save all quotes

        transporterQuoteRepository.saveAll(quotes);

        // STEP 9: Return success response

        return new InputResponse(
                "success",
                "Input data saved successfully."
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse assignTransporters(
            AssignmentRequest request) {

        // STEP 1: Validate the request
        if (request == null || request.getMaxTransporters() <= 0) {
            throw new IllegalArgumentException(
                    "Maximum transporters must be greater than zero.");
        }

        // STEP 2: Get all lanes and quotes from the database
        List<Lane> lanes = laneRepository.findAll();

        List<TransporterQuote> allQuotes =
                transporterQuoteRepository.findAll();

        if (lanes.isEmpty() || allQuotes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please save lanes and transporter quotes first.");
        }

        // STEP 3: Check that every lane has at least one quote
        for (Lane lane : lanes) {

            boolean hasQuote = allQuotes.stream()
                    .anyMatch(q -> q.getLane().getId().equals(lane.getId()));

            if (!hasQuote) {
                throw new IllegalArgumentException(
                        "No transporter quote available for lane: "
                                + lane.getId());
            }
        }

        // STEP 4: Find the maximum number of transporters allowed
        Set<Long> allTransporterIds = new HashSet<>();

        for (TransporterQuote quote : allQuotes) {
            allTransporterIds.add(quote.getTransporter().getId());
        }

        int maxAllowed = Math.min(
                request.getMaxTransporters(),
                allTransporterIds.size());

        // STEP 5: Start the optimization
        Map<Set<Long>, OptimizationResult> states = new HashMap<>();

        Set<Long> emptySet = new HashSet<>();

        states.put(
                emptySet,
                new OptimizationResult(
                        BigDecimal.ZERO,
                        new ArrayList<>(),
                        emptySet));

        // STEP 6: Process each lane
        for (Lane lane : lanes) {

            Map<Set<Long>, OptimizationResult> nextStates =
                    new HashMap<>();

            for (OptimizationResult current : states.values()) {

                for (TransporterQuote quote : allQuotes) {

                    if (!quote.getLane().getId().equals(lane.getId())) {
                        continue;
                    }

                    Long transporterId =
                            quote.getTransporter().getId();

                    Set<Long> newTransporterIds =
                            new HashSet<>(current.selectedTransporterIds);

                    newTransporterIds.add(transporterId);

                    if (newTransporterIds.size() > maxAllowed) {
                        continue;
                    }

                    List<TransporterQuote> newQuotes =
                            new ArrayList<>(current.selectedQuotes);

                    newQuotes.add(quote);

                    BigDecimal newCost =
                            current.totalCost.add(quote.getQuote());

                    OptimizationResult candidate =
                            new OptimizationResult(
                                    newCost,
                                    newQuotes,
                                    newTransporterIds);

                    OptimizationResult existing =
                            nextStates.get(newTransporterIds);

                    if (existing == null ||
                            newCost.compareTo(existing.totalCost) < 0) {

                        nextStates.put(newTransporterIds, candidate);
                    }
                }
            }

            states = nextStates;
        }

        // STEP 7: Select maximum transporter usage and minimum cost
        OptimizationResult best = null;

        for (OptimizationResult result : states.values()) {

            if (best == null ||
                    result.selectedTransporterIds.size()
                            > best.selectedTransporterIds.size() ||
                    (result.selectedTransporterIds.size()
                            == best.selectedTransporterIds.size()
                            && result.totalCost.compareTo(best.totalCost) < 0)) {

                best = result;
            }
        }

        if (best == null) {
            throw new IllegalArgumentException(
                    "No valid transporter assignment found.");
        }

        // STEP 8: Prepare the response
        List<AssignmentDetailResponse> assignments =
                new ArrayList<>();

        for (TransporterQuote quote : best.selectedQuotes) {

            assignments.add(
                    new AssignmentDetailResponse(
                            quote.getLane().getId(),
                            quote.getTransporter().getId()));
        }

        List<Long> selectedTransporters =
                new ArrayList<>(best.selectedTransporterIds);

        return new AssignmentResponse(
                "success",
                best.totalCost,
                assignments,
                selectedTransporters);
    }
    
    private static class OptimizationResult {

        private BigDecimal totalCost;
        private List<TransporterQuote> selectedQuotes;
        private Set<Long> selectedTransporterIds;

        public OptimizationResult(
                BigDecimal totalCost,
                List<TransporterQuote> selectedQuotes,
                Set<Long> selectedTransporterIds) {

            this.totalCost = totalCost;
            this.selectedQuotes = selectedQuotes;
            this.selectedTransporterIds = selectedTransporterIds;
        }
    }
    
}

