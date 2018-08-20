package com.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
public class Appointment extends BaseEntity {

	@Column
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate date;
	
	@Column
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalTime hour;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public LocalTime getHour() {
		return hour;
	}
	
	public void setHour(LocalTime hour) {
		this.hour = hour;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}