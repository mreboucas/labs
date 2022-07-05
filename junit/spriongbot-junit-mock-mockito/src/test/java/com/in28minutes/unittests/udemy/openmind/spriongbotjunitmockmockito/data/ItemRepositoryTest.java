package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@RunWith(SpringRunner.class) -> only junit4
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Test
    public void testFindAll() {
        List<Item> items = repository.findAll();
        assertEquals(3,items.size());
    }

    @Test
    public void testFindOne() {
        Item item = repository.findById(10001).get();

        assertEquals("Item1",item.getName());
    }

}
