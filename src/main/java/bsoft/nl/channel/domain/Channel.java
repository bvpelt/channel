package bsoft.nl.channel.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Channel extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channelid")
    private int channelId;

    private String name; // max length 24

    public int getChannelId() {
        return channelId;
    }

    public void setChannelid(int id) {
        this.channelId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return (name == null ? "": name);
    }
}
