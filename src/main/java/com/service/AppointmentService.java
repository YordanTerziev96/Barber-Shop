package com.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.model.AppointmentDTO;
import com.model.User;

@Service
@Transactional
public class AppointmentService {

	@PersistenceContext
	EntityManager emm;
	
	
    static HashMap<LocalDate, List<LocalTime>> map = new HashMap<LocalDate, List<LocalTime>>();

	@SuppressWarnings("unchecked")
	public String makeAnAppointment(String username, String date, String hour) {
		
		User u = (User) emm.createQuery("Select u from User u WHERE u.username =:username")
				.setParameter("username", username).getSingleResult();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, formatter);
		LocalTime t = LocalTime.parse(hour);
		
		if(validateDate(d)) {
			List<Appointment> ap = new ArrayList<>();
			ap =  emm.createQuery("Select a from Appointment a where a.date =:date and a.hour =:hour")
					.setParameter("date", d).setParameter("hour", t).getResultList();
			if(validateAppointment(u, d)) {
				
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
			}else {
				return "You cannot make appoint in such a close range !";
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
	public boolean validateAppointment(User u, LocalDate date) {
		List<Appointment> appointments = emm.createQuery("Select a from Appointment a where a.user =:user")
				.setParameter("user", u).getResultList();
		if(appointments.size() == 0) {
			return true;
		}
		else {
			for(Appointment a : appointments) {
				
				LocalDate dp = date.plusDays(3);
				LocalDate dm = date.minusDays(3);
				
				if(a.getDate().compareTo(date) == 0) {
					return false;
				}
				else if(a.getDate().isBefore(date) && a.getDate().isAfter(dm)) {
					return false;
				}
				else if(a.getDate().isAfter(date) && a.getDate().isBefore(dp)) {
					return false;
				}
			}
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, List<LocalTime>> showAppointedHoursForDate(String date) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate d = LocalDate.parse(date, formatter);

		List<Appointment> apps = emm.createQuery("Select a from Appointment a where a.date =:date")
				.setParameter("date", d).getResultList();
		HashMap<String, List<LocalTime>> mapHours = new HashMap<>();
		for(Appointment a : apps) {
			mapHours.put(a.getUser().getUsername(), new ArrayList<LocalTime>());
			mapHours.get(a.getUser().getUsername()).add(a.getHour());
			mapHours.get(a.getUser().getUsername()).add(a.getHour().plusMinutes(30));
			
		}
		return mapHours;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<LocalTime> showFreeHoursForDate(String date) {
		List<LocalTime> hours = new ArrayList<LocalTime>();
		generateHours(hours);
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
		List<LocalTime> hours = new ArrayList<LocalTime>();
		generateHours(hours);
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
	
	public void generateHours(List<LocalTime> hours) {
		
		 DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
		 LocalTime time = LocalTime.parse("10:00", dateTimeFormatter);
		 hours.add(time);
		 LocalTime lastHour = LocalTime.parse("18:00", dateTimeFormatter);
		while(time.compareTo(lastHour) != 0) {
			time = time.plusMinutes(30);
			hours.add(time);
		}
		
	}
	
	public HashMap<LocalDate, List<LocalTime>> generateDates() {
		LocalDate date = LocalDate.now();
		List<LocalTime> hours = new ArrayList<LocalTime>();
		generateHours(hours);
		map.put(date, hours);
		for(int i = 0; i < 6; i++) {
			date = date.plusDays(1);
			map.put(date, hours);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<AppointmentDTO> AppointedHoursForThisWeek() {
		
		LocalDate d = LocalDate.now();
		while(d.getDayOfWeek().toString()!="MONDAY") {
			d = d.minusDays(1);
		}
		List<AppointmentDTO> hours = new ArrayList<AppointmentDTO>();
		List<Appointment> apps = new ArrayList<Appointment>();
		for(int i = 0; i < 6; i++) {
			
			apps = emm.createQuery("Select a from Appointment a where a.date =:date")
						.setParameter("date", d).getResultList();
			
			for(Appointment a : apps) {
				AppointmentDTO temp = new AppointmentDTO();
				LocalDateTime datetime = LocalDateTime.of(d.getYear(), d.getMonth(), d.getDayOfMonth(), a.getHour().getHour(), a.getHour().getMinute(), a.getHour().getSecond());
				
				temp.setTitle(a.getUser().getUsername());
				temp.setStart(datetime);
				temp.setEnd(datetime.plusMinutes(30));
				hours.add(temp);
			}
			d = d.plusDays(1);
		}
		
		return hours;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AppointmentDTO> AppointedHoursForNextWeek() {
		
		LocalDate d = LocalDate.now();
		while(d.getDayOfWeek().toString()!="MONDAY") {
			d = d.plusDays(1);
		}
		List<AppointmentDTO> hours = new ArrayList<AppointmentDTO>();
		List<Appointment> apps = new ArrayList<Appointment>();
		for(int i = 0; i < 6; i++) {
			
			apps = emm.createQuery("Select a from Appointment a where a.date =:date")
						.setParameter("date", d).getResultList();
			
			for(Appointment a : apps) {
				AppointmentDTO temp = new AppointmentDTO();
				LocalDateTime datetime = LocalDateTime.of(d.getYear(), d.getMonth(), d.getDayOfMonth(), a.getHour().getHour(), a.getHour().getMinute(), a.getHour().getSecond());
				
				temp.setTitle(a.getUser().getUsername());
				temp.setStart(datetime);
				temp.setEnd(datetime.plusMinutes(30));
				hours.add(temp);
			}
			d = d.plusDays(1);
		}
		
		return hours;
		
	}

}
