package pl.symentis;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.ZZI_Result;

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
    public static HarrisLinkedList<Integer> createIntegerList(int ... keys) {
        Node<Integer> newestNode = null;
        for (int i = keys.length - 1; i >=0 ; i--) {
            newestNode = new Node<>(keys[i], newestNode);
        }
        HarrisLinkedList<Integer> list = new HarrisLinkedList<>();
        list.getHead().set(newestNode, false);
        return list;
    }
}
