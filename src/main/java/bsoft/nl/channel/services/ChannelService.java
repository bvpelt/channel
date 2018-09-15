package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.repositories.ChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ChannelService {
    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    private ChannelRepository channelRepository = null;

    @Autowired
    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    /*
    public List<Channel> getAll() {
        return channelRepository.findAll();
    }
   */
    public Iterable<Channel> getAll() {
        return channelRepository.findAll();
    }
}
