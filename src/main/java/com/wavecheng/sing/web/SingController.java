package com.wavecheng.sing.web;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wavecheng.sing.entity.Attendee;
import com.wavecheng.sing.service.CategoryServiceImpl;

@Controller
public class SingController {

	@Autowired
	private CategoryServiceImpl service;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String index(Model model) {
		Object obj =  service.getCurrentChoice();
		model.addAttribute("categories",obj);
		model.addAttribute("test", 1);
		return "index";
	}
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public String index(@RequestParam(name="name",required=true) String name, @RequestParam(name="phone",required=true) String phone,
			@RequestParam(name="category",required=true) int categoryId) {
		try{
			service.addAttendee(name, phone, categoryId);
		}catch(Exception ex) {
			return "full";
		}
		return "thanks";
	}
	
	@RequestMapping(value="user/category",method=RequestMethod.GET)
	public String adminCategory(Model model) {
		model.addAttribute("allCategories", service.getAllCategory());
		return "user/category";
	}

	@RequestMapping(value="user/category/{id}/view_attendee",method=RequestMethod.GET)
	public String viewAttendee(@PathVariable int id, Model model) {
		List<Attendee> attendees = service.getAttendeesByCategory(id); 
		model.addAttribute("attendees", attendees);
		model.addAttribute("category", service.getCategory(id));
		return "user/view_attendee";
	}

	@RequestMapping(value="user/category/add",method=RequestMethod.GET)
	public String addCategory() {	
		return "user/category_add";
	}

	@RequestMapping(value="user/category/add",method=RequestMethod.POST)
	public String addCategory(@RequestParam String name, @RequestParam String date,
			@RequestParam boolean active, @RequestParam int maxCount) throws ParseException {
		service.addCategory(name,date,active,maxCount);
		return "redirect:/user/category";
	}

	@RequestMapping(value="user/category/{id}/delete",method=RequestMethod.GET)
	public String deleteCategory(@PathVariable int id) throws ParseException {
		service.deleteCategory(id);
		return "redirect:/user/category";
	}
	
	@RequestMapping(value="user/category/{id}/view",method=RequestMethod.GET)
	public String viewCategory(@PathVariable int id,Model model) {
		model.addAttribute("cat", service.getCategory(id));
		return "user/category_view";
	}
	
	@RequestMapping(value="user/category/{id}/update",method=RequestMethod.POST)
	public String viewAttendee(@RequestParam int id, @RequestParam String name, @RequestParam String date,
			@RequestParam boolean active, @RequestParam int maxCount) throws ParseException {
		service.updateCategory(id,name,date,active,maxCount);
		return "redirect:/user/category";
	}

	@RequestMapping(value="user/category/{categoryId}/attendee/{id}/delete",method=RequestMethod.GET)
	public String deleteAttendee(@PathVariable("categoryId") int categoryId,  @PathVariable("id") int id ) {
		service.deleteAttendee(id);
		return "redirect:/user/category/"+ categoryId+ "/view_attendee";
	}
	
	@RequestMapping(value="user/login",method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	
}
