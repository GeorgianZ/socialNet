package domain;

import java.util.Objects;

public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /***
     * return left element
     * */
    public E1 getLeft(){
        return this.e1;
    }

    /***
     * set the value for left element
     * */
    public void setLeft(E1 e1){
        this.e1 = e1;
    }

    /***
     * return right element
     * */
    public E2 getRight(){
        return this.e2;
    }

    /***
     * set the value for right element
     * */
    public void setRight(E2 e2){
        this.e2 = e2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(e1, tuple.e1) && Objects.equals(e2, tuple.e2);
    }

    @Override
    public String toString() {
        return "" + e1 + "," + e2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}

