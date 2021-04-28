package com.bot.insched.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class DiscordUser {

    @Id
    @Column(name = "id_discord")
    private String id_discord;

    @Column(name = "google_token")
    private String googleToken;

    public DiscordUser(String idDisc, String googleToken) {
        this.id_discord = idDisc;
        this.googleToken = googleToken;
    }


}
