package com.sanosysalvos.notificaciones.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String destino, String asunto, String mensaje) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<h3>" + asunto + "</h3>"
                    + "<p>" + mensaje + "</p>"
                    + "<br><br><p>Atentamente,<br>El equipo de Sanos y Salvos</p>";

            helper.setText(htmlMsg, true); // true indicates HTML
            helper.setTo(destino);
            helper.setSubject("Sanos y Salvos: " + asunto);
            helper.setFrom("sanosysalvos@noreply.com");

            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente a {}", destino);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo a {}: {}", destino, e.getMessage());
        }
    }
}
