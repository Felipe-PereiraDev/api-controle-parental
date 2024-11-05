package br.fametro.controle_parental.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public String EnviarVerificacaoEmail(String to, String token) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();

            mensagem.setTo(to);
            mensagem.setSubject("Verificação de Email");
            mensagem.setText("Por favor clique no link para verificar seu email:\n" +
                    "http://localhost:8080/verify?token=" + token);
            javaMailSender.send(mensagem);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao tentar enviar email "
                    + e.getLocalizedMessage();
        }
    }

    public String EnviarVerificacaoEmailFilho(String to, String token, String msg) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();

            mensagem.setTo(to);
            mensagem.setSubject("Verificação de Email");
            mensagem.setText(msg + "http://localhost:8080/verify/f?token=" + token);
            javaMailSender.send(mensagem);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao tentar enviar email "
                    + e.getLocalizedMessage();
        }
    }

    public String EnviarVerificacaoEmailJuntarFamilia(String to, String token, String msg) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();

            mensagem.setTo(to);
            mensagem.setSubject("Verificação de Email");
            mensagem.setText(msg + "http://localhost:8080/verify/f/add-user?token=" + token);
            javaMailSender.send(mensagem);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao tentar enviar email "
                    + e.getLocalizedMessage();
        }
    }

    public void enviarEmailComAnexo(String destinatario, String assunto, String mensagem, byte[] pdfData) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(mensagem);

        // Adicionando o PDF como anexo
        helper.addAttachment("relatorio.pdf", new ByteArrayDataSource(pdfData, "application/pdf"));

        // Enviar o e-mail
        javaMailSender.send(mimeMessage);
        System.out.println("E-mail com anexo enviado com sucesso!");
    }

}
