package com.mbr.voting.election;

import com.mbr.voting.election.dto.CreateElectionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateElection {
    private final ElectionRepository electionRepository;

    public Election execute(CreateElectionCommand createElectionCommand) {
        return electionRepository.save(prepareElection(createElectionCommand));
    }

    private Election prepareElection(CreateElectionCommand createElectionCommand) {
        var election = new Election();
        election.setName(createElectionCommand.name());
        election.setElectionOptions(createElectionCommand.options().stream().map(name -> new ElectionOption(name, election)).toList());
        return election;
    }

}

