package kiv.tut.microservices24.ecommerce.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kiv.tut.microservices24.ecommerce.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kiv.tut.microservices24.ecommerce.email.EmailTemplate.ORDER_CONFIRMATION;
import static kiv.tut.microservices24.ecommerce.email.EmailTemplate.PAYMENT_CONFIRMATION;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    public static final String EMAIL_FROM = "kalinchukihor@gmail.com";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destination,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) {

        // set destination to my email
        destination = EMAIL_FROM;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper =
                    new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
            messageHelper.setFrom(EMAIL_FROM);


            Map<String, Object> variables = new HashMap<>();
            variables.put("customerName", customerName);
            variables.put("amount", amount);
            variables.put("orderReference", orderReference);

            Context context = new Context();
            context.setVariables(variables);

            messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());


            String htmlTemplate = templateEngine.process(PAYMENT_CONFIRMATION.getTemplateName(), context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info(String.format("IHOR:: mail sent to %s with template %s",
                    destination, PAYMENT_CONFIRMATION.getTemplateName()));
        } catch (MessagingException e) {
            log.warn("IHOR WARN:: cannot sent mail to {} with template ", destination);
        }
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destination,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) {

        // set destination to my email
        destination = EMAIL_FROM;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper =
                    new MimeMessageHelper(mimeMessage, MULTIPART_MODE_RELATED, UTF_8.name());
            messageHelper.setFrom(EMAIL_FROM);


            Map<String, Object> variables = new HashMap<>();
            variables.put("customerName", customerName);
            variables.put("totalAmount", amount);
            variables.put("orderReference", orderReference);
            variables.put("products", products);

            Context context = new Context();
            context.setVariables(variables);

            messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

            String htmlTemplate = templateEngine.process(ORDER_CONFIRMATION.getTemplateName(), context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info(String.format("IHOR:: mail sent to %s with template %s",
                    destination, ORDER_CONFIRMATION.getTemplateName()));
        } catch (MessagingException e) {
            log.warn("IHOR WARN:: cannot sent mail to {} with template ", destination);
        }
    }
}
