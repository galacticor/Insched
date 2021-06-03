package com.bot.insched.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/*


    Deprecated. Do not use


 */

@Entity
@Table(name = "booking")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "appointment")
    private Appointment appointment;

    @Column(name = "event")
    private Event event;

    @ManyToOne
    @Column(name = "requester")
    private DiscordUser requester;

    @Override
    public String toString() {
        return String.format("%s: %s %s %s",
                requester.getIdDiscord(), appointment.getIdAppointment(), event.getTanggal(), event.getWaktu());
    }

}
