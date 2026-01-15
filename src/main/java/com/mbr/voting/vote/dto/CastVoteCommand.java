package com.mbr.voting.vote.dto;

import jakarta.validation.constraints.NotNull;

public record CastVoteCommand(@NotNull Long voterId, @NotNull Long electionId, @NotNull Long optionId) {
}
