package repository.database;

import domain.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.sql.DriverManager.getConnection;

public class MessageDBRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public int size() {
        AtomicInteger n= new AtomicInteger();
        Iterable<Message> messages = getAll();
        messages.forEach(x-> {
            n.getAndIncrement();});
        return n.get();
    }


    @Override
    public Message find(Long idd) {
        if (idd==null)
            throw new IllegalArgumentException("id must be not null");
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages WHERE id=?")){
            statement.setLong(1, idd.intValue());
            ResultSet resultSet = statement.executeQuery();
            Message message = null;
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String message1 = resultSet.getString("message");
                Long from = resultSet.getLong("from_");
                Long to = resultSet.getLong("to_");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                message = new Message(message1, from, to, date);
                message.setId(id);
            }
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("plm");
            return null;
        }
    }

    @Override
    public Iterable<Message> getAll() {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String message = resultSet.getString("message");
                Long from = resultSet.getLong("from_");
                Long to = resultSet.getLong("to_");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                Message message1 = new Message(message, from, to, date);
                message1.setId(id);
                messages.add(message1);
            }
            return messages;
        } catch (SQLException e) {
            //e.printStackTrace();
            return messages;
        }
    }

    @Override
    public Message save(Message entity) {
        //System.out.println(entity.getLastName());
        String sql = "insert into messages (message, from_, to_, date) values (?, ?, ?, ?)";
        System.out.println(sql);
        try (Connection connection = getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getMessage());
            ps.setLong(2, entity.getFrom());
            ps.setLong(3, entity.getTo());
            ps.setString(4, String.valueOf(entity.getDate()));
            ps.executeQuery();
            return entity;
        } catch (SQLException e) {
            System.out.println("dc nu merge bossule");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message delete(Long aLong) {
        String sql = "delete from messages where id = ?";
        Message message = find(aLong);
        try(Connection connection = getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setLong(1, aLong);
            ps.executeUpdate();
            return message;
        } catch (SQLException throwables) {
            return null;
        }


    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    @Override
    public Message findByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public void update2(Message friendship, Message newFriendship) {

    }

    @Override
    public List<Message> findByUsers(Long id1, Long id2) {
        if (id1 == null || id2 == null)
            throw new IllegalArgumentException("id must be not null");
        List<Message> messages = new ArrayList<>();
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages WHERE from_=? and to_=?")){
            statement.setLong(1, id1);
            statement.setLong(2, id2);
            ResultSet resultSet = statement.executeQuery();
            Message message = null;
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String message1 = resultSet.getString("message");
                Long from = resultSet.getLong("from_");
                Long to = resultSet.getLong("to_");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                message = new Message(message1, from, to, date);
                message.setId(id);
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("plm");
            return null;
        }
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages WHERE from_=? and to_=?")){
            statement.setLong(1, id2);
            statement.setLong(2, id1);
            ResultSet resultSet = statement.executeQuery();
            Message message = null;
            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String message1 = resultSet.getString("message");
                Long from = resultSet.getLong("from_");
                Long to = resultSet.getLong("to_");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));
                message = new Message(message1, from, to, date);
                message.setId(id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("plm");
            return null;
        }
    }


}


