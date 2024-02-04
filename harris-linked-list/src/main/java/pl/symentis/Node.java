package pl.symentis;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicMarkableReference;

class Node<T extends Comparable<T>> {
    private final T key;
    private final AtomicMarkableReference<Node<T>> next;

    Node(T key, Node<T> nextNode) {
        this.key = key;
        next = new AtomicMarkableReference<>(nextNode, false);
    }

    public boolean hasKey(T key) {
        return this.key.equals(key);
    }

    public AtomicMarkableReference<Node<T>> getNextNodeMarkableReference() {
        return next;
    }

    public boolean hasKeyEqualOrGreaterThan(T key) {
        return this.key.compareTo(key) >= 0;
    }

    public T getKey() {
        return key;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
            .add("identityString=" +getNodeIdentityString(this))
            .add("key=" + key)
            .add("next=" + getAtomicReferenceToString(next))
            .toString();
    }

    private String getAtomicReferenceToString(AtomicMarkableReference<Node<T>> atomicMarkableReference) {
        return new StringJoiner(", ", AtomicMarkableReference.class.getSimpleName() + "[", "]")
            .add("isMarked=" + atomicMarkableReference.isMarked())
            .add("reference=" + getNodeIdentityString(atomicMarkableReference.getReference()))
            .toString();
    }

    private static <T extends Comparable<T>> String getNodeIdentityString(Node<T> node) {
        return Optional.ofNullable(node)
            .map(System::identityHashCode)
            .map(Integer::toHexString)
            .map(hexString -> "Node@" + hexString)
            .orElse("null");
    }
}
