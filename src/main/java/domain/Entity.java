package domain;

import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 5435435221L;
    private ID id;

    /***
     * return the ID of an Entity
     * @return id
     * */
    public ID getId() {
        return id;
    }

    /***
     * set the ID of an Entity
     * @param id - ID
     * */
    public void setId(ID id) {
        this.id = id;
    }
}

