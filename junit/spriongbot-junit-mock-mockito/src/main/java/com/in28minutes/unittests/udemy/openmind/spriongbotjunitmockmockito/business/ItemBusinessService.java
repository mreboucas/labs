package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.business;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.Item;
import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemBusinessService {

    @Autowired
    private ItemRepository repository;

    public Item retreiveHardcodedItem() {
        return new Item(1, "Ball", 10, 100);
    }

    public List<Item> retrieveAllItems() {
        List<Item> items = repository.findAll();

        for(Item item:items) {
            item.setValue(item.getPrice() * item.getQuantity());
        }

        return items;
    }
}
