package com.example.repository;

import com.example.entity.QuizInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("repository")
public interface QuizRepository extends CrudRepository<QuizInfo, Integer> {
}
