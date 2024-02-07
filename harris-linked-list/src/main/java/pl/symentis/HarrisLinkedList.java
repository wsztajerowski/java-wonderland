package pl.symentis;

public class HarrisLinkedList<T extends Comparable<T>> {

    private final Node<T> head;

    public HarrisLinkedList() {
        this.head = new Node<>(null, null);
    }

    public boolean isEmpty() {
        return !head.hasNext();
    }

    public boolean insert(T key) {
        do {
            Pair<Node<T>, Node<T>> result = searchPreviousNodeReference(key);
            Node<T> leftNode = result.left();
            Node<T> rightNode = result.right();
            if (rightNode != null && rightNode.hasKey(key)) {
                return false;
            }
            if(leftNode.tryInsertNewNextNode(rightNode, key)){
                return true;
            }
        } while (true);
    }

    public boolean find(T key) {
        Pair<Node<T>, Node<T>> result  = searchPreviousNodeReference(key);
        Node<T> rightNode = result.right();
        return rightNode != null && rightNode.hasKey(key);
    }

    public boolean delete(T key) {
        Node<T> leftNode;
        Node<T> rightNode;
        Node<T> rightNodeNext;
        do {
            Pair<Node<T>, Node<T>> result = searchPreviousNodeReference(key);
            leftNode = result.left();
            rightNode = result.right();
            if (rightNode == null || !rightNode.hasKey(key)) {
                return false;
            }
            rightNodeNext = rightNode.getNextNode();
            if (rightNodeNext == null || !rightNodeNext.isMarked()) {
                if(leftNode.tryMarkNextNodeToDeletion(rightNode)){
                    break;
                }
            }
        } while (true);
        if (!leftNode.tryDeleteMarkedNextNodes(rightNode, rightNodeNext)){
            searchPreviousNodeReference(key);
        }
        return true;
    }

    private Pair<Node<T>, Node<T>> searchPreviousNodeReference(T key) {
        Node<T> leftNode = head;
        Node<T> leftNodeNext = null;
        Node<T> rightNode;

        /* 1: Find left_node and right_node */
        do {
            Node<T> t = head;
            Node<T> tNext = head.getNextNode();
            do {
                if (!t.isMarked()){
                    leftNode = t;
                    leftNodeNext = tNext;
                }
                t = tNext;
                if (t == null){
                    break;
                }
                tNext = t.getNextNode();
            } while (t.isMarked() || t.hasKeyLessThan(key));
            rightNode = t;

            /* 2: Check nodes are adjacent */
            if (leftNodeNext == rightNode){
                if (rightNode != null && rightNode.isMarked()){
                    continue;
                } else {
                    return new Pair<>(leftNode, rightNode);
                }
            }

            /* 3: Remove one or more marked nodes */
            if (leftNode.tryDeleteMarkedNextNodes(leftNodeNext, rightNode)){
                if (rightNode == null || !rightNode.isMarked()) {
                    return new Pair<>(leftNode, rightNode);
                }
            }
        } while (true);
    }

    public int size() {
        int nodeCounter = 0;
        Node<T> node = head;
        while (node.hasNext()) {
            nodeCounter++;
            node = node.getNextNode();
        }
        return nodeCounter;
    }

    /* for test only */
    Node<T> getHead() {
        return head;
    }
}