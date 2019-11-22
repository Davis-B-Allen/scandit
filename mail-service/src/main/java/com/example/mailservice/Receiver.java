package com.example.mailservice;

import static com.example.mailservice.config.RabbitMQConfig.QUEUE_NAME;

import com.example.mailservice.client.UserClient;
import com.example.mailservice.mailer.Mailer;
import com.example.mailservice.responseobject.Comment;
import com.example.mailservice.responseobject.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RabbitListener(queues = QUEUE_NAME)
public class Receiver {

    @Autowired
    UserClient userClient;

    @Autowired
    Mailer mailer;

    @RabbitHandler
    public void receive(String msg) throws JsonProcessingException {
        try {
            System.out.println(msg);
            System.out.println("WE SHOULD NOW EMAIL THIS MESSAGE SOMEHOW");
            ObjectMapper objectMapper = new ObjectMapper();
            Comment comment = objectMapper.readValue(msg, Comment.class);
            System.out.println(comment);
            if (comment != null) {
                String username = comment.getPost().getUser().getUsername();
                if (username != null && !comment.getUser().getUsername().equals(username)) {
                    User op = userClient.getUserByUsername(username);
                    if (op != null) {
                        System.out.println(op.getEmail());
                        mailer.sendEmail(op.getEmail(), comment.getUser().getUsername(), comment.getText(), comment.getPost().getTitle());
                    }
                }
            }
        } catch (Exception e) {
//            System.out.println(e.getStackTrace());
            System.out.println("!!!!---");
            System.out.println("MAILING FAILED BECAUSE: Exception: " + e.getMessage());
            System.out.println("!!!!---");
        }
    }
}
