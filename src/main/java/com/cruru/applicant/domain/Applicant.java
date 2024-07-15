package com.cruru.applicant.domain;

import com.cruru.BaseEntity;
import com.cruru.process.domain.Process;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Applicant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_id")
    private Long id;

    private String name;

    private String email;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    public Applicant(String name, String email, String phone, Process process) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.process = process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Applicant applicant = (Applicant) o;
        return Objects.equals(id, applicant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", process=" + process +
                '}';
    }
}
