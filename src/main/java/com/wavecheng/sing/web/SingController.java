package com.wavecheng.sing.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	@RequestMapping(value="user/category",method=RequestMethod.GET)
	public String adminCategory() {
		return "user/category";
	}
}
