package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<User> friends;

    private String username;

    private String password;

    /***
     * Constructor
     * @param firstName - String
     * @param lastName - String
     * @param username - String
     * @param password - String
     * */

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.friends = new ArrayList<User>();
    }


    /***
     * Override toString function
     * */
    @Override
    public String toString() {
        return firstName + " " + lastName + "\tUsername: " + username;
    }

    /***
     * return firstName
     * */
    public String getFirstName() {
        return firstName;
    }


    /***
     * set a String value for firstName
     * */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /***
     * return lastName
     * */
    public String getLastName() {
        return lastName;
    }


    /***
     * set a String value for lastName
     * */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /***
     * return list of friends
     * */
    public List<User> getFriends() {
        return friends;
    }


    /***
     * set a list for friends
     * */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    /**
     * @return username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * set username
     * @param username - String
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword(){
        return this.password;
    }

    /**
     * set password
     * @param password - String
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * add friend in friend list
     * @param u - User
     * @return boolean
     */
    public boolean addFriend(User u) {
        if (friends != null) {
            for (User u1 : friends) {
                if (u1 == u)
                    return false;
            }
        }
        this.friends.add(u);
        return true;
    }

    /***
     * delete a friend in friend list
     * @param u - User
     * @return boolean
     */
    public boolean removeFriend(User u) {
        if (friends != null) {
            for (User u1 : friends) {
                if (u1 == u) {
                    friends.remove(u1);
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * Override Equals function
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(friends, user.friends);
    }


    /***
     * Override HashCode function
     * */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, friends);
    }
}

