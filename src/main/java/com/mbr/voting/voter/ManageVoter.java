package com.mbr.voting.voter;

import com.mbr.voting.voter.dto.ChangeVoterStatusCommand;
import com.mbr.voting.voter.dto.CreateVoterCommand;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManageVoter {
    private final VoterRepository voterRepository;

    public Voter create(CreateVoterCommand createVoterCommand) {
        return voterRepository.save(prepareVoter(createVoterCommand));
    }

    public Voter changeStatus(Long id, ChangeVoterStatusCommand changeVoterStatusCommand) {
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Voter not found"));
        voter.setBlocked(changeVoterStatusCommand.blocked());
        return voterRepository.save(voter);
    }

    private Voter prepareVoter(CreateVoterCommand createVoterCommand) {
        var voter = new Voter();
        voter.setFirstName(createVoterCommand.firstName());
        voter.setLastName(createVoterCommand.lastName());
        return voter;
    }

}
