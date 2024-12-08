package ar.uba.fi.ingsoft1.product.services;

import ar.uba.fi.ingsoft1.product.products.Product;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.security.sendgrid.secret-key}")
    private String secretKey;

    @Value("${spring.sendgrid.email.from}")
    private String emailFrom;

    public  void sendEmail(String toEmail, String subject, String content) {

        try {
            Email from = new Email(this.emailFrom);
            Email to = new Email(toEmail);
            Content emailContent = new Content("text/plain", content);
            Mail mail = new Mail(from, subject, to, emailContent);

            SendGrid sg = new SendGrid(this.secretKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);

        } catch (IOException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void main(String[] args) {
        sendEmail("example@domain.com", "Subject of the email", "Body of the email");
    }

    public String generateEmailBodyOrder(String productsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder bodyContent = new StringBuilder("Thank you for your order. Here are the details:\n\n");

        try {
            List<Product> products = objectMapper.readValue(productsJson, new TypeReference<>() {
            });

            for (Product product : products) {
                bodyContent.append("- ").append(product.getName())
                        .append(" (Product name: ").append(product.getName())
                        .append(", Description: ").append(product.getDescription())
                        .append(")\n");
            }
        } catch (Exception e) {
            bodyContent.append("Error loading product details");
        }
        return bodyContent.toString();
    }
}
