package com.proj.expensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data; 
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo { 

	@Id
	private Long id;
	private String username;
	private String email; 
	private String password; 
	private String roles;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Column(name = "update_by")
	private Date updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_at")
	private LocalDateTime updateAt;


	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}

}
