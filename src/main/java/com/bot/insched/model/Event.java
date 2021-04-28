package com.bot.insched.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="event")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @Column(name="id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idEvent;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name="id_appointment")
    private Appointment appointment;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "capacity")
    private int capacity;

}
