package uz.pdp.lesson5task1.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;

@Component
public class MailSender {

    @Autowired
    JavaMailSender mailSender;

    public boolean send(String to, String text) throws MessagingException {
        String from = "sukhrob@gmail.com";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Confirm email");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(text, true);
        mailSender.send(message);
        return true;
    }

    public boolean mailTextAdd(String email, String code, String pass) throws MessagingException {
        String link = "http:localhost:8080/user/verifyEmail?email=" + email + "&code=" + code;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Emailni tasdiqlash</a>\n" +
                        "<br>\n" +
                        "<p>Parolingiz: <b> " + pass + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"><b>Iltimos uni hech kimga bermang!</b></p>";

        return send(email, text);
    }

    public boolean mailTextEdit(String email) {
        return true;
    }

    public boolean mailTextForGivingTask(String email, String taskName, String taskGiver, String taskDescription, Timestamp deadline) throws MessagingException {
        String link = "http:localhost:8080/task/verifyTask?email=" + email + "&taskName=" + taskName;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Vazifani boshlaganliginggizni tasdiqlang</a>\n" +
                        "<br>\n" +
                        "<p>Vazifa nomi: <b> " + taskName + "</b></p>\n" +
                        "<p>Vazifa izohi: <b> " + taskDescription + "</b></p>\n" +
                        "<p>Vazifa beruvchi: <b> " + taskGiver + "</b></p>\n" +
                        "<p>Vazifa deadline: <b> " + deadline + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"><b>Omad yor bo'lsin!</b></p>";


        return send(email, text);
    }

    public boolean mailForComplete(String email, String taskName, String taskDescription, String taskTaker) throws MessagingException {
        String link = "http:localhost:8080/task/completeTask?taskName=" + taskName;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Maosh kelib tushganligini tasdiqlang</a>\n" +
                        "<br>\n" +
                        "<p> <b> "+taskTaker+"Xodim vazifani a'lo darajada bajardi </b></p>\n" +
                        "<p>Vazifa nomi: <b> " + taskName + "</b></p>\n" +
                        "<p>Vazifa izohi: <b> " + taskDescription + "</b></p>\n" +
                        "<p>Vazifa Bajaruvchi: <b> " + taskTaker + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"><b>Omad yor bo'lsin!</b></p>";


        return send(email, text);
    }
    public boolean mailForConfirmSalary(String email, double amount,String month) throws MessagingException {
        String link = "http:localhost:8080/salary/confirmSalary?email=" + email+"&month="+month;
        String text =
                "<a href=\"" + link + "\" style=\"padding: 10px 15px; background-color: darkslateblue; color: white; text-decoration: none; border-radius: 4px; margin: 10px; display: flex; max-width: 120px;\">Vazifani boshlaganliginggizni tasdiqlang</a>\n" +
                        "<br>\n" +
                        "<p> <b> "+"Sizga oylik maosh berildi tasdiqlang </b></p>\n" +
                        "<p>Maosh miqdori: <b> " + amount + "</b></p>\n" +
                        "<p>Qabul qiluvchi email manzii: <b> " + email + "</b></p>\n" +
                        "<p>Qaysi oy maoshi: <b> " +month + "</b></p>\n" +
                        "<br>\n" +
                        "<p style=\"color: red\"><b>Omad yor bo'lsin!</b></p>";


        return send(email, text);
    }
}
