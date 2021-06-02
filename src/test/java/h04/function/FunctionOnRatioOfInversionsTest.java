package h04.function;

import h04.provider.RatioOfInversionsProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.reflect.*;
import java.util.Comparator;
import java.util.List;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
public class FunctionOnRatioOfInversionsTest {

    public static Class<?> functionOnRatioOfInversionsClass;
    public static Constructor<?> constructor;
    public static Field doubleToIntFunction;
    public static Method apply;

    private static Object instance;
    private static Object doubleToIntFunctionProxy;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(FunctionOnDegreeOfDisorderTest.class),
                "FunctionOnRatioOfInversionsTest requires FunctionOnDegreeOfDisorder to be implemented correctly");
        assumeTrue(definitionCorrect(DoubleToIntFunctionTest.class),
                "FunctionOnRatioOfInversionsTest requires DoubleToIntFunction to be implemented correctly");

        functionOnRatioOfInversionsClass = getClassForName("h04.function.FunctionOnRatioOfInversions");

        // is public
        assertTrue(isPublic(functionOnRatioOfInversionsClass.getModifiers()));

        // is generic
        assertEquals(1, functionOnRatioOfInversionsClass.getTypeParameters().length, "FunctionOnRatioOfInversions must be generic");
        assertEquals("T", functionOnRatioOfInversionsClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // extends FunctionOnDegreeOfDisorder<T>
        assertEquals("h04.function.FunctionOnDegreeOfDisorder<T>", functionOnRatioOfInversionsClass.getGenericSuperclass().getTypeName(),
                "FunctionOnRatioOfInversions must extend FunctionOnDegreeOfDisorder");

        // is not abstract
        assertFalse(isAbstract(functionOnRatioOfInversionsClass.getModifiers()), "FunctionOnRatioOfInversions must not be abstract");

        // constructors
        try {
            constructor = functionOnRatioOfInversionsClass.getDeclaredConstructor(DoubleToIntFunctionTest.doubleToIntFunctionClass, Comparator.class);
        } catch (NoSuchMethodException e) {
            fail("FunctionOnRatioOfInversions is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of FunctionOnRatioOfInversions must be public");
        assertEquals("java.util.Comparator<? super T>", constructor.getGenericParameterTypes()[1].getTypeName(),
                "Second parameter of constructor of FunctionOnDegreeOfDisorder is not parameterized correctly");

        // fields
        for (Field field : functionOnRatioOfInversionsClass.getDeclaredFields())
            if (isPrivate(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getType().equals(DoubleToIntFunctionTest.doubleToIntFunctionClass))
                doubleToIntFunction = field;

        assertNotNull(doubleToIntFunction, "FunctionOnDegreeOfDisorder has no field matching the criteria specified by the assignment");

        doubleToIntFunction.setAccessible(true);

        // methods
        apply = functionOnRatioOfInversionsClass.getDeclaredMethod("apply", List.class);


        doubleToIntFunctionProxy = doubleToIntFunctionProxy();
        instance = constructor.newInstance(doubleToIntFunctionProxy, Comparator.naturalOrder());
    }

    @Test
    public void testDoubleToIntFunctionField() throws ReflectiveOperationException {
        assertSame(doubleToIntFunctionProxy, doubleToIntFunction.get(instance), "Field of type DoubleToIntFunction is not set correctly");
    }

    @Nested
    class ApplyTests {

        @Test
        public void testIllegalArguments() {
            assertThrows(NullPointerException.class, () -> getActualException(apply, instance, (Object) null));
        }

        @ParameterizedTest
        @ArgumentsSource(RatioOfInversionsProvider.class)
        public void testValidArguments(List<Integer> sequence, int numberOfInversions, int maxNumberOfInversions) throws ReflectiveOperationException {
            assertEquals((int) (10.0E8 * numberOfInversions / maxNumberOfInversions), apply.invoke(instance, sequence));
        }
    }
}
