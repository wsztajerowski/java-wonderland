package pl.symentis;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class HarrisLinkedListTestUtils {
    public static <T extends Comparable<T>> String toString(HarrisLinkedList<T> list) {
        StringJoiner joiner = new StringJoiner(", ", list.getClass().getSimpleName() + "[", "]");
        Node<T> node = list.getHead();
        while (node != null){
            joiner.add("\n  " + node);
            node = node.getNextNode();
        }
        return joiner
            .toString();
    }

    public static <T extends Comparable<T>> void iterateOverNodes(HarrisLinkedList<T> list, Consumer<Node<T>> consumer) {
        Node<T> current = list.getHead().getNextNode();
        while (current != null) {
            consumer.accept(current);
            current = current.getNextNode();
        }
    }

    public static <T extends Comparable<T>> void markNodesToDelete(HarrisLinkedList<T> list, T ... keys) {
        List<T> keyList = Arrays.stream(keys).toList();
        Node<T> node = list.getHead();
        while (node.hasNext()) {
            Node<T> nextNode = node.getNextNode();
            if (keyList.contains(nextNode.getKey())){
                node.markNextNodeToDeletion();
            }
            node = node.getNextNode();
        }
    }
}
