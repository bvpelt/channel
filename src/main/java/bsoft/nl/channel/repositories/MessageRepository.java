package bsoft.nl.channel.repositories;

import bsoft.nl.channel.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


//public interface MessageRepository extends JpaRepository<Message, Integer> {
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {

    public final static String FIND_MESSAGE_BY_CHANNELNAME_QUERY = "SELECT m " +
            "FROM Channel c, Message m " +
            "WHERE m.channelId = c.channelId " +
            "AND c.name = :name " +
            "ORDER BY m.dateTime DESC";

    public final static String FIND_MESSAGE_BY_CHANNELNAME_AND_DATETIME_QUERY = "SELECT m " +
            "FROM Message m " +
            "WHERE m.channelId = :channelid " +
            "AND m.dateTime = :dateTime " +
            "AND m.message = :message";

    @Query(FIND_MESSAGE_BY_CHANNELNAME_QUERY)
    public List<Message> findMessageByChannelName(@Param("name") String name);

    @Query(FIND_MESSAGE_BY_CHANNELNAME_AND_DATETIME_QUERY)
    public List<Message> findMessageByChannelNameAndDateTime(@Param("channelid") int channelid,
                                                             @Param("dateTime") LocalDateTime dateTime,
                                                             @Param("message") String message);

}
