package h04.provider;

import h04.collection.ListItemTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ListItemProvider implements ArgumentsProvider {

    private static final int MAX_STREAM_SIZE = 5;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.generate(() -> {
            var list = RandomListProvider.generateRandomList();

            return Arguments.of(list, listItemFromList(list));
        }).limit(MAX_STREAM_SIZE);
    }

    public static <T> Object listItemFromList(List<T> list) {
        try {
            return listItemFromIterator(list.iterator());
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> Object listItemFromIterator(Iterator<T> iterator) throws ReflectiveOperationException {
        if (iterator == null || !iterator.hasNext())
            return null;

        Object listItem = ListItemTest.constructor.newInstance();

        ListItemTest.key.set(listItem, iterator.next());
        ListItemTest.next.set(listItem, listItemFromIterator(iterator));

        return listItem;
    }

    @SuppressWarnings("unchecked")
    public static <T> void listFromListItems(List<T> list, Object listItem) {
        if (listItem == null)
            return;

        try {
            list.add((T) ListItemTest.key.get(listItem));
            listFromListItems(list, ListItemTest.next.get(listItem));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void listItems(List<Object> list, Object listItem) throws ReflectiveOperationException {
        if (listItem == null)
            return;

        list.add(ListItemTest.key.get(listItem));
        listItems(list, ListItemTest.next.get(listItem));
    }
}
