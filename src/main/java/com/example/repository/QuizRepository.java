package com.example.repository;

import com.example.QuizInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("repository")
public interface QuizRepository extends CrudRepository<QuizInfo,Long> {
    List<QuizInfo> findById(long uniqId);
}
