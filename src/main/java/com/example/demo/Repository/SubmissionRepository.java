package com.example.demo.Repository;

import com.example.demo.Entity.SubmissionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SubmissionRepository extends CrudRepository<SubmissionEntity, Integer> {
}
