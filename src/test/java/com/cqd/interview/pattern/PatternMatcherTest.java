package com.cqd.interview.pattern;

import com.cqd.interview.pattern.model.FindPatternCommand;
import com.cqd.interview.pattern.model.MatchDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PatternMatcherTest {

    @ParameterizedTest
    @MethodSource("provideParams")
    void findFirstBestMatchIsReturningResult(FindPatternCommand command, MatchDescription expected) {
        var firstBestMatch = PatternMatcher.fromCommand(command).findFirstBestMatch();

        assertTrue(firstBestMatch.isMatchFound(), "Match is not found");
        assertNotNull(firstBestMatch.matchDescription(), "Match description is null");

        var match = firstBestMatch.matchDescription();

        assertEquals(expected.position(), match.position(), "Position is not found as expected");
        assertEquals(expected.typos(), match.typos(), "Typos not found as expected");
    }

    private static Stream<Arguments> provideParams() {
        return Stream.of(
                Arguments.of(new FindPatternCommand("ABCD", "BCD"), new MatchDescription(1, 0)),
                Arguments.of(new FindPatternCommand("ABCD", "BWD"), new MatchDescription(1, 1)),
                Arguments.of(new FindPatternCommand("ABCDEFG", "CFG"), new MatchDescription(4, 1)),
                Arguments.of(new FindPatternCommand("ABCABC", "ABC"), new MatchDescription(0, 0)),
                Arguments.of(new FindPatternCommand("ABCDEFG", "TDD"), new MatchDescription(1, 2))
        );
    }

    @Test
    void findFirstBestMatchIsReturningResultEvenMatchDoesNotExist() {
        var command = new FindPatternCommand("AAAA", "BBBB");
        var firstBestMatch = PatternMatcher.fromCommand(command).findFirstBestMatch();

        assertFalse(firstBestMatch.isMatchFound(), "Match is found");
        assertNull(firstBestMatch.matchDescription(), "Match description is not null");
    }

    @Test
    void findFirstBestMatchWorksWithBigText() {
        var command = new FindPatternCommand(LongTextProvider.provideLongText(), "Tadeuszu");
        var firstBestMatch = PatternMatcher.fromCommand(command).findFirstBestMatch();

        assertTrue(firstBestMatch.isMatchFound(), "Match is not found");
        assertNotNull(firstBestMatch.matchDescription(), "Match description is null");

        var match = firstBestMatch.matchDescription();

        assertEquals(7801, match.position(), "Position is not found as expected");
        assertEquals(0, match.typos(), "Typos not found as expected");
    }

}