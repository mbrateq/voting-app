package com.mbr.voting.voter;

import com.mbr.voting.voter.dto.ChangeVoterStatusCommand;
import com.mbr.voting.voter.dto.CreateVoterCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/voters")
@RequiredArgsConstructor
public class VoterController {

    private final ManageVoter manageVoter;

    @PostMapping
    public ResponseEntity<Voter> create(@Valid @RequestBody CreateVoterCommand createVoterCommand) {
        return new ResponseEntity<>(manageVoter.create(createVoterCommand), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Voter> changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeVoterStatusCommand changeVoterStatusCommand) {
        return new ResponseEntity<>(manageVoter.changeStatus(id, changeVoterStatusCommand), HttpStatus.OK);
    }

}
