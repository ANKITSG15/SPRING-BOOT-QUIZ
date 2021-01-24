package com.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("repository")
public interface QuizRepository extends CrudRepository<QuizInfo,Long> {
    QuizInfo findById(long qid);

}
