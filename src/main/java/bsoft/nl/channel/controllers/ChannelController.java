package bsoft.nl.channel.controllers;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.domain.ChannelsList;
import bsoft.nl.channel.services.ChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelController extends ResourceSupport {
    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    private ChannelService channelService = null;

    /*
    @Autowired
    ChannelRepository repository;
    */

    @Autowired
    public ChannelController(final ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/channels")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ChannelsList> getChannels() {
        logger.info("Get channel list");

        //List<Channel> channelList = channelService.getAll();
        Iterable<Channel> channelList = channelService.getAll();

        ChannelsList channelResult = new ChannelsList();

        for (Channel channel : channelList) {
            Link link = ControllerLinkBuilder.linkTo(ChannelController.class)
                    .slash(channel.getChannelid())
                    .withSelfRel();

            channel.add(link);

            channelResult.getChannel().add(channel);
        }

        //Adding self link accounts collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(ChannelController.class).getChannels())
                .withSelfRel();
        channelResult.add(selfLink);

        logger.info("Result: {}", channelResult.toString());

        return new ResponseEntity<ChannelsList>(channelResult, HttpStatus.OK);
    }
}
