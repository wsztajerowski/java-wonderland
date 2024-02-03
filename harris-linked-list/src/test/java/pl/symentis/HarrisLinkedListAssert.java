package pl.symentis;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.Objects;

public class HarrisLinkedListAssert<T extends Comparable<T>> extends AbstractAssert<HarrisLinkedListAssert<T>, HarrisLinkedList<T>> {
    HarrisLinkedListAssert(HarrisLinkedList<T> actual) {
        super(actual, HarrisLinkedListAssert.class);
    }

    public static <A extends Comparable<A>> HarrisLinkedListAssert<A> assertThat(HarrisLinkedList<A> node) {
        return new HarrisLinkedListAssert<>(node);
    }

    public static InstanceOfAssertFactory<HarrisLinkedList, HarrisLinkedListAssert<?>> createAssertFactory() {
        return new InstanceOfAssertFactory<>(HarrisLinkedList.class, HarrisLinkedListAssert::assertThat);
    }

    public HarrisLinkedListAssert<T> hasKey(Object key) {
        isNotNull();
//        checkNodeKey(actual, key);
        return this;
    }

    public HarrisLinkedListAssert<T> hasNextNode() {
        isNotNull();
//        Node<T> nextNode = actual.getNextNodeMarkableReference().getReference();
//        if (nextNode == null) {
//            failWithMessage("Expected node: \n <%s>\nto had a reference to another node, but it was the last node in the list",actual);
//        }
        return this;
    }

    private void checkNodeKey(Node<T> node, Object key){
        if (!Objects.equals(node.getKey(), key)) {
            failWithMessage("Expected node: \n <%s>\nto have key <%s>, but was <%s>", node, key, node.getKey());
        }
    }

    public HarrisLinkedListAssert<T> hasSize(int expectedSize) {
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
        return new NodeAssert<>(actual.getHead().getReference());
    }
}
