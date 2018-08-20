package com.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.model.User;

@Service
@Transactional
public class UserService {

	@PersistenceContext
	EntityManager em;

	public void register(String username, String password, String firstName, String lastName, String phone) {
		User u = new User();
		u.setUsername(username);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setPhone(phone);
		em.persist(u);

	}

	public List<User> getAllUsers() {
		return em.createQuery("SELECT u FROM User u").setMaxResults(100).getResultList();
	}
}
