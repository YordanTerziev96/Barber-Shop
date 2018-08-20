package com.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Appointment;
import com.service.AppointmentService;

@Controller
public class HelloController {
	
	@Autowired
	AppointmentService as;

	@GetMapping("/indexPage")
	public String index(Model model){
		model.addAttribute("view", "home/index");
		return "base-layout";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view", "appointment/createNew");
		return "base-layout";
	}
	
	@PostMapping("/create")
	public @ResponseBody String createAppointmentProccesing(Appointment app) {
		return as.makeAnAppointment(app.getDate().toString(), app.getHour().toString());
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
