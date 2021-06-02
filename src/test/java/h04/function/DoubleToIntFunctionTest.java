package h04.function;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

@DefinitionCheck("checkInterface")
public class DoubleToIntFunctionTest {

    public static Class<?> doubleToIntFunctionClass;
    public static Method apply;

    @BeforeAll
    public static void checkInterface() {
        doubleToIntFunctionClass = getClassForName("h04.function.DoubleToIntFunction");

        // is public
        assertTrue(isPublic(doubleToIntFunctionClass.getModifiers()));

        // is interface
        assertTrue(doubleToIntFunctionClass.isInterface(), "DoubleToIntFunction must be an interface");

        // is not generic
        assertEquals(0, doubleToIntFunctionClass.getTypeParameters().length, "DoubleToIntFunction must not be generic");

        // methods
        try {
            apply = doubleToIntFunctionClass.getDeclaredMethod("apply", double.class);

            assertTrue(isPublic(apply.getModifiers()), "Method apply(double) must be public (implicit or explicit)");
            assertFalse(isStatic(apply.getModifiers()), "Method apply(double) must not be static");
            assertFalse(apply.isDefault(), "Method apply(double) must not be default");
            assertEquals(int.class, apply.getReturnType(), "Method apply(double) does not have correct return type");
            assertTrue(apply.getExceptionTypes().length == 0 || apply.getExceptionTypes().length == 1,
                    "Method apply(double) must throw at most one exception");

            if (apply.getExceptionTypes().length == 1)
                assertEquals(IllegalArgumentException.class, apply.getExceptionTypes()[0],
                        "Method apply(double) must throw IllegalArgumentException");
        } catch (NoSuchMethodException e) {
            fail("Interface DoubleToIntFunction is missing a required method", e);
        }
    }

    @Test
    public void interfaceDefinitionCorrect() {}
}
