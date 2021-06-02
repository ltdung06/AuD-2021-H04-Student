package h04.function;

import h04.provider.RatioOfRunsProvider;
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
public class FunctionOnRatioOfRunsTest {

    public static Class<?> functionOnRatioOfRunsClass;
    public static Constructor<?> constructor;
    public static Field doubleToIntFunction;
    public static Method apply;

    private static Object instance;
    private static Object doubleToIntFunctionProxy;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(FunctionOnDegreeOfDisorderTest.class),
                "FunctionOnRatioOfRunsTest requires FunctionOnDegreeOfDisorder to be implemented correctly");
        assumeTrue(definitionCorrect(DoubleToIntFunctionTest.class),
                "FunctionOnRatioOfRunsTest requires DoubleToIntFunction to be implemented correctly");

        functionOnRatioOfRunsClass = getClassForName("h04.function.FunctionOnRatioOfRuns");

        // is public
        assertTrue(isPublic(functionOnRatioOfRunsClass.getModifiers()));

        // is generic
        assertEquals(1, functionOnRatioOfRunsClass.getTypeParameters().length, "FunctionOnRatioOfRuns must be generic");
        assertEquals("T", functionOnRatioOfRunsClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // extends FunctionOnDegreeOfDisorder<T>
        assertEquals("h04.function.FunctionOnDegreeOfDisorder<T>", functionOnRatioOfRunsClass.getGenericSuperclass().getTypeName(),
                "FunctionOnRatioOfRuns must extend FunctionOnDegreeOfDisorder");

        // is not abstract
        assertFalse(isAbstract(functionOnRatioOfRunsClass.getModifiers()), "FunctionOnRatioOfRuns must not be abstract");

        // constructors
        try {
            constructor = functionOnRatioOfRunsClass.getDeclaredConstructor(DoubleToIntFunctionTest.doubleToIntFunctionClass, Comparator.class);
        } catch (NoSuchMethodException e) {
            fail("FunctionOnRatioOfRuns is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of FunctionOnRatioOfRuns must be public");
        assertEquals("java.util.Comparator<? super T>", constructor.getGenericParameterTypes()[1].getTypeName(),
                "Second parameter of constructor of FunctionOnDegreeOfDisorder is not parameterized correctly");

        // fields
        for (Field field : functionOnRatioOfRunsClass.getDeclaredFields())
            if (isPrivate(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getType().equals(DoubleToIntFunctionTest.doubleToIntFunctionClass))
                doubleToIntFunction = field;

        assertNotNull(doubleToIntFunction, "FunctionOnDegreeOfDisorder has no field matching the criteria specified by the assignment");

        doubleToIntFunction.setAccessible(true);

        // methods
        apply = functionOnRatioOfRunsClass.getDeclaredMethod("apply", List.class);


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
        @ArgumentsSource(RatioOfRunsProvider.class)
        public void testValidArguments(List<Integer> sequence, int numberOfRuns) throws ReflectiveOperationException {
            assertEquals((int) (10.0E8 * numberOfRuns / sequence.size()), apply.invoke(instance, sequence));
        }
    }
}
