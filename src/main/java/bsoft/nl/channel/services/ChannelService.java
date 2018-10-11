package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ChannelService {
    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);

    private ChannelRepository channelRepository = null;
    private MessageRepository messageRepository = null;

    @Autowired
    public ChannelService(ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Cacheable("allChannels")
    public Iterable<Channel> getAll() {

        return channelRepository.findAll();
    }

    public Iterable<Channel> getPage(int pageNumber, int PAGESIZE) {
        PageRequest request = PageRequest.of(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name");

        return channelRepository.findAll(request).getContent();
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

    @CacheEvict(cacheNames="allChannels", allEntries=true)
    public boolean delete(final String channelName) {
        boolean deleted = false;
        List<Channel> existingChannel = null;
        existingChannel = channelRepository.findChannelByName(channelName);

        if ((existingChannel != null) && (existingChannel.size() == 1)) {
            Channel channel = existingChannel.get(0);
            logger.debug("Channel already exists, id: {} name:{}", channel.getChannelId(), channel.getName());
            messageRepository.deleteMessageByChannelId(channel.getChannelId());
            channelRepository.deleteChannelByName(channelName);
            deleted = true;
        } else {
            logger.error("Channel name: {} didnot exist", channelName);
        }
        return deleted;
    }

}
