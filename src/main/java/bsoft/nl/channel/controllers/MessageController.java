package bsoft.nl.channel.controllers;

import bsoft.nl.channel.domain.Message;
import bsoft.nl.channel.domain.MessagesList;
import bsoft.nl.channel.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin(origins = "*",
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET,
                RequestMethod.DELETE,
                RequestMethod.POST,
                RequestMethod.OPTIONS
        })
@RestController
public class MessageController extends ResourceSupport {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private MessageService messageService = null;

    @Autowired
    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages/channel/{channelName}")
    public ResponseEntity<MessagesList> getMessagesByChannelName(@PathVariable String channelName) {
        logger.info("Get message by name: {}", channelName);

        List<Message> messageList = messageService.getMessageByChannelName(channelName);

        MessagesList messageResult = new MessagesList();

        for (Message message : messageList) {
            if (message.getLinks().size() == 0) {  // result can be cached with link already added
                Link link = ControllerLinkBuilder
                        .linkTo(ControllerLinkBuilder
                                .methodOn(MessageController.class).getMessages())
                        .slash(message.getMessageId())
                        .withSelfRel();

                message.add(link);
            }

            messageResult.getMessage().add(message);
        }

        //Adding self link message collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(MessageController.class).getMessages())
                .withSelfRel();

        messageResult.add(selfLink);

        logger.info("Result: {}", messageResult.toString());

        return new ResponseEntity<MessagesList>(messageResult, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<MessagesList> getMessages() {
        logger.info("Get message list");

        Iterable<Message> messageList = messageService.getAll();

        MessagesList messageResult = new MessagesList();

        for (Message message : messageList) {
            Link link = ControllerLinkBuilder
                    .linkTo(ControllerLinkBuilder
                            .methodOn(MessageController.class).getMessages())
                    .slash(message.getMessageId())
                    .withSelfRel();

            message.add(link);

            messageResult.getMessage().add(message);
        }

        //Adding self link message collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(MessageController.class).getMessages())
                .withSelfRel();

        messageResult.add(selfLink);

        logger.info("Result: {}", messageResult.toString());

        return new ResponseEntity<MessagesList>(messageResult, HttpStatus.OK);
    }

    /*
   POST - Create new message
    */
    @PostMapping("/messages/channel/{channelName}")
    public ResponseEntity<Message> creatMessage(@PathVariable String channelName, @RequestBody final Message message) {
        logger.info("Create message for channel: {}, message: {}", message);

        if (message.getDateTime() == null) {
            message.setDateTime(LocalDateTime.now());
        }

        Message savedMessage = messageService.create(channelName, message);

        if (null == savedMessage) {
            return new ResponseEntity<Message>(savedMessage, HttpStatus.NOT_MODIFIED);
        } else {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedMessage.getMessageId()).toUri();

            logger.info("Message id: " + savedMessage.getMessageId() + " saved at url: {}", location.toString());

            return new ResponseEntity<Message>(savedMessage, HttpStatus.CREATED);
        }
    }

}
