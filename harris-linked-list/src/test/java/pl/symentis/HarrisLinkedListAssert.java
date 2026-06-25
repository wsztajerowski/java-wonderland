package pl.symentis;

import org.assertj.core.api.AbstractAssert;

import java.util.concurrent.atomic.AtomicBoolean;

public class HarrisLinkedListAssert<T extends Comparable<T>> extends AbstractAssert<HarrisLinkedListAssert<T>, HarrisLinkedList<T>> {
    HarrisLinkedListAssert(HarrisLinkedList<T> actual) {
        super(actual, HarrisLinkedListAssert.class);
    }

    public static <A extends Comparable<A>> HarrisLinkedListAssert<A> assertThat(HarrisLinkedList<A> node) {
        return new HarrisLinkedListAssert<>(node);
    }

    public HarrisLinkedListAssert<T> isEmpty() {
        isNotNull();
        if (!actual.isEmpty()){
            failWithMessage("Expected list: \n%s\nto be empty!", HarrisLinkedListTestUtils.toString(actual));
        }
        return this;
    }

    public HarrisLinkedListAssert<T> hasSize(int expectedSize) {
        isNotNull();
        int size = actual.size();
        if (size != expectedSize) {
            failWithMessage("Expected list: \n%s\nto have size <%s>, but was <%s>",
                HarrisLinkedListTestUtils.toString(actual),
                expectedSize,
                size);
        }
        return this;
    }

    public NodeAssert<T> extractFirstNode() {
        isNotNull();
        return new NodeAssert<>(actual.getHead().getNextNode());
    }

    public HarrisLinkedListAssert<T> isSorted() {
        isNotNull();
        boolean isSorted = true;
        Node<T> current = actual.getHead().getNextNode();
        while (current != null && current.hasNext()) {
            Node<T> next = current.getNextNode();
            if (current.hasKeyEqualOrGreaterThan(next.getKey())) {
                isSorted = false;
                break;
            }
            current = next;
        }
        if (!isSorted) {
            failWithMessage("Expected list: \n%s\nto have nodes ordered by keys, but node: \n%s\nhave smaller key than its successor: \n%s",
                HarrisLinkedListTestUtils.toString(actual),
                current,
                current.getNextNode());
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
        HarrisLinkedListTestUtils.iterateOverNodes(actual, node -> {
            if (node.isMarked()){
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
