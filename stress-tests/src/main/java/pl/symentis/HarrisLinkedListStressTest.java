package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.ZZI_Result;
import org.openjdk.jcstress.infra.results.ZZZZ_Result;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;

public class HarrisLinkedListStressTest {
    @JCStressTest
    @Outcome(id = "true, true, 4", expect = ACCEPTABLE,             desc = "Both keys were inserted properly.")
    @State
    public static class CaseInsertInsert {
        HarrisLinkedList<Integer> harrisList;
        CaseInsertInsert(){
            harrisList = createIntegerList(10,20);
        }

        @Actor
        public void actor1(ZZI_Result r) {
            r.r1 = harrisList
                .insert(15);
        }

        @Actor
        public void actor2(ZZI_Result r) {
            r.r2 = harrisList
                .insert(18);
        }

        @Arbiter
        public void arbiter(ZZI_Result r) {
            r.r3 = harrisList
                .size();
        }
    }
    @JCStressTest
    @Outcome(id = "true, true, 3", expect = ACCEPTABLE,             desc = "Element was inserted correctly and other one was found")
    @State
    public static class CaseInsertFind {
        HarrisLinkedList<Integer> harrisList;
        CaseInsertFind(){
            harrisList = createIntegerList(10,20);
        }

        @Actor
        public void actor1(ZZI_Result r) {
            r.r1 = harrisList
                .insert(15);
        }

        @Actor
        public void actor2(ZZI_Result r) {
            r.r2 = harrisList
                .find(20);
        }

        @Arbiter
        public void arbiter(ZZI_Result r) {
            r.r3 = harrisList
                .size();
        }
    }
    @JCStressTest
    @Outcome(id = "true, true, 2", expect = ACCEPTABLE,             desc = "One element was added to list and other one was removed from it.")
    @State
    public static class CaseInsertDelete {
        HarrisLinkedList<Integer> harrisList;
        CaseInsertDelete(){
            harrisList = createIntegerList(10,20);
        }

        @Actor
        public void actor1(ZZI_Result r) {
            r.r1 = harrisList
                .insert(15);
        }

        @Actor
        public void actor2(ZZI_Result r) {
            r.r2 = harrisList
                .delete(10);
        }

        @Arbiter
        public void arbiter(ZZI_Result r) {
            r.r3 = harrisList
                .size();
        }
    }
    @JCStressTest
    @Outcome(id = "true, true, 2", expect = ACCEPTABLE,             desc = "Both keys were deleted from list.")
    @State
    public static class CaseDeleteDelete {
        HarrisLinkedList<Integer> harrisList;
        CaseDeleteDelete(){
            harrisList = createIntegerList(10,20,30,40);
        }

        @Actor
        public void actor1(ZZI_Result r) {
            r.r1 = harrisList
                .delete(30);
        }

        @Actor
        public void actor2(ZZI_Result r) {
            r.r2 = harrisList
                .delete(40);
        }

        @Arbiter
        public void arbiter(ZZI_Result r) {
            r.r3 = harrisList
                .size();
        }
    }
    @JCStressTest
    @Outcome(id = "true, true, 3", expect = ACCEPTABLE,             desc = "One key was removed from list and other one found in list.")
    @State
    public static class CaseDeleteFind {
        HarrisLinkedList<Integer> harrisList;
        CaseDeleteFind(){
            harrisList = createIntegerList(10,20,30,40);
        }

        @Actor
        public void actor1(ZZI_Result r) {
            r.r1 = harrisList
                .delete(30);
        }

        @Actor
        public void actor2(ZZI_Result r) {
            r.r2 = harrisList
                .find(40);
        }

        @Arbiter
        public void arbiter(ZZI_Result r) {
            r.r3 = harrisList
                .size();
        }
    }
    @JCStressTest
    @Outcome(id = "true, true, true, true", expect = ACCEPTABLE,             desc = "All operation succeed and the final list size is equal expected value")
    @State
    public static class CaseInsertDeleteFindInRandomOrder {
        private static final List<Integer> elements = Arrays.asList(10, 20, 30, 40);
        private static final List<Integer> numbersToInsert = Arrays.asList(5, 15, 25, 35, 45);
        HarrisLinkedList<Integer> harrisList;
        Integer deleteArg;
        Integer findArg;
        Integer insertArg;

        CaseInsertDeleteFindInRandomOrder(){
            harrisList = createIntegerList(10,20,30,40);
            Collections.shuffle(elements);
            deleteArg = elements.get(0);
            findArg = elements.get(1);
            Collections.shuffle(numbersToInsert);
            insertArg = numbersToInsert.get(0);
        }

        @Actor
        public void actor1(ZZZZ_Result r) {
            r.r1 = harrisList
                .delete(deleteArg);
        }

        @Actor
        public void actor2(ZZZZ_Result r) {
            r.r2 = harrisList
                .find(findArg);
        }

        @Actor
        public void actor3(ZZZZ_Result r) {
            r.r3 = harrisList
                .insert(insertArg);
        }

        @Arbiter
        public void arbiter(ZZZZ_Result r) {
            r.r4 = harrisList.size() == 4;
        }
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
