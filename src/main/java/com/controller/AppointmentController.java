package com.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.AppointmentDTO;
import com.service.AppointmentService;

@RestController
@RequestMapping(value = "/appointment")
public class AppointmentController {

	@Autowired
	AppointmentService as;

	
	@RequestMapping(value = "/new", method = RequestMethod.POST, params = { "date", "hour"})
	@Transactional
	public String makeAnAppointment(String date, String hour) {
		
			return as.makeAnAppointment(SecurityContextHolder.getContext().getAuthentication().getName(), date, hour);
	}
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> showHours() {
		
		HashMap<LocalDate, List<LocalTime>> mapche = as.generateDates();
		return new ResponseEntity<HashMap<LocalDate, List<LocalTime>>>(mapche, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appointmentsAtDate", method = RequestMethod.GET, params= {"date"})
	@Transactional
	public ResponseEntity<?> showAppointmentsAtDate(String date) {
		
		return new ResponseEntity<HashMap<String, List<LocalTime>>>(as.showAppointedHoursForDate(date), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/freeAppointments", method = RequestMethod.GET, params= {"date"})
	@Transactional
	public @ResponseBody ResponseEntity<?> showFreeHoursAtDate(String date) {
		
		return new ResponseEntity<List<LocalTime>>(as.showFreeHoursForDatee(date), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appointmentsForThisWeek", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> showAppointmentsForThisWeek() {
		return new ResponseEntity<List<AppointmentDTO>>(as.AppointedHoursForThisWeek(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/appointmentsForNextWeek", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> showAppointmentsForNextWeek() {
		return new ResponseEntity<List<AppointmentDTO>>(as.AppointedHoursForNextWeek(), HttpStatus.OK);
	}
}
