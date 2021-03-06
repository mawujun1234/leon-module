package com.mawujun.repository;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.Email;

import com.mawujun.repository.idEntity.AutoIdEntity;
import com.mawujun.repository.idEntity.UUIDEntity;

@Entity//(name="t_EntityTest")
@Table(name="t_EntityTest")
public class EntityTest extends AutoIdEntity {
	private String firstName;
	private String lastName;
	private Integer age;
	
	@org.hibernate.annotations.Type(type="yes_no")
	@Column(length=1)
	private Boolean sex;
	@Email
	private String email;
	@Version
	private int version;
	
	@Embedded
	private Address address;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Parent parent;
	
	
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
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
	public Parent getParent() {
		return parent;
	}
	public void setParent(Parent parent) {
		this.parent = parent;
	}

}
