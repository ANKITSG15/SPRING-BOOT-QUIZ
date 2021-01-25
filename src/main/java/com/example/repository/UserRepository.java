package com.example.repository;

import com.example.UserDetails;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDetails,Integer> {

}
