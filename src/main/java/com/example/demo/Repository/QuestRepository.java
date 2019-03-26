package com.example.demo.Repository;

import com.example.demo.Entity.QuestEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuestRepository extends CrudRepository<QuestEntity, Integer> {
}
