package com.rocketseat.planner.trip;

import com.rocketseat.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository repository;

    @Autowired
    private ParticipantService participantService;
    //Trip

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
