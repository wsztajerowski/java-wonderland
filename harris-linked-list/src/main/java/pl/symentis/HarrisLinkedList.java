package pl.symentis;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class HarrisLinkedList<T extends Comparable<T>> {

    private final AtomicMarkableReference<Node<T>> head;

    public HarrisLinkedList() {
        this.head = new AtomicMarkableReference<>(null, false);
    }

    public boolean insert(T key) {
        boolean isNodeAdded;
        do {
            AtomicMarkableReference<Node<T>> leftNodeRef = searchPreviousNodeReference(key);
            Node<T> rightNode = leftNodeRef.getReference();
            if (rightNode != null && rightNode.hasKey(key)) {
                return false;
            }
            Node<T> newNode = new Node<>(key, rightNode);
            isNodeAdded = leftNodeRef.compareAndSet(rightNode, newNode, false, false);
        } while (!isNodeAdded);
        return true;
    }

    public boolean find(T key) {
        AtomicMarkableReference<Node<T>> leftNodeRef = searchPreviousNodeReference(key);
        Node<T> rightNode = leftNodeRef.getReference();
        return rightNode != null && rightNode.hasKey(key);
    }

    public boolean delete(T key) {
        boolean isReferenceMarked;
        do {
            AtomicMarkableReference<Node<T>> leftNodeRef = searchPreviousNodeReference(key);
            Node<T> rightNode = leftNodeRef.getReference();
            if (rightNode == null || !rightNode.hasKey(key)) {
                return false;
            }
            isReferenceMarked = leftNodeRef.attemptMark(rightNode, true);
        } while (!isReferenceMarked);
        searchPreviousNodeReference(key);
        return true;
    }

    private AtomicMarkableReference<Node<T>> searchPreviousNodeReference(T key) {
        start_again:
        while (true) {
            AtomicMarkableReference<Node<T>> nodeRef = head;
            Node<T> nextNode = head.getReference();
            do {
                if (nodeRef.isMarked()) {
                    AtomicMarkableReference<Node<T>> firstUnmarkedNodeRef = nodeRef;
                    while (firstUnmarkedNodeRef.getReference() != null && firstUnmarkedNodeRef.isMarked()) {
                        firstUnmarkedNodeRef = firstUnmarkedNodeRef.getReference().getNextNodeMarkableReference();
                    }
                    if (nodeRef.compareAndSet(nextNode, firstUnmarkedNodeRef.getReference(), true, false)){
                        nextNode = nodeRef.getReference();
                    } else {
                        break start_again;
                    }
                }
                if (nextNode == null || nextNode.hasKeyEqualOrGreaterThan(key)) {
                    return nodeRef;
                }
                nodeRef = nextNode.getNextNodeMarkableReference();
                nextNode = nodeRef.getReference();
            } while (true);
        }
        return null;
    }

    /* for test only */
    AtomicMarkableReference<Node<T>> getHead() {
        return head;
    }
}