package bsoft.nl.channel;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.repositories.ChannelRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ChannelRepositoryIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositoryIntegrationTest.class);

    @Rule
    public TestName name = new TestName();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    public ChannelRepositoryIntegrationTest() {

    }

    @Test
    public void getAllChannels() {
        logger.info("Start test: {}", name.getMethodName());
        // get list of all known channels
        List<Channel> channels = channelRepository.findAll();
        assertThat(channels).isNotNull();
        assertThat(channels.size()).isEqualTo(12);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getChannelByName() {
        logger.info("Start test: {}", name.getMethodName());
        String channelName = "Nederland";

        // Find channel
        List<Channel> channels = channelRepository.findChannelByName(channelName);

        // Check that channel exists by checking the list
        assertThat(channels).isNotNull();
        assertThat(channels.size()).isEqualTo(1);
        // Anc checking the content of the list
        Channel channel = channels.get(0);
        assertThat(channel).isNotNull();
        assertThat(channel.getName()).isEqualTo(channelName);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void deleteChannelByName() {
        logger.info("Start test: {}", name.getMethodName());
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

        // Delete channel
        channelRepository.deleteChannelByName(channelName);

        // Check that channel is deleted
        channels = channelRepository.findChannelByName(channelName);
        assertThat(channels).isNotNull();
        assertThat(channels.size()).isEqualTo(0);
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getAllChannelsByPage() {
        logger.info("Start test: {}", name.getMethodName());
        // get list of all known channels
        int PAGESIZE = 2;
        for (int pageNumber = 1; pageNumber < 4; pageNumber++) {
            PageRequest request = PageRequest.of(pageNumber - 1, PAGESIZE, Sort.Direction.ASC, "name");

            List<Channel> channels = channelRepository.findAll(request).getContent();

            assertThat(channels).isNotNull();
            assertThat(channels.size()).isEqualTo(PAGESIZE);
        }
        logger.info("End   test: {}", name.getMethodName());
    }

}
