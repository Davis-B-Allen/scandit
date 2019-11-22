package com.example.mailservice.mailer;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Mailer {

    public boolean sendEmail(String toEmail, String commenterUsername, String commentText, String postTitle) {
        Email from = new Email("notifications@scandit.com");
        String subject = "Scandit: " + commenterUsername + " commented on one of your posts!";

//        Email to = new Email(toEmail);
        Email to = new Email("davis.b.allen@gmail.com");

        Content content = new Content("text/plain",
                commenterUsername + " left a message on your post \"" + postTitle + "\".\n\n" +
                commenterUsername + " said: " + "\"" + commentText + "\"\n\n\n\n" +
                "Note: Actual email to which this should be delivered is: " + toEmail);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
        return true;
    }

}
