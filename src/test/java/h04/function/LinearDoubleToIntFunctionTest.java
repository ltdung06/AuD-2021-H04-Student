package h04.function;

import org.junit.jupiter.api.*;

import java.lang.reflect.*;
import java.util.Arrays;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
public class LinearDoubleToIntFunctionTest {

    public static Class<?> linearDoubleToIntFunctionClass;
    public static Constructor<?> constructor;
    public static Field a, b;
    public static Method apply;

    private static Object instance;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(DoubleToIntFunctionTest.class),
                "LinearDoubleToIntFunctionTest requires DoubleToIntFunction to be implemented correctly");

        linearDoubleToIntFunctionClass = getClassForName("h04.function.LinearDoubleToIntFunction");

        // is public
        assertTrue(isPublic(linearDoubleToIntFunctionClass.getModifiers()));

        // is not generic
        assertEquals(0, linearDoubleToIntFunctionClass.getTypeParameters().length, "LinearDoubleToIntFunction must not be generic");

        // implements DoubleToIntFunction
        assertTrue(Arrays.stream(linearDoubleToIntFunctionClass.getGenericInterfaces())
                        .anyMatch(type -> type.getTypeName().equals("h04.function.DoubleToIntFunction")),
                "LinearDoubleToIntFunction must implement DoubleToIntFunction");

        // is not abstract
        assertFalse(isAbstract(linearDoubleToIntFunctionClass.getModifiers()), "LinearDoubleToIntFunction must not be abstract");

        // constructors
        try {
            constructor = linearDoubleToIntFunctionClass.getDeclaredConstructor(double.class, double.class);
        } catch (NoSuchMethodException e) {
            fail("LinearDoubleToIntFunction is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of LinearDoubleToIntFunction must be public");

        // fields
        try {
            a = linearDoubleToIntFunctionClass.getDeclaredField("a");
            b = linearDoubleToIntFunctionClass.getDeclaredField("b");
        } catch (NoSuchFieldException e) {
            fail("LinearDoubleToIntFunction is missing one or more required fields", e);
        }

        assertFalse(isStatic(a.getModifiers()), "Field a in LinearDoubleToIntFunction must not be static");
        assertTrue(isFinal(a.getModifiers()), "Field a in LinearDoubleToIntFunction must be final");
        assertEquals(double.class, a.getType(), "Field a in LinearDoubleToIntFunction has wrong type");

        assertFalse(isStatic(b.getModifiers()), "Field b in LinearDoubleToIntFunction must not be static");
        assertTrue(isFinal(b.getModifiers()), "Field b in LinearDoubleToIntFunction must be final");
        assertEquals(double.class, b.getType(), "Field b in LinearDoubleToIntFunction has wrong type");

        a.setAccessible(true);
        b.setAccessible(true);

        // methods
        apply = linearDoubleToIntFunctionClass.getDeclaredMethod("apply", double.class);


        instance = constructor.newInstance(4.0, 2.0);
    }

    @Test
    public void testFields() throws ReflectiveOperationException {
        assertEquals(4.0, a.get(instance));
        assertEquals(2.0, b.get(instance));
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
            int[] expectedValues = new int[] {2, 2, 3, 3, 4, 4, 4, 5, 5, 6, 6};

            for (int i = 0; i < expectedValues.length; i++)
                assertEquals(expectedValues[i], apply.invoke(instance, i * 0.1));
        }
    }
}
