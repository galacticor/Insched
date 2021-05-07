package com.bot.insched.repository;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Appointment findByIdAppointment(UUID id);

    void deleteByIdAppointment(String id);

    Appointment findAppointmentByOwner(DiscordUser owner);

}
