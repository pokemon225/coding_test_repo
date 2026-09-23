package com.colorvotes;

/** Total votes cast for a single color, summed across every city. */
public record ColorVotes(long colorId, String color, long votes) {
}
