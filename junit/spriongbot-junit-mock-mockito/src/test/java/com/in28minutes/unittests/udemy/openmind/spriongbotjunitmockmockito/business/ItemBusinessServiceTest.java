package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.business;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.Item;
import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemBusinessServiceTest {

    @InjectMocks
    private ItemBusinessService business;

    @Mock
    private ItemRepository repository;

    @Test
    void retrieveAllItems_basic() {
        when(repository.findAll()).thenReturn(Arrays.asList(new Item(2,"Item2",10,10),
                new Item(3,"Item3",20,20)));
        List<Item> items = business.retrieveAllItems();

        assertEquals(100, items.get(0).getValue());
        assertEquals(400, items.get(1).getValue());
    }
}
