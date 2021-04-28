package com.bot.insched.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="discord_user")
@Data
@Getter
@Setter
@NoArgsConstructor
public class DiscordUser {

    @Id
    @Column(name = "id_discord")
    private String idDiscord;

    @Column(name = "google_token")
    private String googleToken;

    @OneToMany
    @Column(name = "list_appointment")
    private List<Appointment> listAppointment;

    public DiscordUser(String idDisc, String googleToken) {
        this.idDiscord = idDisc;
        this.googleToken = googleToken;
    }




}
