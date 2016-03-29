package com.mawujun.repository.test;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name="t_EntityTest")
public class EntityTest { //extends UUIDEntity{//extends AutoIdEntity {

	@Id
	@GeneratedValue
	public UUID id;

	private String firstName;
	private String lastName;
	private Integer age;
	

	private Boolean sex;
	@Email
	private String email;
	@Version
	private int version;
	

//	@ManyToOne(fetch=FetchType.LAZY)
//	private Parent parent;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

//	public Parent getParent() {
//		return parent;
//	}
//	public void setParent(Parent parent) {
//		this.parent = parent;
//	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}


}
