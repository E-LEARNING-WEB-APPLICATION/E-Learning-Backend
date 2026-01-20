package com.learnease.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "admin_id"))
@ToString(exclude = "createdBy")
@Accessors(chain = true)
public class Admin extends BaseEntity{

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, unique = true)
	@NotNull
	private UserDetails userDetails;

	@ManyToOne
	@JoinColumn(name = "created_by_admin_id")
	private Admin createdBy;
}
