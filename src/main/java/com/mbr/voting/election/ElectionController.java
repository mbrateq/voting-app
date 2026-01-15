package com.mbr.voting.election;

import com.mbr.voting.election.dto.CreateElectionCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final CreateElection createElection;

    @PostMapping
    public ResponseEntity<Election> create(@Valid @RequestBody CreateElectionCommand createElectionCommand) {
        return new ResponseEntity<>(createElection.execute(createElectionCommand), HttpStatus.CREATED);
    }

}
