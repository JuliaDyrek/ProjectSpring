package com.projektRestauracja.Projekt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projektRestauracja.Projekt.model.Reservation;
import com.projektRestauracja.Projekt.repository.ReservationRepository;
import com.projektRestauracja.Projekt.repository.OrderRepository;
import com.projektRestauracja.Projekt.config.SessionUtils;
import com.projektRestauracja.Projekt.model.CustomerOrder;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String showReservationsPage(Model model, HttpSession session) {
        String loggedUser = (String) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedUser", loggedUser);
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("reservations", reservations);
        return "reservations/index";
    }

    @PostMapping
    public String createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        return "redirect:/api/reservations";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        for (CustomerOrder order : reservation.getOrders()) {
            orderRepository.delete(order);
        }

        reservationRepository.delete(reservation);
        return "redirect:/api/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model, HttpSession session) {
        if (!SessionUtils.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        model.addAttribute("reservation", reservation);
        return "reservations/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateReservation(@PathVariable Long id, Reservation updatedReservation) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setName(updatedReservation.getName());
        reservation.setDate(updatedReservation.getDate());
        reservation.setTime(updatedReservation.getTime());
        reservation.setNumberOfPeople(updatedReservation.getNumberOfPeople());
        reservationRepository.save(reservation);
        return "redirect:/api/reservations";
    }
}
