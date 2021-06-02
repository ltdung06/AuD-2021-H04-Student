package h04;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

@DefinitionCheck("checkClass")
public class SortingExperimentTest {

    public static Class<?> sortingExperimentClass;
    public static Method main, computeOptimalThresholds;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        sortingExperimentClass = getClassForName("h04.SortingExperiment");

        // is public
        assertTrue(isPublic(sortingExperimentClass.getModifiers()));

        // is not generic
        assertEquals(0, sortingExperimentClass.getTypeParameters().length, "SortingExperiment must not be generic");

        // is not abstract
        assertFalse(isAbstract(sortingExperimentClass.getModifiers()), "SortingExperiment must not be abstract");

        // methods
        try {
            main = sortingExperimentClass.getDeclaredMethod("main", String[].class);
            computeOptimalThresholds = sortingExperimentClass.getDeclaredMethod("computeOptimalThresholds", int.class, int.class, int.class, double.class);
        } catch (NoSuchMethodException e) {
            fail("SortingExperiment is missing a required method", e);
        }

        assertTrue(isPublic(main.getModifiers()), "Method main must be public");
        assertTrue(isStatic(main.getModifiers()), "Method main must be static");
        assertEquals(void.class, main.getReturnType(), "Return type of method main must be void");

        assertTrue(isPublic(computeOptimalThresholds.getModifiers()), "Method computeOptimalThresholds must be public");
        assertTrue(isStatic(computeOptimalThresholds.getModifiers()), "Method computeOptimalThresholds must be static");
        assertEquals(Integer[][].class, computeOptimalThresholds.getReturnType(),
                "Return type of method computeOptimalThresholds must be Integer[][]");
    }

    @Test
    public void classDefinitionCorrect() {}
}


