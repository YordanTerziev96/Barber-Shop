package com.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.model.Appointment;
import com.model.User;

@Service
@Transactional
public class AppointmentService {

	@PersistenceContext
	EntityManager emm;
	
	static List<LocalTime> hours = new ArrayList<LocalTime>();
    static HashMap<LocalDate, List<LocalTime>> map = new HashMap<LocalDate, List<LocalTime>>();

	@SuppressWarnings("unchecked")
	public String makeAnAppointment(String date, String hour) {
		
		User u = (User) emm.createQuery("Select u from User u WHERE u.id = 1").getSingleResult();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, formatter);
		LocalTime t = LocalTime.parse(hour);
		if(validateDate(d)) {
			List<Appointment> ap = new ArrayList<>();
			ap =  emm.createQuery("Select a from Appointment a where a.date =:date and a.hour =:hour")
					.setParameter("date", d).setParameter("hour", t).getResultList();
			if(ap.size() == 0) {
				Appointment a = new Appointment();
				a.setUser(u);
				a.setDate(d);
				a.setHour(t);
				emm.persist(a);
				return "You`ve made an appointment for " + hour + " at " + date;
			}
			else {
				return "This hour has been already taken! Choose another one!";
			}
		}
		else {
			return "Invalid date !";
		}
	}
	
	public boolean validateDate(LocalDate date) {
		if(date.isBefore(LocalDate.now())) {
			return false;
		}
		else if(date.getDayOfWeek().getValue() == 7) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Appointment> showAppointedHoursForDate(String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, formatter);

		List<Appointment> apps = emm.createQuery("Select a from Appointment a where a.date =:date")
				.setParameter("date", d).getResultList();
		return apps;
	}
	
	@SuppressWarnings("unchecked")
	public List<LocalTime> showFreeHoursForDate(String date) {
		generateHours();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, formatter);
		
		List<LocalTime> reservatedHours = emm.createQuery("Select a.hour from Appointment a where a.date =:date")
				.setParameter("date", d).getResultList();
		for(LocalTime hour : reservatedHours) {
			if(hours.contains(hour)) {
				hours.remove(hour);
			}
		}
		return hours;
	}
	
	@SuppressWarnings("unchecked")
	public List<LocalTime> showFreeHoursForDatee(String date) {
		generateHours();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate d = LocalDate.parse(date, formatter);
		
		List<LocalTime> reservatedHours = emm.createQuery("Select a.hour from Appointment a where a.date =:date")
				.setParameter("date", d).getResultList();
		for(LocalTime hour : reservatedHours) {
			if(hours.contains(hour)) {
				hours.remove(hour);
			}
		}
		return hours;
	}
	
	public void generateHours() {
		 DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
		 LocalTime time = LocalTime.parse("10:00:00", dateTimeFormatter);
		 hours.add(time);
		 LocalTime lastHour = LocalTime.parse("18:00:00", dateTimeFormatter);
		while(time.compareTo(lastHour) != 0) {
			time = time.plusMinutes(30);
			hours.add(time);
		}
		
	}
	
	public HashMap<LocalDate, List<LocalTime>> generateDates() {
		LocalDate date = LocalDate.now();
		generateHours();
		map.put(date, hours);
		for(int i = 0; i < 6; i++) {
			date = date.plusDays(1);
			map.put(date, hours);
		}
		return map;
	}

}