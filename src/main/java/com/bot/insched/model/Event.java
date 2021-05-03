package com.bot.insched.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="event")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    public Event(String start_time, int duration, int capacity) {
        this.startTime = LocalDateTime.parse(start_time);
        this.endTime = this.startTime.plusMinutes(duration);
        this.capacity = capacity;
        this.isAvailable = true;
    }

    @Id
    @Column(name="id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String idEvent;

    @Column(name = "google_event_id")
    private String idGoogleEvent;

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

    @ManyToMany(mappedBy = "listEvent")
    private List<DiscordUser> listAttendee;

    public void updateAvailability() {
        if (this.capacity < listAttendee.size()) {
            isAvailable = true;
            return;
        }
        isAvailable = false;
        return;
    }

}
