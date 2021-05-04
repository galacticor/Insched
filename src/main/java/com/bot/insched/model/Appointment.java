package com.bot.insched.model;


import jdk.vm.ci.meta.Local;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    public Appointment(String desc, LocalDate startDate, LocalDate endDate) {
        this.description = desc;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idAppointment;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "appointment", fetch = FetchType.EAGER)
    @Column(name = "list_event")
    private List<Event> listEvent;

    @ManyToOne
    @JoinColumn(name = "appointment_owner")
    private DiscordUser owner;

    @Override
    public String toString() {
        return String.format("%s %s %s %s", description, startDate.toString(),endDate.toString(),idAppointment.toString());
    }
}
