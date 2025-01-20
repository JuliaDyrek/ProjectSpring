package com.projektRestauracja.Projekt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projektRestauracja.Projekt.config.SessionUtils;
import com.projektRestauracja.Projekt.model.CustomerOrder;
import com.projektRestauracja.Projekt.model.Dish;
import com.projektRestauracja.Projekt.model.Reservation;
import com.projektRestauracja.Projekt.repository.DishRepository;
import com.projektRestauracja.Projekt.repository.OrderRepository;
import com.projektRestauracja.Projekt.repository.ReservationRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DishRepository dishRepository;

    @GetMapping
    public String showOrdersPage(Model model, HttpSession session) {
        String loggedUser = (String) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedUser", loggedUser);
        Iterable<CustomerOrder> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders/index";
    }

    @GetMapping("/add")
    public String addOrderPage(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null) {
            return "redirect:/login";
        }

        List<Dish> dishes = dishRepository.findAll();
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("dishes", dishes);
        model.addAttribute("reservations", reservations);
        model.addAttribute("order", new CustomerOrder());
        return "orders/add-order";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!SessionUtils.isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        CustomerOrder order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("dishes", dishRepository.findAll());
        model.addAttribute("order", order);
        model.addAttribute("reservations", reservationRepository.findAll());
        return "orders/edit";
    }


    @PostMapping("/add")
    public String saveOrder(
        @RequestParam Long dishId, 
        @RequestParam int quantity, 
        @RequestParam(required = false) Long reservationId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        CustomerOrder order = new CustomerOrder();
        order.setDish(dish);
        order.setQuantity(quantity);

        if (reservationId != null) {
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));
            order.setReservation(reservation);
        } else {
            order.setReservation(null);
        }

        orderRepository.save(order);
        return "redirect:/api/orders";
    }



    @PostMapping("/edit/{id}")
    public String updateOrder(
        @PathVariable Long id,
        @RequestParam Long dishId,
        @RequestParam int quantity,
        @RequestParam(required = false) Long reservationId) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Dish updatedDish = dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dish not found"));

        order.setDish(updatedDish);
        order.setQuantity(quantity);
        if (reservationId != null) {
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));
            order.setReservation(reservation);
        } else {
            order.setReservation(null);
        }

        orderRepository.save(order);
        return "redirect:/api/orders";
    }


    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/api/orders";
    }
}
