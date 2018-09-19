package bsoft.nl.channel.repositories;

import bsoft.nl.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


//public interface ChannelRepository extends JpaRepository<Channel, Integer> {
public interface ChannelRepository extends PagingAndSortingRepository<Channel, Integer> {


    public final static String FIND_ALL_CHANNELS_ORDEREDBY_NAME_QUERY = "SELECT c " +
            "FROM Channel c " +
            "ORDER BY c.name";

    public final static String FIND_CHANNELS_BY_NAME_QUERY = "SELECT c " +
            "FROM Channel c " +
            "WHERE c.name = :name";

    @Query(FIND_ALL_CHANNELS_ORDEREDBY_NAME_QUERY)
    public List<Channel> findAll();

    @Query(FIND_CHANNELS_BY_NAME_QUERY)
    public List<Channel> findChannelByName(@Param("name") String name);

}
