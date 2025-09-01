package org.sid.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fileUrl")
    private String fileUrl;

    @Column(name = "submittedAt")
    private LocalDateTime submittedAt;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;



}
