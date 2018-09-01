package bsoft.nl.channel.repositories;

import bsoft.nl.channel.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface MessageRepository extends JpaRepository<Message, Integer> {
}
