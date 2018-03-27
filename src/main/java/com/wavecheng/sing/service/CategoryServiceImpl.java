package com.wavecheng.sing.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private long getAvailableCount(Category category) {
		List<Attendee> attendees =  attendeRepository.findByCategory(category);
		
		//filter after registered
		long count = attendees.stream().filter( t -> t.getRegisterTime().after(category.getBeginTime())).count();
		log.debug("searching categoryId={},name={}, startTme={}, maxCount={},attendess count={}",
				 category.getId(),category.getName(),
				category.getBeginTime(),category.getMaxCount(),count);
		long result = category.getMaxCount() - count ;
		return result > 0 ? result: 0;
	}
	
	public List<CategoryBO> getCurrentChoice(){
		List<Category> activeCategory = categoryRepository.findByActive(true);
		List<CategoryBO> boList = new ArrayList<CategoryBO>();
		for(Category c: activeCategory) {
			
			//not begin, skip
			log.info("{} checking beginTime:{}, current time:{}" , c.getName(), c.getBeginTime().getTime(),
					  System.currentTimeMillis());
			if((System.currentTimeMillis() - c.getBeginTime().getTime()) < 0) {				
					continue;
			}
			
			CategoryBO cb = new CategoryBO();
			cb.setId(c.getId());
			cb.setMaxCount(c.getMaxCount());
			cb.setName(c.getName());
			cb.setAvailableCount((int)getAvailableCount(c));
			boList.add(cb);
		}
		return boList;
	}
	
	public synchronized boolean addAttendee(String name,String phone, int categoryId){
		Category category = categoryRepository.findOne(categoryId);		
		if(getAvailableCount(category) <= 0) {			
			log.error(" {} is done! Failed to register: {}, {} ", category.getName(), name, phone);
			throw new RuntimeException(name + ":"+ phone + " failed to register:" + category.getName());
		}	
		
		Attendee attendee = new Attendee();
		attendee.setCategory(category);
		attendee.setName(name);
		attendee.setPhone(phone);
		attendeRepository.save(attendee);
		return true;
	}
	
	public List<Category> getAllCategory(){
		return (List<Category>) categoryRepository.findAll();
	}
	
	public void updateCategory(int id, String name, String beginDate, boolean active, int maxCount) throws ParseException {
		Category cat = categoryRepository.findOne(id);
		cat.setName(name);
		
		cat.setBeginTime(Timestamp.from(df.parse(beginDate).toInstant()));
		cat.setMaxCount(maxCount);
		cat.setActive(active);
		categoryRepository.save(cat);
	}
	
	public Category getCategory(int id) {
		return categoryRepository.findOne(id);
	}
	
	public List<Attendee> getAttendeesByCategory(int id){
		List<Attendee> userList = attendeRepository.findByCategory(categoryRepository.findOne(id));
		return userList.stream().filter( t -> t.getRegisterTime().after(t.getCategory().getBeginTime()))
				.collect(Collectors.toList());
	}

	public void deleteAttendee(int id) {
		attendeRepository.delete(id);
	}

	public void addCategory(String name, String beginDate, boolean active, int maxCount) throws ParseException {
		Category c = new Category();
		c.setActive(active);
		c.setBeginTime(Timestamp.from(df.parse(beginDate).toInstant()));
		c.setMaxCount(maxCount);
		c.setName(name);	
		categoryRepository.save(c);
	}

	public void deleteCategory(int id) {
		categoryRepository.delete(id);
	}
}
