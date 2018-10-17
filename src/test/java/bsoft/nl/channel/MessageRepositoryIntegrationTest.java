package bsoft.nl.channel;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.domain.Message;
import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.repositories.MessageRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepositoryIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryIntegrationTest.class);

    @Rule
    public TestName name = new TestName();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    public MessageRepositoryIntegrationTest() {

    }

    @Test
    public void getAllMessages() {
        logger.info("Start test: {}", name.getMethodName());
        // get list of all known messages
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(33);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getMessageByChannelName() {
        logger.info("Start test: {}", name.getMethodName());
        // get list of all known messages
        String channelName = "Nederland";
        List<Message> messages = messageRepository.findMessageByChannelName(channelName);
        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(32);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getMessageByChannelIdAndDateTime() {
        logger.info("Start test: {}", name.getMethodName());
        // get list of all known messages
        String channelName = "Nederland";
        int channelId = 0;

        // Find channel with specified name
        List<Channel> channels = channelRepository.findChannelByName(channelName);
        assertThat(channels).isNotNull();
        assertThat(channels.size()).isEqualTo(1);

        // Retrieve channel
        Channel channel = channels.get(0);
        assertThat(channel).isNotNull();
        assertThat(channel.getName()).isEqualTo(channelName);

        // Get channel id
        channelId = channel.getChannelId();
        assertThat(channelId).isEqualTo(3);

        // determine date
        LocalDateTime dateTime = LocalDateTime.of(2018, 9, 16, 10, 00);

        // Find existing message with specified channelId and dateTime
        List<Message> messages = messageRepository.findMessageByChannelIdAndDateTime(channelId, dateTime);
        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(1);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void deleteAllMessages() {
        logger.info("Start test: {}", name.getMethodName());
        // Create Channel
        String channelName = "Dit is mijn test";
        List<Channel> channels = null;

        // Define channel
        Channel channel = new Channel();
        channel.setName(channelName);

        // Insert channel
        Channel savedChannel = entityManager.persist(channel);
        assertThat(savedChannel).isNotNull();
        assertThat(savedChannel.getName()).isEqualTo(channelName);

        // Check that channel exists
        channels = channelRepository.findChannelByName(channelName);
        assertThat(channels).isNotNull();
        assertThat(channels.size()).isEqualTo(1);

        int channelId = savedChannel.getChannelId();

        assertThat(channelId).isGreaterThan(0);

        int maxMessages = 10;
        // Add messages
        for (int i = 0; i < maxMessages; i++) {
            String textMessage = "Dit is een test message ";
            LocalDateTime dateTime = LocalDateTime.now();

            Message message = new Message();
            message.setDateTime(dateTime);
            message.setChannelId(channelId);
            message.setMessage(textMessage + i);

            entityManager.persist(message);
        }

        // get list of all known messages
        List<Message> messages = messageRepository.findMessageByChannelName(channelName);
        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(maxMessages);

        // Delete all messages for created channel
        messageRepository.deleteMessageByChannelId(channelId);

        // Check all messages are deleted
        messages = messageRepository.findMessageByChannelName(channelName);
        assertThat(messages).isNotNull();
        assertThat(messages.size()).isEqualTo(0);
        logger.info("End   test: {}", name.getMethodName());
    }
}
