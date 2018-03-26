package com.wavecheng.sing.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.mockito.internal.verification.Times;

@Entity
public class Category {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private int maxCount = 30;
	private boolean active = true;
	private Timestamp beginTime = Timestamp.from(Calendar.getInstance().toInstant());
	private Timestamp updateTime = Timestamp.from(Calendar.getInstance().toInstant());
	private Timestamp addedTime = Timestamp.from(Calendar.getInstance().toInstant());
	
	@OneToMany(mappedBy="category")
	private List<Attendee> attendees;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Timestamp getAddedTime() {
		return addedTime;
	}
	public void setAddedTime(Timestamp addedTime) {
		this.addedTime = addedTime;
	}
	public List<Attendee> getAttendees() {
		return attendees;
	}
	public void setAttendees(List<Attendee> attendees) {
		this.attendees = attendees;
	}
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}			
}
