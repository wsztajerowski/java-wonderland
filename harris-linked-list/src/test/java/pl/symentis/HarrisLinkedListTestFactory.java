package pl.symentis;

import java.util.Arrays;

public class HarrisLinkedListTestFactory {
    public static HarrisLinkedList<Integer> createEmptyIntegerList() {
        return new HarrisLinkedList<>();
    }

    public static HarrisLinkedList<Integer> createIntegerList(int ... keys) {
        Arrays.sort(keys);
        Node<Integer> newestNode = null;
        for (int i = keys.length -1; i >=0 ; i--) {
            newestNode = new Node<>(keys[i], newestNode);
        }
        HarrisLinkedList<Integer> list = new HarrisLinkedList<>();
        list.getHead().setNextNode(newestNode);
        return list;
    }
}
