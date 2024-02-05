package pl.symentis;

import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

public class NodeAssert<T extends Comparable<T>> extends AbstractAssert<NodeAssert<T>, Node<T>> {
    NodeAssert(Node<T> actual) {
        super(actual, NodeAssert.class);
    }

    public static <A extends Comparable<A>> NodeAssert<A> assertThat(Node<A> node) {
        return new NodeAssert<>(node);
    }

    public NodeAssert<T> hasKey(T key) {
        isNotNull();
        checkNodeKey(actual, key);
        return this;
    }

    public NodeAssert<T> isTail() {
        isNotNull();
        Node<T> nextNode = actual.getNextNodeMarkableReference().getReference();
        if (nextNode != null) {
            failWithMessage("Expected node: \n %s\nto be last in the list, but had next reference to:\n <%s>",actual, nextNode);
        }
        return this;
    }

    public NodeAssert<T> hasNextNode() {
        isNotNull();
        Node<T> nextNode = actual.getNextNodeMarkableReference().getReference();
        if (nextNode == null) {
            failWithMessage("Expected node: \n <%s>\nto had a reference to another node, but it was the last node in the list",actual);
        }
        return this;
    }

    public NodeAssert<T> hasNextNodeWithKey(T key) {
        hasNextNode();
        Node<T> nextNode = actual.getNextNodeMarkableReference().getReference();
        checkNodeKey(nextNode, key);
        return this;
    }

    private void checkNodeKey(Node<T> node, T key){
        if (!Objects.equals(node.getKey(), key)) {
            failWithMessage("Expected node: \n %s\nto have key <%s>, but was <%s>", node, key, node.getKey());
        }
    }
}
