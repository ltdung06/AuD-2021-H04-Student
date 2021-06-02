package h04.collection;

import h04.Pair;
import h04.function.ListToIntFunctionTest;
import h04.provider.ListItemProvider;
import h04.provider.RandomListProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static h04.Utils.*;
import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DefinitionCheck("checkClass")
class MyCollectionsTest {

    public static Class<?> myCollectionsClass;
    public static Constructor<?> constructor;
    public static Field function, comparator;
    public static Method sort, adaptiveMergeSortInPlace, selectionSortInPlace;

    private static Object instance;
    private static Object listToIntFunctionProxy, comparatorInstance;

    @BeforeAll
    public static void checkClass() throws ReflectiveOperationException {
        assumeTrue(definitionCorrect(ListToIntFunctionTest.class),
                "MyCollectionsTest requires ListToIntFunction to be implemented correctly");
        assumeTrue(definitionCorrect(ListItemTest.class),
                "MyCollectionsTest requires ListItem to be implemented correctly");

        myCollectionsClass = getClassForName("h04.collection.MyCollections");

        // is public
        assertTrue(isPublic(myCollectionsClass.getModifiers()));

        // is generic
        assertEquals(1, myCollectionsClass.getTypeParameters().length, "MyCollections must be generic");
        assertEquals("T", myCollectionsClass.getTypeParameters()[0].getName(), "Type parameter is not named 'T'");

        // is not abstract
        assertFalse(isAbstract(myCollectionsClass.getModifiers()), "MyCollections must not be abstract");

        // constructors
        try {
            constructor = myCollectionsClass.getDeclaredConstructor(ListToIntFunctionTest.listToIntFunctionClass, Comparator.class);
        } catch (NoSuchMethodException e) {
            fail("MyCollections is missing a required constructor", e);
        }

        assertTrue(isPublic(constructor.getModifiers()), "Constructor of MyCollections must be public");
        assertEquals("h04.function.ListToIntFunction<T>", constructor.getGenericParameterTypes()[0].getTypeName(),
                "First parameter of constructor has incorrect type");
        assertEquals("java.util.Comparator<? super T>", constructor.getGenericParameterTypes()[1].getTypeName(),
                "Second parameter of constructor has incorrect type");

        // fields
        for (Field field : myCollectionsClass.getDeclaredFields())
            if (isPrivate(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getGenericType().getTypeName().equals("h04.function.ListToIntFunction<T>"))
                function = field;
            else if (isPrivate(field.getModifiers()) &&
                    isFinal(field.getModifiers()) &&
                    field.getGenericType().getTypeName().equals("java.util.Comparator<? super T>"))
                comparator = field;

        assertNotNull(function, "MyCollections is missing a required field");
        assertNotNull(comparator, "MyCollections is missing a required field");

        function.setAccessible(true);
        comparator.setAccessible(true);

        // methods
        try {
            sort = myCollectionsClass.getDeclaredMethod("sort", List.class);
            adaptiveMergeSortInPlace = myCollectionsClass.getDeclaredMethod("adaptiveMergeSortInPlace", ListItemTest.listItemClass, int.class);
            selectionSortInPlace = myCollectionsClass.getDeclaredMethod("selectionSortInPlace", ListItemTest.listItemClass);
        } catch (NoSuchMethodException e) {
            fail("MyCollections is missing a required method (some are implemented later)", e);
        }

        assertTrue(isPublic(sort.getModifiers()), "sort(List) must be public");
        assertEquals(void.class, sort.getReturnType(), "sort(List) must have return type void");
        assertEquals("java.util.List<T>", sort.getGenericParameterTypes()[0].getTypeName(),
                "First parameter of sort(List) is not correctly parameterized");

        assertTrue(isPrivate(adaptiveMergeSortInPlace.getModifiers()), "adaptiveMergeSortInPlace(ListItem, int) must be private");
        assertEquals("h04.collection.ListItem<T>", adaptiveMergeSortInPlace.getGenericReturnType().getTypeName(),
                "adaptiveMergeSortInPlace(ListItem, int) must have return type ListItem");
        assertEquals("h04.collection.ListItem<T>", adaptiveMergeSortInPlace.getGenericParameterTypes()[0].getTypeName(),
                "First parameter of adaptiveMergeSortInPlace(ListItem, int) is not correctly parameterized");

        assertTrue(isPrivate(selectionSortInPlace.getModifiers()), "selectionSortInPlace(ListItem) must be private");
        assertEquals("h04.collection.ListItem<T>", selectionSortInPlace.getGenericReturnType().getTypeName(),
                "selectionSortInPlace(ListItem) must have return type ListItem");
        assertEquals("h04.collection.ListItem<T>", selectionSortInPlace.getGenericParameterTypes()[0].getTypeName(),
                "First parameter of selectionSortInPlace(ListItem) is not correctly parameterized");

        adaptiveMergeSortInPlace.setAccessible(true);
        selectionSortInPlace.setAccessible(true);


        listToIntFunctionProxy = listToIntFunctionProxy();
        comparatorInstance = getComparator();
        instance = constructor.newInstance(listToIntFunctionProxy, comparatorInstance);
    }

    @Test
    public void testFields() throws ReflectiveOperationException {
        assertSame(listToIntFunctionProxy, function.get(instance), "Field of type ListToIntFunction<T> is not set correctly");
        assertSame(comparatorInstance, comparator.get(instance), "Field of type Comparator<? super T> is not set correctly");
    }

    @ParameterizedTest
    @ArgumentsSource(RandomListProvider.class)
    public void testSort(List<Pair<Integer, Integer>> sequence) throws ReflectiveOperationException {
        List<Pair<Integer, Integer>> expected = new ArrayList<>(sequence);

        expected.sort(getComparator());
        sort.invoke(instance, sequence);

        assertSequenceEquals(expected, sequence);
        assertSortWasStable(sequence);
    }

    private void assertSortWasStable(List<Pair<Integer, Integer>> sequence) {
        assertTrue(wasStable(sequence), "Sequence sorted correctly but was not stable");
    }

    private boolean wasStable(List<Pair<Integer, Integer>> sequence) {
        return sequence.stream()
                       .collect(Collectors.groupingBy(Pair::getFirst, Collectors.mapping(Pair::getSecond, Collectors.toList())))
                       .values()
                       .stream().allMatch(this::isSortedAscending);
    }

    private boolean isSortedAscending(List<Integer> sequence) {
        for (int i = 0; i < sequence.size() - 1; i++) {
            var a = sequence.get(i);
            var b = sequence.get(i + 1);

            if (a > b)
                return false;
        }

        return true;
    }

    @ParameterizedTest
    @ArgumentsSource(ListItemProvider.class)
    public void testAdaptiveMergeSortInPlace(List<Pair<Integer, Integer>> expected, Object unsortedListItem) throws Throwable {
        assertSort(expected, unsortedListItem, () -> doMergeSort(expected, unsortedListItem));
    }

    private Object doMergeSort(List<Pair<Integer, Integer>> expected, Object unsortedListItem) {
        try {
            return adaptiveMergeSortInPlace.invoke(
                instance, unsortedListItem, whenToUseSelectionSort(expected));
        } catch (Throwable throwable) {
            fail(throwable);
        }

        return null;
    }

    private void assertSort(List<Pair<Integer, Integer>> expected, Object unsortedListItem, Supplier<Object> doSort) throws Throwable {
        List<Object> listItems = new ArrayList<>(expected.size());
        ListItemProvider.listItems(listItems, unsortedListItem);

        List<Pair<Integer, Integer>> actual = new ArrayList<>(expected.size());
        Object sortedListItem = doSort.get();

        ListItemProvider.listFromListItems(actual, sortedListItem);
        expected.sort(getComparator());

        assertSequenceEquals(expected, actual);
        assertSortWasStable(actual);
        assertListItemsAreTheSame(listItems, actual, sortedListItem);
    }

    private int whenToUseSelectionSort(List<Pair<Integer, Integer>> expected) throws Throwable {
        return (int) Proxy.getInvocationHandler(listToIntFunctionProxy)
            .invoke(listToIntFunctionProxy, ListToIntFunctionTest.apply, new Object[]{expected});
    }

    private void assertListItemsAreTheSame(List<Object> listItems, List<Pair<Integer, Integer>> returnedSequence, Object sortedListItem) {
        assertTrue(listItemsAreTheSame(listItems, returnedSequence, sortedListItem),
            "At least one ListItem object has been added. ListItem objects returned by invocation of adaptiveMergeSortInPlace(ListItem, int) " +
                "are not the same created by the provider");
    }

    private boolean listItemsAreTheSame(List<Object> listItems, List<Pair<Integer, Integer>> returnedSequence, Object sortedListItem) {
        return listItems.stream().allMatch(expectedListItem -> {
            List<Object> actualListItems = new ArrayList<>(returnedSequence.size());

            try {
                ListItemProvider.listItems(actualListItems, sortedListItem);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }

            return actualListItems.stream().anyMatch(actualListItem -> actualListItem == expectedListItem);
        });
    }

    private void assertSequenceEquals(List<?> expected, List<?> actual) {
        assertEquals(expected.size(), actual.size(), "Sequences differ in size");

        Iterator<?> eIter = expected.iterator(), aIter = actual.iterator();
        int i = 0;

        while (aIter.hasNext())
            assertEquals(eIter.next(), aIter.next(), "Sequences differ at index " + i++);
    }

    @ParameterizedTest
    @ArgumentsSource(ListItemProvider.class)
    public void testSelectionSortInPlace(List<Pair<Integer, Integer>> expected, Object unsortedListItem) throws Throwable {
        assertSort(expected, unsortedListItem, () -> doSelectionSort(unsortedListItem));
    }

    private Object doSelectionSort(Object unsortedListItem) {
        try {
            return selectionSortInPlace.invoke(instance, unsortedListItem);
        } catch (Throwable throwable) {
            fail(throwable);
        }

        return null;
    }

    private static <T extends Comparable<? super T>> Comparator<Pair<T, ?>> getComparator() {
        return Comparator.comparing(Pair::getFirst);
    }
}
