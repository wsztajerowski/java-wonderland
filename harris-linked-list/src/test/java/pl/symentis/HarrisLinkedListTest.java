package pl.symentis;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.symentis.HarrisLinkedListAssert.assertThat;
import static pl.symentis.HarrisLinkedListTestFactory.createEmptyIntegerList;
import static pl.symentis.HarrisLinkedListTestFactory.createIntegerList;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HarrisLinkedListTest {
    private HarrisLinkedList<Integer> sut;

    @BeforeEach
    void beforeEach(){
        sut = createEmptyIntegerList();
    }

    @Test
    void insert_element_to_empty_list() {
        // when
        sut.insert(10);

        // then
        assertThat(sut)
            .hasSize(1)
            .extractFirstNode()
                .hasKey(10)
                .isTail();
    }

    @Test
    void insert_element_with_key_value_less_than_only_existing_element_in_list_put_it_on_first_node() {
        // given
        sut = createIntegerList(100);

        // when
        sut.insert(10);

        // then
        assertThat(sut)
            .hasSize(2)
            .isSorted()
            .containsKey(10);
    }

    @Test
    void insert_element_with_key_value_greater_than_only_existing_element_in_list_put_it_on_last_node() {
        // given
        sut = createIntegerList(20);

        // when
        sut.insert(40);

        // then
        assertThat(sut)
            .hasSize(2)
            .isSorted()
            .containsKey(40);
    }

    @Disabled
    @Test
    void insert_operation_removes_all_marked_nodes_with_key_value_less_or_equal_to_inserting_one(){
        // given
        sut = createIntegerList(10, 20, 30, 40, 50);
        HarrisLinkedListTestUtils.markNodesToDelete(sut, 10, 20, 40);

        // with pre-assumption
        assertThat(sut)
            .hasSize(5);

        // when
        sut.insert(25);

        // then
        assertThat(sut)
            .hasSize(4)
            .doesNotContainKey(10)
            .doesNotContainKey(20)
            .containsKey(25)
            .containsKey(40);
    }

    @Disabled
    @Test
    void insert_element_before_marked_node_removes_all_marked_nodes_first_unmarked_node_with_key_greater_than_inserting_one(){
        // given
        sut = createIntegerList(10, 20, 30, 40, 50, 60);
        HarrisLinkedListTestUtils.markNodesToDelete(sut, 10, 20, 40, 60);

        // with pre-assumption
        assertThat(sut)
            .hasSize(6);

        // when
        sut.insert(35);

        // then
        assertThat(sut)
            .hasSize(4)
            .doesNotContainKey(10)
            .doesNotContainKey(20)
            .containsKey(35)
            .containsKey(60);
    }

    @Test
    void find_existing_element_returns_true(){
        // given
        sut = createIntegerList(10,12,20,40,220);

        // when
        boolean isElementFound = sut.find(20);

        // then
        assertThat(isElementFound)
            .as("Verifying if find returns true")
            .isTrue();
    }

    @Test
    void find_non_existing_element_when_list_contains_elements_returns_false(){
        // given
        sut = createIntegerList(10,12,20,40,220);

        // when
        boolean isElementFound = sut.find(50);

        // then
        assertThat(isElementFound)
            .as("Verifying if find returns false")
            .isFalse();
    }

    @Test
    void find_element_when_list_is_empty_returns_false(){
        // when
        boolean isElementFound = sut.find(10);

        // then
        assertThat(isElementFound)
            .as("Verifying if find returns false")
            .isFalse();
    }

    @Disabled
    @Test
    void find_operation_removes_all_marked_nodes_with_key_value_less_or_equal_to_searching_one(){
        // given
        sut = createIntegerList(10,12,20,40,220);
        HarrisLinkedListTestUtils.markNodesToDelete(sut, 10, 12, 40);

        // with pre-assumption
        assertThat(sut)
            .hasSize(5);

        // when
        sut.find(12);

        // then
        assertThat(sut)
            .hasSize(3)
            .doesNotContainKey(10)
            .doesNotContainKey(12)
            .containsKey(20)
            .containsKey(40);
    }

    @Test
    void delete_element_from_list_with_size_1_makes_list_empty(){
        // given
        sut = createIntegerList(45);

        // when
        boolean deleted = sut.delete(45);

        // then
        assertThat(deleted)
            .as("Verifying if delete returns true")
            .isTrue();

        // and
        assertThat(sut)
            .isEmpty();
    }

    @Test
    void delete_existing_element_returs_true(){
        // given
        sut = createIntegerList(15, 45, 50);

        // when
        boolean deleted = sut.delete(45);

        // then
        assertThat(deleted)
            .as("Verifying if delete returns true")
            .isTrue();
    }

    @Test
    void delete_non_existing_element_returs_false(){
        // given
        sut = createIntegerList(15, 45, 50);

        // when
        boolean deleted = sut.delete(30);

        // then
        assertThat(deleted)
            .as("Verifying if delete returns false")
            .isFalse();
    }

    @Test
    void after_delete_existing_element_no_node_is_marked(){
        // given
        sut = createIntegerList(15, 45, 50);

        // when
        boolean deleted = sut.delete(15);

        // then
        assertThat(deleted)
            .as("Verifying if delete returns true")
            .isTrue();

        // and
        assertThat(sut)
            .hasSize(2)
            .doesNotContainMarkedNodes();
    }

    @Test
    void after_delete_non_existing_element_no_node_is_marked(){
        // given
        sut = createIntegerList(15, 45, 50);

        // when
        boolean deleted = sut.delete(35);

        // then
        assertThat(deleted)
            .as("Verifying if delete returns false")
            .isFalse();

        // and
        assertThat(sut)
            .hasSize(3)
            .doesNotContainMarkedNodes();
    }

}