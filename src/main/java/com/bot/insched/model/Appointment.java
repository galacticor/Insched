package com.bot.insched.model;

import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "appointment")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idAppointment;

    @OneToMany(mappedBy = "appointment", fetch = FetchType.EAGER)
    @Column(name = "list_event")
    private List<Event> listEvent;

    @OneToOne
    private DiscordUser owner;

    // Non-mandatory one-to-one relation
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "appointment_booking",
        joinColumns =
                {@JoinColumn(name = "appointment_id", referencedColumnName = "id")},
        inverseJoinColumns =
                {@JoinColumn(name = "booking_id", referencedColumnName = "id")})
    private Booking booking;
}
