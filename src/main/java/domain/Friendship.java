package domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Tuple<Long, Long>> {
    private long user1;
    private long user2;

    private LocalDateTime dateTime;


    /***
     * Friendship constructor
     * @param user1 - Long
     * @param user2 - Long
     * */
    public Friendship(long user1, long user2) {
        this.user1 = user1;
        this.user2 = user2;
        if (user1 < user2) {
            setId(new Tuple<>(user1, user2));
        } else {
            setId(new Tuple<>(user2, user1));
        }
        this.dateTime = LocalDateTime.now();
    }


    /***
     * return user1
     * */
    public long getUser1() {
        return user1;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /***
     * set user1
     * */
    public void setUser1(long user1) {
        this.user1 = user1;
    }

    /***
     * return user2
     * */
    public long getUser2() {
        return user2;
    }

    /***
     * set user2
     * */
    public void setUser2(long user2) {
        this.user2 = user2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return user1 == that.user1 && user2 == that.user2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", dateTime=" + dateTime +
                '}';
    }
}

