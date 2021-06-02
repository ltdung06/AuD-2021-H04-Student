package h04.provider;

import h04.Utils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RatioOfRunsProvider implements ArgumentsProvider {

    private static final int MAX_STREAM_SIZE = 5;
    private static final int NUMBER_OF_RUNS = 5;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.generate(() -> {
            List<Integer> sequence = IntStream.range(0, NUMBER_OF_RUNS)
                                              .boxed()
                                              .flatMap(integer -> Stream.concat(
                                                      Stream.of(0),
                                                      Utils.RANDOM.ints(1, 10)
                                                                  .distinct()
                                                                  .limit(Utils.RANDOM.nextInt(9) + 1)
                                                                  .sorted()
                                                                  .boxed()))
                                              .collect(Collectors.toUnmodifiableList());

            return Arguments.of(sequence, NUMBER_OF_RUNS);
        }).limit(MAX_STREAM_SIZE);
    }
}
