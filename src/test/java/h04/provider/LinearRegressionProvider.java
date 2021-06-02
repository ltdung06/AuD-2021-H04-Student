package h04.provider;

import h04.Utils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

public class LinearRegressionProvider implements ArgumentsProvider {

    private static final int MAX_STREAM_SIZE = 500;
    private static final int MIN_ARRAY_SIZE = 10;
    private static final int MAX_ARRAY_SIZE = 30;
    private static final double MAX_ABS_Y_INTERSECT = 10;
    private static final double MAX_ABS_SLOPE = 20;
    private static final double NON_NULL_RATIO = 0.5;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.generate(this::generateFunctionAndArray)
                     .limit(MAX_STREAM_SIZE);
    }

    private Arguments generateFunctionAndArray() {
        double m = Utils.RANDOM.nextDouble() * 2 * MAX_ABS_SLOPE - MAX_ABS_SLOPE;
        double b = Utils.RANDOM.nextDouble() * 2 * MAX_ABS_Y_INTERSECT - MAX_ABS_Y_INTERSECT;
        var array = generateArray(x -> m * x + b);

        return Arguments.of(m, b, array);
    }

    private Integer[] generateArray(DoubleUnaryOperator f) {
        var size = Utils.RANDOM.nextInt(MAX_ARRAY_SIZE-MIN_ARRAY_SIZE) + MIN_ARRAY_SIZE;
        var array = new Integer[size];

        fill(f, array, 0);
        fill(f, array, size - 1);
        var tofill = size * NON_NULL_RATIO - 2;

        while (tofill > 0) {
            var index = Utils.RANDOM.nextInt(size);

            if (array[index] == null) {
                fill(f, array, index);
                tofill--;
            }
        }

        return array;
    }

    private void fill(DoubleUnaryOperator f, Integer[] array, int index) {
        var x = index / (array.length - 1.0);
        array[index] = (int) Math.round(f.applyAsDouble(x));
    }

}
