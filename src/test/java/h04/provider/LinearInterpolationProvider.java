package h04.provider;

import h04.Utils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LinearInterpolationProvider implements ArgumentsProvider {

    private static final int MAX_STREAM_SIZE = 500;
    private static final int MIN_ARRAY_SIZE = 10;
    private static final int MAX_ARRAY_SIZE = 30;
    private static final int MAX_ABS_Y_INTERSECT = 10;
    private static final int MAX_ABS_SLOPE = 20;
    private static final double NON_NULL_RATIO = 0.5;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.generate(this::generateArrayAndArrayWithNulls).limit(MAX_STREAM_SIZE);
    }

    private Arguments generateArrayAndArrayWithNulls() {
        var array = generateArray();
        Integer[] arrayWithNulls = getIntegers(array);

        fillNulls(arrayWithNulls);

        return Arguments.of(array, arrayWithNulls);
    }

    private Integer[] getIntegers(int[] array) {
        return Arrays.stream(array).boxed().toArray(Integer[]::new);
    }

    private void fillNulls(Integer[] array) {
        var nulls = array.length * (1 -  NON_NULL_RATIO);

        while (nulls > 0) {
            var index = Utils.RANDOM.nextInt(array.length - 2) + 1;

            if (array[index] != null) {
                array[index] = null;
                nulls--;
            }
        }
    }

    private int[] generateArray() {
        var size = Utils.RANDOM.nextInt(MAX_ARRAY_SIZE-MIN_ARRAY_SIZE) + MIN_ARRAY_SIZE;
        var m = Utils.RANDOM.nextInt(2 * MAX_ABS_SLOPE) - MAX_ABS_SLOPE;
        var b = Utils.RANDOM.nextInt(2 * MAX_ABS_Y_INTERSECT) - MAX_ABS_Y_INTERSECT;

        return IntStream.iterate(b, i -> i + m).limit(size).toArray();
    }
}
