package bsoft.nl.channel.domain;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessagesList extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 4L;

    private List<Message> messages = new ArrayList<Message>();

    public List<Message> getMessage() {
        return messages;
    }

    public void setMessage(List<Message> messages) {
        this.messages = messages;
    }
}
