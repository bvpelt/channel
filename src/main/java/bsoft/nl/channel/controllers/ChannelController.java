package bsoft.nl.channel.controllers;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.domain.ChannelsList;
import bsoft.nl.channel.services.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ChannelController extends ResourceSupport {
    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    private ChannelService channelService = null;

    @Autowired
    public ChannelController(final ChannelService channelService) {
        this.channelService = channelService;
    }


    @GetMapping("/channels")
    public ResponseEntity<ChannelsList> getChannels() {
        logger.info("Get channel list");

        Iterable<Channel> channelList = channelService.getAll();

        ChannelsList channelResult = new ChannelsList();

        for (Channel channel : channelList) {
            Link link = ControllerLinkBuilder
                    .linkTo(ControllerLinkBuilder
                            .methodOn(ChannelController.class).getChannels())
                    .slash(channel.getChannelId())
                    .withSelfRel();

            channel.add(link);

            channelResult.getChannel().add(channel);
        }

        //Adding self link channels collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(ChannelController.class).getChannels())
                .withSelfRel();


        channelResult.add(selfLink);

        logger.info("Result: {}", channelResult.toString());

        return new ResponseEntity<ChannelsList>(channelResult, HttpStatus.OK);
    }

    /*
    POST - Create new channel
     */
    @PostMapping("/channels")
    public ResponseEntity<Channel> createChannel(@RequestBody final Channel channel) {
        logger.info("Create channel for: {}", channel);

        Channel savedChannel = channelService.create(channel);

        if (null == savedChannel) {
            return new ResponseEntity<Channel>(savedChannel, HttpStatus.NOT_MODIFIED);
        } else {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedChannel.getChannelId()).toUri();

            logger.info("Channel id: " + savedChannel.getChannelId() + " saved at uri: {}", location.toString());

            return new ResponseEntity<Channel>(savedChannel, HttpStatus.CREATED);
        }
    }

    /*
   Delete - Delete an existing channel
    */
    @DeleteMapping("/channels/{channelName}")
    public ResponseEntity<Channel> deleteChannel(@PathVariable String channelName) {
        logger.info("Delete channel for: {}", channelName);
        Channel savedChannel = null;
        boolean result = channelService.delete(channelName);

        if (result) {
            return new ResponseEntity<Channel>(savedChannel, HttpStatus.OK);
        } else {
            return new ResponseEntity<Channel>(savedChannel, HttpStatus.NOT_MODIFIED);
        }
    }

}
