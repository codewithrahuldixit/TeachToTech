package com.rahul.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long assignmentId;

	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	@JsonBackReference
	private Topic topic;

	@ElementCollection
	@CollectionTable(name = "assignment_qa", joinColumns = @JoinColumn(name = "assignment_id"))

	@Column(name = "qa", columnDefinition = "TEXT")
	private List<String> qa;

}
