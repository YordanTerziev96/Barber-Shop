package com.controller;

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

import com.model.User;
import com.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService us;

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = { "username", "password", "firstName",
			"lastName", "phone" })
	@Transactional
	@ResponseBody
	public String registerUser(String username, String password, String firstName, String lastName, String phone) {
		us.register(username, password, firstName, lastName, phone);
		return "Hello " + username + " !!!";
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@Transactional
	@ResponseBody
	public ResponseEntity<?> getAll() {

		return new ResponseEntity<List<User>>(us.getAllUsers(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = {"date", "hour"})
	@Transactional
	@ResponseBody
	public String deleteAppointment(String date, String hour) {
		return us.deleteAppointment(date, hour);
	}
}
