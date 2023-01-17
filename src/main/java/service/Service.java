package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import repository.database.Repository;
import utils.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static java.lang.Math.toIntExact;

public class Service {
    private Repository<Long, User> repo;
    private Repository<Tuple<Long, Long>, Friendship> network;

    /**
     * Class constructor
     *
     * @param repo    - Users Repo
     * @param network - Friendships Repo
     */
    public Service(Repository<Long, User> repo, Repository<Tuple<Long, Long>, Friendship> network) {
        this.repo = repo;
        this.network = network;
        for (Friendship f : network.getAll()) {
            repo.find(f.getUser1()).addFriend(repo.find(f.getUser2()));
            repo.find(f.getUser2()).addFriend(repo.find(f.getUser1()));
        }
    }

    /**
     * add a user to repo
     *
     * @param u - User
     * @return user
     */
    public User addUser(User u) {
        User u1 = repo.save(u);
        return u1;
    }

    public User updateUser(User u){
        User u1 = repo.update(u);
        return u1;
    }

    public boolean userExistsByUsername(String username){
        for(User u1 : getUsers()){
            if(Objects.equals(username, u1.getUsername()))
                return true;
        }
        return false;
    }

    public User findUserByUsername(String username){
        for(User u1 : getUsers()){
            if(Objects.equals(username, u1.getUsername()))
                return u1;
        }
        return null;
    }

    public User findUserById(Long id){
        for(User u1: getUsers()){
            if(Objects.equals(id, u1.getId()))
                return u1;
        }
        return null;
    }

    private List<User> getUsersByName(String name){
        ArrayList<User> matchingUsers = new ArrayList<>();

        name = name.toLowerCase();

        for(User u : getListOfUsers()){
            if(u.getFirstName().toLowerCase().contains(name) || u.getLastName().toLowerCase().contains(name) ||
            u.getUsername().toLowerCase().contains(name)){
                matchingUsers.add(u);
            }
        }
        return matchingUsers;
    }

    public List<User> getListOfUsers(){
        return (List<User>) repo.getAll();
    }

    /**
     * remove user from repo
     *
     * @param u - User
     * @return user deleted
     */
    public User removeUser(User u) {
        try {
            Iterable<User> list = repo.getAll();
            for (User u1 : list) {
                u1.removeFriend(u);
                removeFriendship(u.getId(), u1.getId());
                removeFriendship(u1.getId(), u.getId());
            }
            User user = repo.delete(u.getId());
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * add a Friendship in network
     *
     * @param l1 - User
     * @param l2 - User
     * @return friendship between l1 and l2
     */
    public Friendship addFriendship(Long l1, Long l2) {
        boolean bool1 = repo.find(l1).addFriend(repo.find(l2));
        boolean bool2 = repo.find(l2).addFriend(repo.find(l1));

        if (bool1 || bool2) {
            if (bool1 && bool2) {
                Friendship f = new Friendship(l1, l2);
                network.save(f);
                return f;
            }
        }
        return null;
    }

    public Friendship changeAcceptStatus(Long l1, Long l2){
        if(l2 < l1){
            Long aux = l1;
            l1 = l2;
            l2 = aux;
        }
        for(Friendship f : getFriendships()){
            if(f.getUser1() == l1 && f.getUser2() == l2)
                f.setAccepted(true);
            return f;
        }
        return null;
    }

    public Friendship updateFriendship(Long l1, Long l2){
        boolean bool1 = repo.find(l1).addFriend(repo.find(l2));
        boolean bool2 = repo.find(l2).addFriend(repo.find(l1));

        if (bool1 || bool2) {
            if (bool1 && bool2) {
                Friendship f = new Friendship(l1, l2);
                network.update(f);
                return f;
            }
        }
        return null;
    }

    /**
     * delete a friendship from network
     *
     * @param l1 - User
     * @param l2 - User
     * @return deleted friendship
     */
    public Friendship removeFriendship(Long l1, Long l2) {
        boolean bool1 = repo.find(l1).addFriend(repo.find(l2));
        boolean bool2 = repo.find(l2).addFriend(repo.find(l1));
        try {
            Friendship f = new Friendship(l1, l2);
            network.delete(new Tuple<>(l1, l2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Friendship(l1, l2);
        }
        return null;
    }

    /**
     * return list of users
     *
     * @return list of users
     */
    public Iterable<User> getUsers() {
        return repo.getAll();
    }

    public List<User> getFriendsForUser(User user){
        List<User> list = new ArrayList<User>();
        for(Friendship f : getFriendships()) {
            if (f.getUser1() == user.getId() && f.getAccepted())
                list.add(repo.find(f.getUser2()));
            else
                if(f.getUser2() == user.getId() && f.getAccepted())
                    list.add(repo.find(f.getUser1()));
        }
        return list;
    }

    public List<User> getNonFriendsForUser(User user){
        List<User> friends = getFriendRequests(user);
        List<User> allUsers = getListOfUsers();
        List<User> list = new ArrayList<>();
        for(User u : allUsers){
            boolean good = true;
            for(User u1 : friends){
                if(Objects.equals(u1.getId(), u.getId()) && Objects.equals(u.getId(), user.getId())) {
                    good = false;
                    break;
                }
            }
            if(good)
                list.add(u);
        }
        return list;
    }

    public List<User> getFriendRequests(User user){
        List<User> list = new ArrayList<>();
        for(Friendship f : getFriendships()) {
            if (f.getUser1() == user.getId() && !f.getAccepted())
                list.add(repo.find(f.getUser2()));
            else
            if(f.getUser2() == user.getId() && !f.getAccepted())
                list.add(repo.find(f.getUser1()));
        }
        return list;
    }

    /**
     * return list of friendships
     *
     * @return list of friendships
     */
    public Iterable<Friendship> getFriendships() { return network.getAll(); }

    public void updateFriendship2(Long id1, Long id2, Long newId1, Long newId2) {
        Friendship friendship = new Friendship(id1, id2);
        Friendship newFriendship = new Friendship(newId1, newId2);
        network.update2(friendship, newFriendship);
    }

    /***
     *
     * @return number of communities
     */
    public int numberOfCommunities() {
        int size = 0;
        for (User u : repo.getAll()) {
            size = toIntExact(u.getId());
        }
        Graph g = new Graph(size);
        for (Friendship p : network.getAll()) {
            g.addEdge(toIntExact(p.getUser1()), toIntExact(p.getUser2()));

        }
        g.DFS();
        int nr = g.ConnectedComponents();
        nr = nr - (size - repo.size());
        return nr;
    }

    /***
     *
     * @return list of integers of the most socializable community
     */
    public ArrayList<Integer> socializableCommnunity() {
        int size = 0;
        for (User u : repo.getAll()) {
            size = toIntExact(u.getId());
        }
        Graph g = new Graph(size);
        for (Friendship f : network.getAll()) {
            g.addEdge(toIntExact(f.getUser1()), toIntExact(f.getUser2()));

        }
        g.DFS();
        int max = 1;
        ArrayList<Integer> comp = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> lists = g.returnComponents();
        for (ArrayList<Integer> list : lists) {
            if (list.size() >= max) {
                max = list.size();
                comp = list;
            }

        }
        return comp;
    }

    public Iterable<User> PrintListUtilizatori() {
        return repo.getAll();}

    public Iterable<Friendship> PrintListPrieteni() {
        return network.getAll();
    }


}


