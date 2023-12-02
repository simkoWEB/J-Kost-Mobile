package com.example.j_kost.ForgetPass.OTP;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    private static final String SENDER_EMAIL = "otpjkost@gmail.com"; // Email masih belum menggunakan punya j-kost
    private static final String SENDER_PASSWORD = "gordoreypusvvpbs";
    private static final String EMAIL_SUBJECT = "Kode Verifikasi J-Kost";

    private static Properties properties;

    public EmailSender() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    public static void sendEmail(String recipientEmail, String name, String otp) {
        EmailSender emailSender = new EmailSender();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setSubject(EMAIL_SUBJECT);

            // Masukkan OTP ke dalam pesan email dengan format HTML
            String emailMessageWithOTP = "<html><body><h4>Kepada "+ name + ",</h4><br>"
                    + "<p>Untuk menyelesaikan proses reset password, kami memerlukan verifikasi email Anda dengan memasukkan kode verifikasi berikut:</p><br>"
                    + "<h2>Kode Verifikasi anda : " + otp + "</h2><br>"
                    + "<p>Silakan masukkan kode ini pada halaman verifikasi email. Jika Anda tidak melakukan pengaturan ulang password pada aplikasi Stay Clean, mohon abaikan email ini dan jangan berikan kode verifikasi ini pada pihak manapun.</p><br>"
                    + "<p>Kami mohon untuk tidak memberikan kode verifikasi ini pada orang lain untuk menjaga keamanan akun Anda. Jangan ragu untuk menghubungi kami jika Anda mengalami kesulitan atau memiliki pertanyaan lebih lanjut.</p><br>"
                    + "<p>Terima kasih atas perhatiannya.</p><p>Hormat Kami, J-Kost</p></body></html>";
            mimeMessage.setContent(emailMessageWithOTP, "text/html");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
