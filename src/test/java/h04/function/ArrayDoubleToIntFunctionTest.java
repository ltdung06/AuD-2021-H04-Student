package h04.function;

import org.junit.jupiter.api.*;

import java.lang.reflect.*;
import java.util.Arrays;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SuppressWarnings("RedundantCast")
@DefinitionCheck("checkClass")
public class ArrayDoubleToIntFunctionTest {

    public static Class<?> arrayDoubleToIntFunctionClass;
    public static Constructor<?> constructor;
    public static Field ints;
    public static Method apply;

    private static final int[] GIVEN_INTS = new int[] {10, 4, 5, 1, 8};
    private static Object instance;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(DoubleToIntFunctionTest.class),
                "ArrayDoubleToIntFunctionTest requires DoubleToIntFunction to be implemented correctly");

        arrayDoubleToIntFunctionClass = getClassForName("h04.function.ArrayDoubleToIntFunction");

        // is public
        assertTrue(isPublic(arrayDoubleToIntFunctionClass.getModifiers()));

        // is not generic
        assertEquals(0, arrayDoubleToIntFunctionClass.getTypeParameters().length, "ArrayDoubleToIntFunction must not be generic");

        // implements DoubleToIntFunction
        assertTrue(Arrays.stream(arrayDoubleToIntFunctionClass.getGenericInterfaces())
                         .anyMatch(type -> type.getTypeName().equals("h04.function.DoubleToIntFunction")),
                "ArrayDoubleToIntFunction must implement DoubleToIntFunction");

        // is not abstract
        assertFalse(isAbstract(arrayDoubleToIntFunctionClass.getModifiers()), "ArrayDoubleToIntFunction must not be abstract");

        // constructors
        try {
            constructor = arrayDoubleToIntFunctionClass.getDeclaredConstructor(int[].class);
        } catch (NoSuchMethodException e) {
            fail("ArrayDoubleToIntFunction is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of ArrayDoubleToIntFunction must be public");

        // fields
        for (Field field : arrayDoubleToIntFunctionClass.getDeclaredFields())
            if (isPrivate(field.getModifiers()) && isFinal(field.getModifiers()) && field.getType().equals(int[].class))
                ints = field;

        assertNotNull(ints, "ArrayDoubleToIntFunction has no field matching the criteria specified by the assignment");

        ints.setAccessible(true);

        // methods
        apply = arrayDoubleToIntFunctionClass.getDeclaredMethod("apply", double.class);


        instance = constructor.newInstance((Object) GIVEN_INTS);
    }

    @Test
    public void testIntArray() throws ReflectiveOperationException {
        assertEquals(GIVEN_INTS.length, ((int[]) ints.get(instance)).length,
                "Array in ArrayDoubleToIntFunction does not have same length as given one");
        assertNotSame(GIVEN_INTS, ints.get(instance), "Array objects must not be the same. Values must be copied");
    }

    @Nested
    class ApplyTests {

        @Test
        public void testIllegalArguments() {
            assertThrows(IllegalArgumentException.class, () -> getActualException(apply, instance, -0.1), "-0.1 is out of bounds for apply(double)");
            assertThrows(IllegalArgumentException.class, () -> getActualException(apply, instance, 1.1), "1.1 is out of bounds for apply(double)");
            assertThrows(IllegalArgumentException.class, () -> getActualException(apply, instance, -1.0), "-1.0 is out of bounds for apply(double)");
            assertThrows(IllegalArgumentException.class, () -> getActualException(apply, instance, 2.0), "2.0 is out of bounds for apply(double)");
        }

        @Test
        public void testValidArguments() throws ReflectiveOperationException {
            int[] expectedValues = new int[] {10, 8, 5, 4, 5, 5, 3, 2, 2, 5, 8};

            for (int i = 0; i < expectedValues.length; i++)
                assertEquals(expectedValues[i], apply.invoke(instance, i * 0.1));
        }
    }
}


