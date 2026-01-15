package com.mbr.voting.vote;

import com.mbr.voting.election.Election;
import com.mbr.voting.election.ElectionOption;
import com.mbr.voting.voter.Voter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "election_id"})
)
public class Vote {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Voter voter;

    @ManyToOne
    private Election election;

    @ManyToOne
    private ElectionOption electionOption;
}
