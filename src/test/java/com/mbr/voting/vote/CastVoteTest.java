package com.mbr.voting.vote;

import com.mbr.voting.election.Election;
import com.mbr.voting.election.ElectionOption;
import com.mbr.voting.election.ElectionRepository;
import com.mbr.voting.exception.BusinessException;
import com.mbr.voting.vote.dto.CastVoteCommand;
import com.mbr.voting.voter.Voter;
import com.mbr.voting.voter.VoterRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CastVoteTest {


    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoterRepository voterRepository;

    @Mock
    private ElectionRepository electionRepository;

    @InjectMocks
    private CastVote castVote;

    @Test
    void shouldCastVoteSuccessfully() {
        // given
        var castVoteCommand = new CastVoteCommand(1L, 10L, 100L);

        var voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        var option = new ElectionOption();
        option.setId(100L);

        var election = new Election();
        election.setId(10L);
        election.setElectionOptions(List.of(option));

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(electionRepository.findById(10L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(false);
        when(voteRepository.save(any(Vote.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        var result = castVote.execute(castVoteCommand);

        // then
        assertThat(result.getVoter()).isEqualTo(voter);
        assertThat(result.getElection()).isEqualTo(election);
        assertThat(result.getElectionOption()).isEqualTo(option);

        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    void shouldThrowExceptionWhenVoterNotFound() {
        // given
        var command = new CastVoteCommand(1L, 10L, 100L);

        when(voterRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> castVote.execute(command))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Voter not found");

        verifyNoInteractions(electionRepository, voteRepository);
    }

    @Test
    void shouldThrowExceptionWhenVoterIsBlocked() {
        // given
        var command = new CastVoteCommand(1L, 10L, 100L);

        var voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(true);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));

        // when & then
        assertThatThrownBy(() -> castVote.execute(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Voter is blocked");

        verifyNoInteractions(electionRepository, voteRepository);
    }

    @Test
    void shouldThrowExceptionWhenVoterAlreadyVotedInElection() {
        // given
        var command = new CastVoteCommand(1L, 10L, 100L);

        var voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> castVote.execute(command))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Voter already voted in this election");

        verifyNoInteractions(electionRepository);
        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenElectionNotFound() {
        // given
        var command = new CastVoteCommand(1L, 10L, 100L);

        var voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(false);
        when(electionRepository.findById(10L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> castVote.execute(command))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Election not found");

        verify(voteRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenElectionOptionNotFound() {
        // given
        var command = new CastVoteCommand(1L, 10L, 100L);

        var voter = new Voter();
        voter.setId(1L);
        voter.setBlocked(false);

        var election = new Election();
        election.setId(10L);
        election.setElectionOptions(List.of()); // brak opcji

        when(voterRepository.findById(1L)).thenReturn(Optional.of(voter));
        when(voteRepository.existsByVoterIdAndElectionId(1L, 10L)).thenReturn(false);
        when(electionRepository.findById(10L)).thenReturn(Optional.of(election));

        // when & then
        assertThatThrownBy(() -> castVote.execute(command))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Election option not found.");

        verify(voteRepository, never()).save(any());
    }
}

