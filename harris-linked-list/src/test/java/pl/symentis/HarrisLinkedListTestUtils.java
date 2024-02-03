package pl.symentis;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicMarkableReference;

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
}
