package h04;

import java.util.Objects;

public class Pair<T, S> {

    public T fst;
    public S scd;

    public Pair(T fst, S scd) {
        this.fst = fst;
        this.scd = scd;
    }

    public T getFirst() {
        return fst;
    }

    public S getSecond() {
        return scd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(fst, pair.fst) && Objects.equals(scd, pair.scd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, scd);
    }

    @Override
    public String toString() {
        return "(" + fst + ", " + scd + ')';
    }
}
