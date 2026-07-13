package com.sanosysalvos.notificaciones.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void enviarCorreo_shouldSendEmail() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.enviarCorreo("test@test.com", "Prueba", "Mensaje de prueba");

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
