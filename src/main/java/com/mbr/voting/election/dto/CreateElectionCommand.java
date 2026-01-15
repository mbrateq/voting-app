package com.mbr.voting.election.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateElectionCommand(
        @NotBlank String name,
        @Size(min = 2) List<@NotBlank String> options
) {
}
