package com.projektRestauracja.Projekt.config;

import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }
}
