package com.cruru.process.domain;

import com.cruru.dashboard.domain.Dashboard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Long id;

    private Integer sequence;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    public Process(int sequence, String name, String description, Dashboard dashboard) {
        this.sequence = sequence;
        this.name = name;
        this.description = description;
        this.dashboard = dashboard;
    }

    public void increaseSequenceNumber() {
        this.sequence++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Process process = (Process) o;
        return Objects.equals(id, process.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dashboard=" + dashboard +
                '}';
    }
}
