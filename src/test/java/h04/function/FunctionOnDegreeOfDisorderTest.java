package h04.function;

import org.junit.jupiter.api.*;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
public class FunctionOnDegreeOfDisorderTest {

    public static Class<?> functionOnDegreeOfDisorderClass;
    public static Field comparator;

    @BeforeAll
    public static void checkClass() {
        assumeTrue(definitionCorrect(ListToIntFunctionTest.class),
                "FunctionOnDegreeOfDisorderTest requires ListToIntFunction to be implemented correctly");

        functionOnDegreeOfDisorderClass = getClassForName("h04.function.FunctionOnDegreeOfDisorder");

        // is public
        assertTrue(isPublic(functionOnDegreeOfDisorderClass.getModifiers()));

        // is generic
        assertEquals(1, functionOnDegreeOfDisorderClass.getTypeParameters().length, "FunctionOnDegreeOfDisorder must be generic");
        assertEquals("T", functionOnDegreeOfDisorderClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // implements ListToIntFunction<T>
        assertTrue(Arrays.stream(functionOnDegreeOfDisorderClass.getGenericInterfaces())
                        .anyMatch(type -> type.getTypeName().equals("h04.function.ListToIntFunction<T>")),
                "FunctionOnDegreeOfDisorder must implement ListToIntFunction<T>");

        // is abstract
        assertTrue(isAbstract(functionOnDegreeOfDisorderClass.getModifiers()), "FunctionOnDegreeOfDisorder must be abstract");

        // constructors
        Constructor<?> constructor = null;

        try {
            constructor = functionOnDegreeOfDisorderClass.getDeclaredConstructor(Comparator.class);
        } catch (NoSuchMethodException e) {
            fail("FunctionOnDegreeOfDisorder is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of FunctionOnDegreeOfDisorder must be public");
        assertEquals("java.util.Comparator<? super T>", constructor.getGenericParameterTypes()[0].getTypeName(),
                "First parameter of constructor of FunctionOnDegreeOfDisorder is not parameterized correctly");

        // fields
        for (Field field : functionOnDegreeOfDisorderClass.getDeclaredFields())
            if (isProtected(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getGenericType().getTypeName().equals("java.util.Comparator<? super T>"))
                comparator = field;

        assertNotNull(comparator, "FunctionOnDegreeOfDisorder has no field matching the criteria specified by the assignment");

        comparator.setAccessible(true);
    }

    @Test
    public void classDefinitionCorrect() {}
}
