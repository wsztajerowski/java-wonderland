package pl.symentis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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
            .extractFirstNode()
                .hasKey(10)
                .hasNextNodeWithKey(100);
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
            .extractFirstNode()
                .hasKey(20)
                .hasNextNodeWithKey(40);
    }

    @Test
    void find_existing_element_returns_true(){
        // given
        sut = createIntegerList(10,12,20,40,220);

        // when
        boolean isElementFound = sut.find(20);

        // then
        assertThat(isElementFound)
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
            .isFalse();
    }

    @Test
    void find_element_when_list_is_empty_returns_false(){
        // when
        boolean isElementFound = sut.find(10);

        // then
        assertThat(isElementFound)
            .isFalse();
    }

}