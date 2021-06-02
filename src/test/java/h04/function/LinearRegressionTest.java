package h04.function;

import h04.Pair;
import h04.provider.LinearRegressionProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
public class LinearRegressionTest {

    private static final double EPSILON = 2.0;
    private static final double MAX_ALLOWED_AVERAGE_ERROR = 0.3;

    public static Class<?> linearRegressionClass;
    public static Method fitFunction;

    private static Object instance;

    private static final List<Pair<Double, Double>> errors = new ArrayList<>();

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(DoubleToIntFunctionFitterTest.class),
                "LinearRegressionTest requires DoubleToIntFunctionFitter to be implemented correctly");
        assumeTrue(definitionCorrect(LinearDoubleToIntFunctionTest.class),
                "LinearRegressionTest requires LinearDoubleToIntFunction be implemented correctly");

        linearRegressionClass = getClassForName("h04.function.LinearRegression");

        // is public
        assertTrue(isPublic(linearRegressionClass.getModifiers()));

        // is not generic
        assertEquals(0, linearRegressionClass.getTypeParameters().length, "LinearRegression must not be generic");

        // implements DoubleToIntFunctionFitter
        assertEquals(DoubleToIntFunctionFitterTest.doubleToIntFunctionFitterClass, linearRegressionClass.getInterfaces()[0],
                "LinearRegression must implement DoubleToIntFunctionFitter");

        // is not abstract
        assertFalse(isAbstract(linearRegressionClass.getModifiers()), "LinearRegression must not be abstract");

        // methods
        fitFunction = linearRegressionClass.getDeclaredMethod("fitFunction", Integer[].class);

        instance = linearRegressionClass.getDeclaredConstructor().newInstance();
    }

    @ParameterizedTest
    @ArgumentsSource(LinearRegressionProvider.class)
    public void testFitFunction(double slope, double yIntersect, Integer[] array) throws ReflectiveOperationException {
        Object result = fitFunction.invoke(instance, (Object) array);

        assertEquals(LinearDoubleToIntFunctionTest.linearDoubleToIntFunctionClass, result.getClass(),
                "Returned object does not have type LinearDoubleToIntFunction");

        double a = (double) LinearDoubleToIntFunctionTest.a.get(result);
        double b = (double) LinearDoubleToIntFunctionTest.b.get(result);

        errors.add(new Pair<>(Math.abs(a - slope), Math.abs(b - yIntersect)));

        assertEquals(slope, a, EPSILON,
                "Field a of result deviates too much (check relative error)");
        assertEquals(yIntersect, b, EPSILON,
                "Field b of result deviates too much (check relative error)");
    }

    @AfterAll
    static void checkAverageError() {
        var a = errors.stream().collect(Collectors.averagingDouble(Pair::getFirst));

        assertTrue(a < MAX_ALLOWED_AVERAGE_ERROR, "Average error of field a deviates too much");

        var b = errors.stream().collect(Collectors.averagingDouble(Pair::getSecond));

        assertTrue(b < MAX_ALLOWED_AVERAGE_ERROR, "Average error of field b deviates too much");
    }
}
