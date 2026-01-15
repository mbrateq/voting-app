package com.mbr.voting.vote;

import com.mbr.voting.election.Election;
import com.mbr.voting.election.ElectionOption;
import com.mbr.voting.election.ElectionRepository;
import com.mbr.voting.exception.BusinessException;
import com.mbr.voting.vote.dto.CastVoteCommand;
import com.mbr.voting.voter.Voter;
import com.mbr.voting.voter.VoterRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CastVote {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;


    public Vote execute(CastVoteCommand castVoteCommand) {
        var voter = prepareVoter(castVoteCommand);
        var election = prepareElection(castVoteCommand.electionId());
        var electionOption = prepareElectionOption(election, castVoteCommand.optionId());

        return voteRepository.save(prepareVote(voter, election, electionOption));
    }

    private ElectionOption prepareElectionOption(Election election, Long optionId) {
        return election.getElectionOptions().stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Election option not found."));
    }

    private Election prepareElection(Long electionId) {
        return electionRepository.findById(electionId)
                .orElseThrow(() -> new ValidationException("Election not found"));
    }

    private Vote prepareVote(Voter voter, Election election, ElectionOption electionOption) {
        var vote = new Vote();
        vote.setVoter(voter);
        vote.setElection(election);
        vote.setElectionOption(electionOption);
        return vote;
    }

    private Voter prepareVoter(CastVoteCommand castVoteCommand) {
        var voter = voterRepository.findById(castVoteCommand.voterId())
                .orElseThrow(() -> new ValidationException("Voter not found"));
        validateVoter(voter, castVoteCommand.electionId());
        return voter;
    }

    private void validateVoter(Voter voter, Long electionId) {
        checkBlocked(voter);
        checkAlreadyBlocked(voter, electionId);
    }

    private void checkAlreadyBlocked(Voter voter, Long electionId) {
        if (voteRepository.existsByVoterIdAndElectionId(
                voter.getId(), electionId)) {
            throw new BusinessException("Voter already voted in this election");
        }
    }

    private static void checkBlocked(Voter voter) {
        if (voter.isBlocked()) {
            throw new BusinessException("Voter is blocked");
        }
    }


}

