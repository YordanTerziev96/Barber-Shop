package com.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		return "base-layout";
	}
	
	@GetMapping("/register")
	public String register(Model model){
		model.addAttribute("view", "home/register");
		return "base-layout";
	}
	
	@PostMapping("/register")
	public ModelAndView registerProcessing(User u, Model model){
		us.register(u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getPhone());
		model.addAttribute("user", u);
		return new ModelAndView("base-layout").addObject("view", "home/helloPage");
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view", "appointment/create");
		return "base-layout";
	}
	
	@PostMapping("/create")
	public @ResponseBody String createAppointmentProccesing(Appointment app) {
		return as.makeAnAppointment(SecurityContextHolder.getContext().getAuthentication().getName(), app.getDate().toString(), app.getHour().toString());
	}
	
	@GetMapping("/appointments/free")
	public String showFreeAppointments(Model model) {
		model.addAttribute("view", "appointment/freeAppointments");
		return "base-layout";
	}
	
	@PostMapping("/appointments/free")
	public @ResponseBody List<LocalTime> showFreeAppointmentsProcessing(Appointment app) {
		return as.showFreeHoursForDate(app.getDate().toString());
	}
}
