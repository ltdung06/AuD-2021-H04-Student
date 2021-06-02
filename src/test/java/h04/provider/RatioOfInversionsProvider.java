package h04.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class RatioOfInversionsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(List.of(0, 1), 0, 1),
                Arguments.of(List.of(1, 0), 1, 1),
                Arguments.of(List.of(1, 2, 3, 4, 5), 0, 10),
                Arguments.of(List.of(5, 4, 3, 2, 1), 10, 10),
                Arguments.of(List.of(1, 2, 5, 4, 3), 3, 10)
        );
    }
}
