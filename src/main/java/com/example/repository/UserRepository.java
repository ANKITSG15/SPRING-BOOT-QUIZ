package com.example.repository;

import com.example.UserDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<UserDetails,Integer> {

}
