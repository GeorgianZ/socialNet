package repository.database;

import domain.Friendship;
import domain.Tuple;
import validate.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.sql.DriverManager.getConnection;

public class FriendshipDBRepository implements Repository<Tuple<Long,Long>, Friendship>{
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;
    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public int size() {
        AtomicInteger n= new AtomicInteger();
        Iterable<Friendship> users = getAll();
        users.forEach(x-> {
            n.getAndIncrement();});
        return n.get();
    }

    @Override
    public Friendship find(Tuple<Long, Long> idd) {
        if (idd==null)
            throw new IllegalArgumentException("id must be not null");
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * from friendship WHERE user1=? and user2=?")){
            statement.setLong(1, idd.getLeft());
            statement.setLong(2, idd.getRight());
            ResultSet resultSet = statement.executeQuery();
            Friendship fr = null;
            while(resultSet.next()) {
                Long id_user1 = resultSet.getLong("user1");
                Long id_user2 = resultSet.getLong("user2");
                fr = new Friendship(id_user1, id_user2);
            }
            return fr;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Friendship> getAll() {
        ArrayList<Friendship> friendships = new ArrayList<>();
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Long id_user1 = resultSet.getLong("user1");
                Long id_user2 = resultSet.getLong("user2");
                Friendship friendship;
                if(id_user1 < id_user2){
                    friendship = new Friendship(id_user1,id_user2);
                    friendship.setAccepted(resultSet.getBoolean("accepted"));
                }
                else {
                    friendship = new Friendship(id_user2, id_user1);
                    friendship.setAccepted(resultSet.getBoolean("accepted"));
                }
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
            return friendships;
        }
    }

    @Override
    public Friendship save(Friendship entity) {
        String sql = "insert into friendship (user1, user2, datetime, accepted) values (?, ?, ?, ?)";
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if(entity.getUser1() < entity.getUser2()){
                ps.setLong(1, entity.getUser1());
                ps.setLong(2, entity.getUser2());
                ps.setString(3, String.valueOf(entity.getDateTime()));
                ps.setBoolean(4, entity.getAccepted());
            }
            else {
                ps.setLong(2, entity.getUser1());
                ps.setLong(1, entity.getUser2());
                ps.setString(3, String.valueOf(entity.getDateTime()));
                ps.setBoolean(4, entity.getAccepted());
            }
            ps.executeUpdate();
            return entity;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Friendship delete(Tuple<Long, Long> idd) {
        String sql = "delete from friendship where user1 = ? and user2 =?";
        Friendship p = find(idd);
        try(Connection connection = getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            if(idd.getLeft() < idd.getRight()){
                ps.setLong(1, idd.getLeft());
                ps.setLong(2, idd.getRight());}
            else {
                ps.setLong(2, idd.getLeft());
                ps.setLong(1, idd.getRight());}
            ps.executeUpdate();
            return p;
        } catch (SQLException throwables) {
            System.out.println(throwables);
            return null;
        }

    }

    public void update2(Friendship entity, Friendship newEntity) {
        String sql = "UPDATE friendship SET accepted=? WHERE user1=? AND user2=?";
        try (Connection connection = DriverManager.getConnection(url, username, password); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, true);
            statement.setLong(2, entity.getUser1());
            statement.setLong(3, entity.getUser2());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Friendship> findByUsers(Tuple<Long, Long> id1, Tuple<Long, Long> id2) {
        return null;
    }

    public Friendship update(Friendship f){return null;}

    @Override
    public Friendship findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
