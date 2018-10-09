package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.repositories.ChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable("allChannels")
    public Iterable<Channel> getAll() {
        return channelRepository.findAll();
    }

    @CacheEvict(cacheNames="allChannels", allEntries=true)
    public Channel create(final Channel channel) {
        Channel savedChannel = null;

        List<Channel> existingChannel = null;

        existingChannel = channelRepository.findChannelByName(channel.getName());

        if ((existingChannel != null) && (existingChannel.size() > 0)) {
            logger.error("Channel already exists, id: {} name:{}", channel.getChannelId(), channel.getName());
        } else {
            savedChannel = channelRepository.save(channel);
            logger.info("Persisted new channel: {}", channel.getChannelId());
        }
        return savedChannel;
    }

}
