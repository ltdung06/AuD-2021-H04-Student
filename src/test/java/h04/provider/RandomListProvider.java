package h04.provider;

import h04.Pair;
import h04.Utils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomListProvider implements ArgumentsProvider {

    private static final int MAX_STREAM_SIZE = 5;
    private static final int LIST_SIZE = 10;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.generate(RandomListProvider::generateRandomList)
                     .map(Arguments::of)
                     .limit(MAX_STREAM_SIZE);
    }

    public static List<Pair<Integer, Object>> generateRandomList() {
        var pairs = Utils.RANDOM.ints(LIST_SIZE, 0, 10)
                                                      .mapToObj(n -> new Pair<>(n, null))
                                                      .collect(Collectors.toList());

        for (int i = 0; i < pairs.size(); i++) {
            pairs.get(i).scd = i;
        }

        return pairs;
    }
}
