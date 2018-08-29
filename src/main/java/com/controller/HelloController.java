package com.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.model.Appointment;
import com.model.User;
import com.service.AppointmentService;
import com.service.UserService;

@Controller
public class HelloController {
	
	@Autowired
	AppointmentService as;
	
	@Autowired
	UserService us;

	@GetMapping("/home")
	public String index(Model model){
		model.addAttribute("view", "home/index");
		model.addAttribute("header", "fragments/lognatHeader");
		return "base-layout";
	}
	
	
	@GetMapping("/register")
	public String register(Model model){
		model.addAttribute("view", "home/register");
		model.addAttribute("header", "fragments/header");
		return "base-layout";
	}
	
	@PostMapping("/register")
	public ModelAndView registerProcessing(User u){
		String str = us.register(u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getPhone());
		if(str.startsWith("This")) {
			return new ModelAndView("base-layout")
					.addObject("view", "home/helloPage")
					.addObject("user", str)
					.addObject("header", "fragments/header");
		}
		return new ModelAndView("base-layout")
				.addObject("view", "home/helloPage")
				.addObject("user", str)
				.addObject("header", "fragments/lognatHeader");
	}
	
	@GetMapping("/login")
	public String login(Model model){
		model.addAttribute("view", "home/login");
		model.addAttribute("header", "fragments/header");
		return "base-layout";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view", "appointment/createNew");
		model.addAttribute("header", "fragments/lognatHeader");
		model.addAttribute("str", "");
		return "base-layout";
	}
	
	@PostMapping("/create")
	public String createAppointmentProccesing(Appointment app, Model model) {
		String str = as.makeAnAppointment(SecurityContextHolder.getContext().getAuthentication().getName(), app.getDate().toString(), app.getHour().toString());
		model.addAttribute("view", "appointment/createNew");
		model.addAttribute("header", "fragments/lognatHeader");
		model.addAttribute("str", str);
		return "base-layout";
	}
	
	@GetMapping("/appointments/free")
	public String showFreeAppointments(Model model) {
		model.addAttribute("view", "appointment/freeAppointments");
		model.addAttribute("header", "fragments/lognatHeader");
		return "base-layout";
	}
	
	@PostMapping("/appointments/free")
	public @ResponseBody List<LocalTime> showFreeAppointmentsProcessing(Appointment app) {
		return as.showFreeHoursForDate(app.getDate().toString());
	}
}