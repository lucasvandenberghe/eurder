package org.switchfully.eurder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.switchfully.eurder.domain.Item;
import org.switchfully.eurder.repository.ItemRepository;
import org.switchfully.eurder.service.request.CreateItemRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ItemService.class)
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void givenValidItemRequest_whenCreateItem_thenItemCreatedSavedAndReturned() {
        CreateItemRequest createItemRequest = new CreateItemRequest(
          "test",
          "testdescription",
          5,
          21
        );

        Item createdItem = itemService.createItem(createItemRequest);

        assertThat(createdItem)
                .isNotNull();
        assertThat(createdItem.getId())
                .isNotNegative();
        assertThat(createdItem.getName())
                .isEqualTo("test");
        assertThat(createdItem.getDescription())
                .isEqualTo("testdescription");
        assertThat(createdItem.getPrice())
                .isEqualTo(5);
        assertThat(createdItem.getAmount())
                .isEqualTo(21);

        Item savedItem = itemRepository.findById(createdItem.getId()).orElse(null);

        assertThat(savedItem)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(createdItem);
    }

    @Test
    void givenItemsInRepository_whenGetAllItems_thenReturnListOfAllItems() {
        Item item1 = new Item(
                "test1",
                "testdescription1",
                5,
                21
        );

        Item item2 = new Item(
                "test2",
                "testdescription2",
                3,
                2
        );

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> items = itemService.getAllItems();

        assertThat(items)
                .isNotNull()
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(item1, item2);
    }
}