package h04.collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

@DefinitionCheck("checkClass")
public class ListItemTest {

    public static Class<?> listItemClass;
    public static Constructor<?> constructor;
    public static Field key, next;

    @BeforeAll
    public static void checkClass() {
        listItemClass = getClassForName("h04.collection.ListItem");

        // is generic
        assertEquals(1, listItemClass.getTypeParameters().length, "ListItem must be generic");
        assertEquals("T", listItemClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // is not abstract
        assertFalse(isAbstract(listItemClass.getModifiers()), "ListItem must not be abstract");

        // constructors
        try {
            constructor = listItemClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("ListItem must have a parameterless constructor (minimal implementation)", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of ListItem must be public");

        // fields
        try {
            key = listItemClass.getDeclaredField("key");
            next = listItemClass.getDeclaredField("next");
        } catch (NoSuchFieldException e) {
            fail("ListItem is missing one or more required fields", e);
        }

        key.setAccessible(true);
        next.setAccessible(true);

        assertEquals("T", key.getGenericType().getTypeName(), "Type of field \"key\" is incorrect");
        assertEquals("h04.collection.ListItem<T>", next.getGenericType().getTypeName(), "Type of field \"next\" is incorrect");
        assertEquals(2, listItemClass.getDeclaredFields().length, "ListItem may only have two fields (minimal implementation)");

        // methods
        assertEquals(0, listItemClass.getDeclaredMethods().length, "ListItem may not declare any methods (minimal implementation)");
    }

    @Test
    public void classDefinitionCorrect() {}
}
