package com.wavecheng.sing.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wavecheng.sing.entity.Attendee;
import com.wavecheng.sing.entity.Category;

@Repository
public interface AttendeRepository extends CrudRepository<Attendee, Integer> {

	List<Attendee> findByCategory(Category category);
}
