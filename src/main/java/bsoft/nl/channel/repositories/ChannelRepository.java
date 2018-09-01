package bsoft.nl.channel.repositories;

import bsoft.nl.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface ChannelRepository extends JpaRepository<Channel, Integer> {
}
