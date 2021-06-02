package h04.function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkInterface")
public class DoubleToIntFunctionFitterTest {

    public static Class<?> doubleToIntFunctionFitterClass;
    public static Method fitFunction;

    @BeforeAll
    public static void checkInterface() {
        assumeTrue(definitionCorrect(DoubleToIntFunctionTest.class),
                "DoubleToIntFunctionFitterTest requires DoubleToIntFunction to be implemented correctly");
        
        doubleToIntFunctionFitterClass = getClassForName("h04.function.DoubleToIntFunctionFitter");

        // is public
        assertTrue(isPublic(doubleToIntFunctionFitterClass.getModifiers()));

        // is interface
        assertTrue(doubleToIntFunctionFitterClass.isInterface(), "DoubleToIntFunctionFitter must be an interface");

        // is not generic
        assertEquals(0, doubleToIntFunctionFitterClass.getTypeParameters().length, "DoubleToIntFunctionFitter must not be generic");

        // methods
        try {
            fitFunction = doubleToIntFunctionFitterClass.getDeclaredMethod("fitFunction", Integer[].class);

            assertTrue(isPublic(fitFunction.getModifiers()), "Method fitFunction(Integer[]) must be public (implicit or explicit)");
            assertFalse(isStatic(fitFunction.getModifiers()), "Method fitFunction(Integer[]) must not be static");
            assertFalse(fitFunction.isDefault(), "Method fitFunction(Integer[]) must not be default");
            assertEquals(DoubleToIntFunctionTest.doubleToIntFunctionClass, fitFunction.getReturnType(),
                    "Method fitFunction(Integer[]) does not have correct return type");
        } catch (NoSuchMethodException e) {
            fail("Interface DoubleToIntFunctionFitter is missing a required method", e);
        }
    }

    @Test
    public void interfaceDefinitionCorrect() {}
}
