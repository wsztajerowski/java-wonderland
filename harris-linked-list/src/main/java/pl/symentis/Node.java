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

    public boolean hasNext() {
        return next.getReference() != null;
    }

    public Node<T> getNextNode() {
        return next.getReference();
    }

    public boolean hasKey(T key) {
        return this.key.equals(key);
    }

    public boolean hasKeyEqualOrGreaterThan(T key) {
        return this.key.compareTo(key) >= 0;
    }

    public boolean hasKeyLessThan(T key) {
        return this.key.compareTo(key) < 0;
    }

    public T getKey() {
        return key;
    }

    public boolean isMarked() {
        return next.isMarked();
    }

    public boolean tryInsertNewNextNode(Node<T> expectedNextNode, T key) {
        Node<T> newNode = new Node<>(key, expectedNextNode);
        return next.compareAndSet(expectedNextNode, newNode, false, false);
    }

    public boolean tryMarkNextNodeToDeletion(Node<T> expectedNextNode) {
        return next.attemptMark(expectedNextNode, true);
    }

    public boolean tryDeleteMarkedNextNodes(Node<T> expectedNextNode, Node<T> newNextNode) {
        return next.compareAndSet(expectedNextNode, newNextNode, true, false);
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

    void setNextNode(Node<T> node) {
        next.set(node, false);
    }

    void markNextNodeToDeletion() {
        next.set(next.getReference(), true);
    }
}
