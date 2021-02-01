package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("studentRepository")
public interface StudentRepository extends CrudRepository<Student, String> {
}
