package com.bot.insched.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    public Event(String startTime, int duration, int capacity, String desc) {
        this.startTime = LocalDateTime.parse(startTime);
        this.endTime = this.startTime.plusMinutes(duration);
        this.capacity = capacity;
        this.isAvailable = true;
        this.description = desc;
        this.listAttendee = new ArrayList<>();
    }

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID idEvent;

    @Column(name = "google_event_id")
    private String idGoogleEvent;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private int duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;

    @Column(name = "is_available")
    private boolean isAvailable;

    @Column(name = "capacity")
    private int capacity;

    @ManyToMany(mappedBy = "listEvent", fetch = FetchType.EAGER)
    private List<DiscordUser> listAttendee;

    public void updateAvailability() {
        if (this.capacity > listAttendee.size()) {
            isAvailable = true;
            return;
        }
        isAvailable = false;
    }

    public String getStatusBooking() {
        if (listAttendee != null && listAttendee.size() > 0) {
            return "Telah dibooking oleh " + listAttendee.size() + " orang";
        }
        return "Belum ada yang booking";
    }

    public String getWaktu() {
        return getWaktuMulai() + " - " + getWaktuSelesai();
    }

    private String getWaktuMulai() {
        return startTime.getHour() + ":" + startTime.getMinute();
    }

    private String getWaktuSelesai() {
        return endTime.getHour() + ":" + endTime.getMinute();
    }

    public LocalDate getTanggal() {
        return startTime.toLocalDate();
    }





    // HACK: Lombok does not generate this for some reason
    public boolean isAvailable() {
        return this.isAvailable;
    }

}
