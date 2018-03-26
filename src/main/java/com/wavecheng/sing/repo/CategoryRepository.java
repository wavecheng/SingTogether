package com.wavecheng.sing.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wavecheng.sing.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

	List<Category> findByActive(boolean isActive);
	
}
