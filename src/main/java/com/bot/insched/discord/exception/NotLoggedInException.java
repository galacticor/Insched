package com.bot.insched.discord.exception;

public class NotLoggedInException extends Exception {

    public NotLoggedInException() {
        super("Kamu belum login. Silahkan login terlebih dahulu!");
    }
}
