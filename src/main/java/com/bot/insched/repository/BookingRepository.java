//package com.bot.insched.repository;
//
//import com.bot.insched.model.Booking;
//
//import com.bot.insched.model.DiscordUser;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
///*
//
//    Deprecated. Do not use
//
//*/
//
//
//@Repository
//public interface BookingRepository extends JpaRepository<Booking, UUID> {
//    Booking findByBookingId(UUID id);
//    void deleteByBookingId(String id);
//    List<Booking> findAllByUser(DiscordUser user);
//
//}
