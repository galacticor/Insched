package com.bot.insched.repository;

import com.bot.insched.model.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    Appointment findByIdAppointment(String id);
    void deleteByIdAppointment(String id);

}
