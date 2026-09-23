package com.colorvotes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ColorRepositoryTest {

    @Autowired
    ColorRepository colors;

    @Test
    void findAllReturnsEveryColorInSpectrumOrder() {
        assertThat(colors.findAll())
                .extracting(Color::name)
                .containsExactly("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet");
    }

    @Test
    void findVotesSumsAcrossCities() {
        assertThat(votesFor("Red")).isEqualTo(260_000);
        assertThat(votesFor("Blue")).isEqualTo(260_000);
        assertThat(votesFor("Yellow")).isEqualTo(30_000);
        assertThat(votesFor("Violet")).isEqualTo(5_000);
    }

    @Test
    void findVotesReturnsZeroForColorWithNoVotes() {
        assertThat(votesFor("Orange")).isZero();
        assertThat(votesFor("Green")).isZero();
        assertThat(votesFor("Indigo")).isZero();
    }

    @Test
    void findVotesReturnsEmptyForUnknownColor() {
        assertThat(colors.findVotes(999_999)).isEmpty();
    }

    private long votesFor(String name) {
        Color color = colors.findAll().stream()
                .filter(c -> c.name().equals(name))
                .findFirst()
                .orElseThrow();
        return colors.findVotes(color.id()).orElseThrow().votes();
    }
}
