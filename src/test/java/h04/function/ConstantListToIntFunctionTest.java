package h04.function;

import org.junit.jupiter.api.*;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
public class ConstantListToIntFunctionTest {

    public static Class<?> constantListToIntFunctionClass;
    public static Constructor<?> constructor;
    public static Field intField;
    public static Method apply;

    private static Object instance;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(ListToIntFunctionTest.class),
                "ConstantListToIntFunctionTest requires ListToIntFunction to be implemented correctly");

        constantListToIntFunctionClass = getClassForName("h04.function.ConstantListToIntFunction");

        // is public
        assertTrue(isPublic(constantListToIntFunctionClass.getModifiers()));

        // is generic
        assertEquals(1, constantListToIntFunctionClass.getTypeParameters().length, "ConstantListToIntFunction must be generic");
        assertEquals("T", constantListToIntFunctionClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // implements ListToIntFunction<T>
        assertTrue(Arrays.stream(constantListToIntFunctionClass.getGenericInterfaces())
                        .anyMatch(type -> type.getTypeName().equals("h04.function.ListToIntFunction<T>")),
                "ConstantListToIntFunction must implement ListToIntFunction<T>");

        // is not abstract
        assertFalse(isAbstract(constantListToIntFunctionClass.getModifiers()), "ConstantListToIntFunction must not be abstract");

        // constructors
        try {
            constructor = constantListToIntFunctionClass.getDeclaredConstructor(int.class);
        } catch (NoSuchMethodException e) {
            fail("ConstantListToIntFunction is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of ConstantListToIntFunction must be public");

        // fields
        for (Field field : constantListToIntFunctionClass.getDeclaredFields())
            if (isPrivate(field.getModifiers()) && isFinal(field.getModifiers()) && field.getType().equals(int.class))
                intField = field;

        assertNotNull(intField, "ConstantListToIntFunction has no field matching the criteria specified by the assignment");

        intField.setAccessible(true);

        // methods
        apply = constantListToIntFunctionClass.getDeclaredMethod("apply", List.class);


        instance = constructor.newInstance(42);
    }

    @Test
    public void testIntField() throws ReflectiveOperationException {
        assertEquals(42, intField.get(instance));
    }

    @Nested
    class ApplyTests {

        @Test
        public void testIllegalArguments() {
            assertThrows(NullPointerException.class, () -> getActualException(apply, instance, (Object) null),
                    "apply(List) must throw a NullPointerException if invoked with null");
        }

        @Test
        public void testValidArguments() throws ReflectiveOperationException {
            assertEquals(intField.get(instance), apply.invoke(instance, List.of()));
            assertEquals(intField.get(instance), apply.invoke(instance, List.of(1, 2, 3, 4, 5)));
        }
    }
}
