package com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.data;

import com.in28minutes.unittests.udemy.openmind.spriongbotjunitmockmockito.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
