package bsoft.nl.channel.repositories;

import bsoft.nl.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


//public interface ChannelRepository extends JpaRepository<Channel, Integer> {
public interface ChannelRepository extends PagingAndSortingRepository<Channel, Integer> {
}
