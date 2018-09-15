package bsoft.nl.channel.services;

import bsoft.nl.channel.domain.Message;
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

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /*
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
*/

    public Iterable<Message> getAll() {
        return messageRepository.findAll();
    }

    public List<Message> getMessageByChannelName(final String channelName) {
        return messageRepository.findMessageByChannelName(channelName);
    }
}
