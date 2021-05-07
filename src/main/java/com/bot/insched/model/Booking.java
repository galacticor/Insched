package com.bot.insched.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

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
    @Column(name = "bid", updatable = false, nullable = false)
    private UUID bid;

    @Column(name = "btitle")
    private String btitle;

    @Column(name = "bdesc")
    private String bdesc;

    @OneToOne(mappedBy = "booking")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "requester")
    private DiscordUser requester;

}
