package me.pk2.adminsecure.command.exception;

public class CommandErrorException extends Exception {
    private String message;
    public CommandErrorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() { return message; }
}