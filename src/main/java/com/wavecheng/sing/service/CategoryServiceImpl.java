package com.wavecheng.sing.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wavecheng.sing.bo.CategoryBO;
import com.wavecheng.sing.entity.Attendee;
import com.wavecheng.sing.entity.Category;
import com.wavecheng.sing.repo.AttendeRepository;
import com.wavecheng.sing.repo.CategoryRepository;

@Service
public class CategoryServiceImpl {
	
	private static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private AttendeRepository attendeRepository;
	
	private int getAvailableCount(Category category) {
		List<Attendee> attendees =  attendeRepository.findByCategory(category);
		log.debug("searching categoryId={},name={}, startTme={}, maxCount={},attendess count={}", category.getId(),category.getName(),
				category.getBeginTime(),category.getMaxCount(),attendees.size());
		return category.getMaxCount() - attendees.size();
	}
	
	public List<CategoryBO> getCurrentChoice(){
		List<Category> activeCategory = categoryRepository.findByActive(true);
		List<CategoryBO> boList = new ArrayList<CategoryBO>();
		for(Category c: activeCategory) {
			CategoryBO cb = new CategoryBO();
			cb.setId(c.getId());
			cb.setMaxCount(c.getMaxCount());
			cb.setName(c.getName());
			cb.setAvailableCount(getAvailableCount(c));
			boList.add(cb);
		}
		return boList;
	}
	
	public synchronized boolean addAttendee(Attendee attendee){
		if(getAvailableCount(attendee.getCategory()) <= 0) {
			String msg = "额满，谢谢！";
			log.error(" {} is done! Failed to register: {}, {} ", attendee.getCategory().getName(), attendee.getName(), attendee.getPhone());
			throw new RuntimeException(msg);
		}	
		attendeRepository.save(attendee);
		return true;
	}
	
	public List<Category> getAllCategory(){
		return (List<Category>) categoryRepository.findAll();
	}
	
	public void updateCategory(Category category) {
		categoryRepository.save(category);
	}
	
	
}
