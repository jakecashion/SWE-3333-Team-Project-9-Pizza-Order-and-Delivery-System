package com.stackunderflow.pizzasystem.util;

public class PasswordHasher {
    public static String hash(String rawPassword) { return "hashed_" + rawPassword + "_salt"; }
    public static boolean check(String rawPassword, String storedHash) { return ("hashed_" + rawPassword + "_salt").equals(storedHash); }
}