package com.airtribe.librarymanagement.service;

import com.airtribe.librarymanagement.exception.LibraryException;
import com.airtribe.librarymanagement.model.Reservation;
import com.airtribe.librarymanagement.pattern.NotificationObserver;
import com.airtribe.librarymanagement.pattern.NotificationService;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReservationService {
    private static final Logger logger = Logger.getLogger(ReservationService.class.getName());
    private Map<String, ArrayList<Reservation>> reservationsByIsbn;
    private List<NotificationObserver> observers;
    private NotificationService notificationService;

    public ReservationService() {
        this.reservationsByIsbn = new HashMap<>();
        this.observers = new ArrayList<>();
        this.notificationService = new NotificationService();
        registerObserver(this.notificationService);
    }

    public String makeReservation(String patronId, String isbn) throws LibraryException {
        String reservationId = "R" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        int priority = getNextPriority(isbn);

        Reservation reservation = new Reservation(reservationId, patronId, isbn, priority);
        reservationsByIsbn.putIfAbsent(isbn, new ArrayList<>());
        reservationsByIsbn.get(isbn).add(reservation);
        sortReservations(isbn);

        logger.info("Reservation created: " + reservationId + " for patron: " + patronId);
        return reservationId;
    }

    public void cancelReservation(String reservationId) throws LibraryException {
        for (ArrayList<Reservation> list : reservationsByIsbn.values()) {
            Reservation reservation = list.stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElse(null);

            if (reservation != null) {
                list.remove(reservation);
                reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
                logger.info("Reservation cancelled: " + reservationId);
                return;
            }
        }
        throw new LibraryException("Reservation not found: " + reservationId);
    }

    public void registerObserver(NotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer registered: " + observer.getClass().getSimpleName());
        }
    }

    public void unregisterObserver(NotificationObserver observer) {
        if (observers.remove(observer)) {
            logger.info("Observer unregistered: " + observer.getClass().getSimpleName());
        }
    }

    public void notifyReservationFulfilled(String isbn) {
        ArrayList<Reservation> list = reservationsByIsbn.get(isbn);
        if (list != null && !list.isEmpty()) {
            Reservation nextReservation = list.get(0);
            list.remove(0);
            if (nextReservation != null) {
                nextReservation.setStatus(Reservation.ReservationStatus.FULFILLED);
                String message = "Your reservation for ISBN " + isbn + " is now available!";
                for (NotificationObserver observer : observers) {
                    observer.update(nextReservation.getPatronId(), message);
                }
                logger.info("Notification sent for fulfilled reservation: " + nextReservation.getReservationId());
            }
        }
    }

    public List<Reservation> getReservationsForBook(String isbn) {
        ArrayList<Reservation> list = reservationsByIsbn.get(isbn);
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }

    public List<Reservation> getReservationsByPatron(String patronId) {
        List<Reservation> result = new ArrayList<>();
        for (ArrayList<Reservation> list : reservationsByIsbn.values()) {
            result.addAll(list.stream()
                    .filter(r -> r.getPatronId().equals(patronId))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    public Reservation getReservation(String reservationId) {
        for (ArrayList<Reservation> list : reservationsByIsbn.values()) {
            Reservation reservation = list.stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElse(null);
            if (reservation != null) {
                return reservation;
            }
        }
        return null;
    }

    public List<String> getPatronNotifications(String patronId) {
        return notificationService.getNotifications(patronId);
    }

    public void clearPatronNotifications(String patronId) {
        notificationService.clearNotifications(patronId);
    }

    private int getNextPriority(String isbn) {
        ArrayList<Reservation> list = reservationsByIsbn.get(isbn);
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return list.size() + 1;
    }

    private void sortReservations(String isbn) {
        ArrayList<Reservation> list = reservationsByIsbn.get(isbn);
        if (list != null) {
            list.sort(null);
        }
    }
}
