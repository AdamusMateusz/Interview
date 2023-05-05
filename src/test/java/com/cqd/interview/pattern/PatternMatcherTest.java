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
        var firstBestMatch = PatternMatcher.findFirstBestMatch(command);

        assertTrue(firstBestMatch.isMatchFound(), "Match is found");
        assertNotNull(firstBestMatch.matchDescription(), "Match description is not null");

        var match = firstBestMatch.matchDescription();

        assertEquals(expected.position(), match.position(), "Position is found as expected");
        assertEquals(expected.typos(), match.typos(), "Typos found as expected");
    }

    @Test
    void findFirstBestMatchIsReturningResultEvenMatchDoesNotExist() {
        var command = new FindPatternCommand("AAAA", "BBBB");
        var firstBestMatch = PatternMatcher.findFirstBestMatch(command);

        assertFalse(firstBestMatch.isMatchFound(), "Match is not found");
        assertNull(firstBestMatch.matchDescription(), "Match description is null");
    }

    @Test
    void findFirstBestMatchWorksWithBigText() {
        var command = new FindPatternCommand(LongTextProvider.provideLongText(), "Tadeuszu");
        var firstBestMatch = PatternMatcher.findFirstBestMatch(command);

        assertTrue(firstBestMatch.isMatchFound(), "Match is found");
        assertNotNull(firstBestMatch.matchDescription(), "Match description is not null");

        var match = firstBestMatch.matchDescription();

        assertEquals(-1/*???*/, match.position(), "Position is found as expected");
        assertEquals(0, match.typos(), "Typos found as expected");
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
}