package com.bot.insched.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "appointment")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    public Appointment(String desc, String date) {
        this.description = desc;
        this.date = LocalDate.parse(date);
    }

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    String id_appointment;

    @Column(name = "description")
    String description;

    @Column(name = "date")
    LocalDate date;

//    @OneToMany(mappedBy = "appointment")
//    @Column(name = "list_event")
//    List<Event> listEvent;
}
