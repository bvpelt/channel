package bsoft.nl.channel.controllers;

import bsoft.nl.channel.domain.Message;
import bsoft.nl.channel.domain.MessagesList;
import bsoft.nl.channel.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController extends ResourceSupport {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageRepository repository;

    @GetMapping("/messages")
    @CrossOrigin(origins = "*")
    public ResponseEntity<MessagesList> getMessages() {
        logger.info("Get message list");

        List<Message> messageList = repository.findAll();

        MessagesList messageResult = new MessagesList();

        for (Message message : messageList) {
            Link link = ControllerLinkBuilder.linkTo(MessageController.class)
                    .slash(message.getMessageid())
                    .withSelfRel();

            message.add(link);

            messageResult.getMessage().add(message);
        }

        //Adding self link accounts collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(MessageController.class).getMessages())
                .withSelfRel();
        messageResult.add(selfLink);

        logger.info("Result: {}", messageResult.toString());

        return new ResponseEntity<MessagesList>(messageResult, HttpStatus.OK);
    }
}
