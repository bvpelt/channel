package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.domain.Message;
import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Transactional
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private MessageRepository messageRepository = null;
    private ChannelRepository channelRepository = null;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }

    public Iterable<Message> getAll() {
        return messageRepository.findAll();
    }

    @Cacheable(cacheNames="allMessages", key="#channelName")
    public List<Message> getMessageByChannelName(final String channelName) {
        return messageRepository.findMessageByChannelName(channelName);
    }

    @CacheEvict(cacheNames="allMessages", key="#channel", allEntries=false)
    public Message create(final String channel, final Message message) {
        Message savedMessage = null;

        List<Channel> existingChannel = null;
        List<Message> existingMessage = null;

        // Find channel id
        // only one can be found!
        existingChannel = channelRepository.findChannelByName(channel);

        if ((existingChannel != null) && (existingChannel.size() == 1)) {

            // Check if message already exists
            message.setChannelId(existingChannel.get(0).getChannelId());
            existingMessage = messageRepository.findMessageByChannelIdAndDateTime(message.getChannelId(), message.getDateTime());

            if ((existingMessage != null) && (existingMessage.size() > 0)) {
                logger.error("Message already exists, id: {}, datatime: {}", message.getMessageId(), message.getDateTime());
            } else {
                savedMessage = messageRepository.save(message);
                logger.info("Persisted new message: {}", savedMessage.getMessageId());
            }
        }
        return savedMessage;
    }

}
