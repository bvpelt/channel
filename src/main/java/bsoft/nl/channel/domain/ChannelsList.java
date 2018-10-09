package bsoft.nl.channel.domain;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChannelsList extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 3L;

    private List<Channel> channels = new ArrayList<Channel>();

    public List<Channel> getChannel() {
        return channels;
    }

    public void setChannel(List<Channel> channels) {
        this.channels = channels;
    }
}
