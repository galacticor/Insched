package com.bot.insched.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import com.google.api.client.auth.oauth2.StoredCredential;

import javax.persistence.*;
import java.util.List;
import java.time.Instant;

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

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany
    @Column(name = "list_appointment")
    private List<Appointment> listAppointment;

    @ManyToMany
    private List<Event> listEvent;

    public DiscordUser(String idDisc, StoredCredential credential) {
        this.idDiscord = idDisc;
        this.accessToken = credential.getAccessToken();
        this.refreshToken = credential.getRefreshToken();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void apply(StoredCredential credential) {
        this.accessToken = credential.getAccessToken();
        this.refreshToken = credential.getRefreshToken();
        this.updatedAt = Instant.now();
    }
}
