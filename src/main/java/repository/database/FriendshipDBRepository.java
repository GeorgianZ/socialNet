package repository.database;

import domain.Friendship;
import domain.Tuple;
import validate.Validator;

import java.sql.*;
import java.util.ArrayList;
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
                if(id_user1 < id_user2)
                    friendship = new Friendship(id_user1,id_user2);
                else
                    friendship = new Friendship(id_user2,id_user1);
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
        String sql = "insert into friendship (user1, user2, datetime) values (?, ?, ?)";
        try (Connection connection = getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            if(entity.getUser1() < entity.getUser2()){
                ps.setLong(1, entity.getUser1());
                ps.setLong(2, entity.getUser2());
                ps.setString(3, String.valueOf(entity.getDateTime()));
            }
            else {
                ps.setLong(2, entity.getUser1());
                ps.setLong(1, entity.getUser2());
                ps.setString(3, String.valueOf(entity.getDateTime()));
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

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }

    @Override
    public Friendship findByUsernameAndPassword(String username, String password) {
        return null;
    }
}
