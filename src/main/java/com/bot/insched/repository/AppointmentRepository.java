package com.bot.insched.repository;

import com.bot.insched.model.Appointment;

import com.bot.insched.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    Appointment findByIdAppointment(String id);
    void deleteByIdAppointment(String id);
    List<Appointment> findAllByOwner(DiscordUser owner);



}
