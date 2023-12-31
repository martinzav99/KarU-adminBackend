package com.ungspp1.gadminbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public Object sendAutentcathionMail(String mail, String code) {
        SimpleMailMessage message = createMessage(mail, code);
        javaMailSender.send(message);
        return null;
    }

    public Object sendTokenMail(String username,String mail, String code) {
        SimpleMailMessage message = recoverPassMessage(username,mail, code);
        javaMailSender.send(message);
        return null;
    }

    private SimpleMailMessage createMessage(String mail, String code){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("losmagiostest@gmail.com");
        message.setTo(mail);
        message.setSubject("KarU: Autenticacion en dos pasos");
        message.setText("Tu codigo de autenticacion para KarU es: "+code);
        return message;
    }

    private SimpleMailMessage recoverPassMessage(String username,String mail,String code){
        String mensaje = String.format("Hola %s, para reestablecer tu contraseña utilice el siguiente codigo:%n%s%n",username ,code);;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("losmagiostest@gmail.com"); 
        message.setTo(mail);
        message.setSubject("KarU: Recuperar contraseña");
        message.setText(mensaje);
        return message;
    }
    
}
