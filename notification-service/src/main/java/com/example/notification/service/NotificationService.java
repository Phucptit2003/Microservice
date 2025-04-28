package com.example.notification.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendConfirmationEmail(String toEmail, String bookingId, String movieName, String showtime, String seats) {
        Email from = new Email("no-reply@cinema.com");
        Email to = new Email(toEmail);
        String subject = "Your Movie Ticket Confirmation";
        String contentText = String.format(
                "Dear Customer,\n\n" +
                        "Your booking has been confirmed!\n\n" +
                        "Booking ID: %s\n" +
                        "Movie: %s\n" +
                        "Showtime: %s\n" +
                        "Seats: %s\n\n" +
                        "Thank you for choosing our cinema!\n" +
                        "Cinema Team",
                bookingId, movieName, showtime, seats
        );
        Content content = new Content("text/plain", contentText);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Email sent with status: " + response.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}