package com.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.boot.model.source.spi.EmbeddableMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.model.Appointment;
import com.model.Authority;
import com.model.User;

@Service
@Transactional
public class UserService {

	@PersistenceContext
	EntityManager em;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public void register(String username, String password, String firstName, String lastName, String phone) {
		
			User u = new User();
			u.setUsername(username);
			u.setFirstName(firstName);
			u.setLastName(lastName);
			u.setPassword(encoder.encode(password));
			u.setPhone(phone);
			u.setEnabled(1);
			em.persist(u);
			
			Authority a = new Authority();
			a.setUsername(username);
			a.setAuthority("USER");
			em.persist(a);
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return em.createQuery("SELECT u FROM User u").setMaxResults(100).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public String deleteAppointment(String date, String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate d = LocalDate.parse(date, formatter);
		LocalTime t = LocalTime.parse(time);
		List<Appointment> a = em.createQuery("Select a from Appointment a where a.date =:date and a.hour =:hour")
				.setParameter("date", d).setParameter("hour", t).getResultList();
		if(a.size() == 0) {
			return "There is no such an appointment !";
		}
		else {
			
			em.remove(a.get(0));
			return "Done !";
		}
	}
}
