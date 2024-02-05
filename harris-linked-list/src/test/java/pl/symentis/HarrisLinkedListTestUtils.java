package pl.symentis;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.function.Consumer;

public class HarrisLinkedListTestUtils {
    public static <T extends Comparable<T>> String toString(HarrisLinkedList<T> list) {
        StringJoiner joiner = new StringJoiner(", ", HarrisLinkedList.class.getSimpleName() + "[", "]");
        AtomicMarkableReference<Node<T>> nodeRef = list.getHead();
        while (nodeRef.getReference() != null){
            joiner.add("\n  " + nodeRef.getReference().toString());
            nodeRef = nodeRef.getReference().getNextNodeMarkableReference();
        }
        return joiner
            .toString();
    }

    public static <T extends Comparable<T>> void iterateOverNodes(HarrisLinkedList<T> list, Consumer<Node<T>> consumer) {
        Node<T> current = list.getHead().getReference();
        while (current != null) {
            consumer.accept(current);
            current = current.getNextNodeMarkableReference().getReference();
        }
    }

    public static <T extends Comparable<T>> void iterateOverNodeReferences(HarrisLinkedList<T> list, Consumer<AtomicMarkableReference<Node<T>>> consumer) {
        AtomicMarkableReference<Node<T>> current = list.getHead();
        while (current != null) {
            consumer.accept(current);
            if(current.getReference() != null) {
                current = current.getReference().getNextNodeMarkableReference();
            } else {
                current =  null;
            }
        }
    }

    public static <T extends Comparable<T>> void markNodesToDelete(HarrisLinkedList<T> list, T ... keys) {
        List<T> keyList = Arrays.stream(keys).toList();
        AtomicMarkableReference<Node<T>> current = list.getHead();
        Node<T> nextNode = current.getReference();
        while (nextNode != null) {
            if (keyList.contains(nextNode.getKey())){
                current.set(nextNode, true);
            }
            current = nextNode.getNextNodeMarkableReference();
            nextNode = current.getReference();
        }
    }
}
