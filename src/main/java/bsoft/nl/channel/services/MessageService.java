package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.domain.Message;
import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    public List<Message> getMessageByChannelName(final String channelName) {
        return messageRepository.findMessageByChannelName(channelName);
    }

    public Message create(final String channel, final Message message) {
        Message savedMessage = null;

        List<Channel> existingChannel = null;
        List<Message> existingMessage = null;

        // Find channel id
        // only one can be found!
        existingChannel = channelRepository.findChannelByName(channel);

        if ((existingChannel != null) && (existingChannel.size() == 1)) {

            message.setChannelId(existingChannel.get(0).getChannelId());
            existingMessage = messageRepository.findMessageByChannelNameAndDateTime(message.getChannelId(), message.getDateTime(), message.getMessage());

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
