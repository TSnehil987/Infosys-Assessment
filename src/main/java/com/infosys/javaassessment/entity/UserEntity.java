package com.infosys.javaassessment.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -1605494281912662919L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable=false, length=50)
	private String firstName;
	
	@Column(nullable=false, length=50)
	private String lastName;

	@Column(nullable=false, length=100, unique=true)
	private String email;

	@Column(nullable=false, unique=true)
	private String userId;

	@Column(nullable=false, unique=true)
	private String encryptedPassword;
}