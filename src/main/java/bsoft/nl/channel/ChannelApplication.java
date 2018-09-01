package bsoft.nl.channel;

import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChannelApplication {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    MessageRepository messageRepository;

	public static void main(String[] args) {

	    SpringApplication.run(ChannelApplication.class, args);
	}
}
