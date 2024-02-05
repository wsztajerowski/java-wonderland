package pl.symentis;

import org.assertj.core.api.AbstractAssert;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class HarrisLinkedListAssert<T extends Comparable<T>> extends AbstractAssert<HarrisLinkedListAssert<T>, HarrisLinkedList<T>> {
    HarrisLinkedListAssert(HarrisLinkedList<T> actual) {
        super(actual, HarrisLinkedListAssert.class);
    }

    public static <A extends Comparable<A>> HarrisLinkedListAssert<A> assertThat(HarrisLinkedList<A> node) {
        return new HarrisLinkedListAssert<>(node);
    }

    public HarrisLinkedListAssert<T> isEmpty() {
        isNotNull();
        Node<T> reference = actual.getHead().getReference();
        if (reference != null){
            failWithMessage("Expected list: \n%s\nto be empty!", HarrisLinkedListTestUtils.toString(actual));
        }
        return this;
    }

    public HarrisLinkedListAssert<T> hasSize(int expectedSize) {
        isNotNull();
        int nodeCounter = 0;
        AtomicMarkableReference<Node<T>> nodeRef = actual.getHead();
        while (nodeRef.getReference() != null){
            nodeCounter++;
            nodeRef = nodeRef.getReference().getNextNodeMarkableReference();
        }
        if (nodeCounter != expectedSize) {
            failWithMessage("Expected list: \n%s\nto have size <%s>, but was <%s>", HarrisLinkedListTestUtils.toString(actual), expectedSize, nodeCounter);
        }
        return this;
    }

    public NodeAssert<T> extractFirstNode() {
        isNotNull();
        return new NodeAssert<>(actual.getHead().getReference());
    }

    public HarrisLinkedListAssert<T> isSorted() {
        isNotNull();
        boolean isSorted = true;
        Node<T> current = actual.getHead().getReference();
        while (current != null && current.getNextNodeMarkableReference().getReference() != null) {
            Node<T> next = current.getNextNodeMarkableReference().getReference();
            if (current.hasKeyEqualOrGreaterThan(next.getKey())) {
                isSorted = false;
                break;
            }
            current = next;
        }
        if (!isSorted) {
            failWithMessage("Expected list: \n%s\nto have nodes ordered by keys, but node: \n%s\nhave smaller key than its successor: \n%s", HarrisLinkedListTestUtils.toString(actual), current, current.getNextNodeMarkableReference().getReference());
        }
        return this;
    }

    public HarrisLinkedListAssert<T> containsKey(T expectedKey) {
        isNotNull();
        AtomicBoolean foundKey = new AtomicBoolean(false);
        HarrisLinkedListTestUtils.iterateOverNodes(actual, node -> {
            if (node.getKey().equals(expectedKey)){
                foundKey.set(true);
            }
        });
        if (!foundKey.get()) {
            failWithMessage("Expected list: \n%s\nto contains node with key: <%s>", HarrisLinkedListTestUtils.toString(actual), expectedKey);
        }
        return this;
    }

    public HarrisLinkedListAssert<T> doesNotContainKey(T expectedKey) {
        isNotNull();
        AtomicBoolean foundKey = new AtomicBoolean(false);
        HarrisLinkedListTestUtils.iterateOverNodes(actual, node -> {
            if (node.getKey().equals(expectedKey)){
                foundKey.set(true);
            }
        });
        if (foundKey.get()) {
            failWithMessage("Expected list: \n%s\nto DOES NOT contain node with key: <%s>",
                HarrisLinkedListTestUtils.toString(actual),
                expectedKey);
        }
        return this;
    }

    public HarrisLinkedListAssert<T> doesNotContainMarkedNodes() {
        isNotNull();
        AtomicBoolean markedNode = new AtomicBoolean(false);
        HarrisLinkedListTestUtils.iterateOverNodeReferences(actual, ref -> {
            if (ref.isMarked()){
                markedNode.set(true);
            }
        });
        if (markedNode.get()) {
            failWithMessage("Expected list: \n%s\nto DOES NOT contain node with marked reference.",
                HarrisLinkedListTestUtils.toString(actual));
        }
        return this;
    }
}
