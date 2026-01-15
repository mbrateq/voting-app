package com.mbr.voting.vote;

import com.mbr.voting.vote.dto.CastVoteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final CastVote castVote;

    @PostMapping
    public ResponseEntity<Vote> cast(@RequestBody CastVoteCommand command) {
        return new ResponseEntity<>(castVote.execute(command), HttpStatus.CREATED);
    }
}
