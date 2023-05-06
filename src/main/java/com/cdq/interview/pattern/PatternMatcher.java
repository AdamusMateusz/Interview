package com.cdq.interview.pattern;

import com.cdq.interview.pattern.model.FindPatternCommand;
import com.cdq.interview.pattern.model.MatchDescription;
import com.cdq.interview.pattern.model.MatchingResult;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.interfaces.MetricStringDistance;

import java.util.stream.IntStream;


public class PatternMatcher {

    private final MetricStringDistance metricStringDistance;
    private final String input;
    private final String pattern;

    public static PatternMatcher fromCommand(FindPatternCommand command) {
        return new PatternMatcher(command.input(), command.pattern());
    }

    private PatternMatcher(String input, String pattern) {
        this.metricStringDistance = new Damerau();
        this.input = input;
        this.pattern = pattern;
    }

    public MatchingResult findFirstBestMatch() {
        IndexWithDistance bestMatch = tryToFindFirstBestMatch();

        if (bestMatch.index() == -1 || bestMatch.distance() == this.pattern.length()) {
            return new MatchingResult(Boolean.FALSE, null);
        }

        MatchDescription description = new MatchDescription(bestMatch.index(), (int) Math.ceil(bestMatch.distance()));
        return new MatchingResult(Boolean.TRUE, description);
    }

    private IndexWithDistance tryToFindFirstBestMatch() {
        int iterations = this.input.length() - (this.pattern.length() - 1);

        return IntStream.range(0, iterations)
                .mapToObj(this::measureSimilarity)
                .reduce(
                        new IndexWithDistance(-1, Double.MAX_VALUE),
                        (curr, next) -> next.distance() < curr.distance() ? next : curr
                );
    }

    private IndexWithDistance measureSimilarity(int offset) {
        String inputPart = this.input.substring(offset, offset + this.pattern.length());
        double distance = metricStringDistance.distance(inputPart, this.pattern);

        return new IndexWithDistance(offset, distance);
    }

    private record IndexWithDistance(int index, double distance) {}

}
