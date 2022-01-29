package jPlus.lang;

/**
 * @see "http://www.javatuples.org/ for a more thorough approach"
 * Hey! It's Marcus. So basically, a Tuple is a lot like a hashmap
 * entry, but on it's own. Or think of it as a single enum entry, non-static
 * of course. Do HashMap<String, Tuple<MyObject, MySecondObject>>.
 * Make more tuples! This is tuple2, but A, B, C would be tuple3.
 */
public class Tuple2<A, B> {

    public final A a;
    public final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2<?, ?> tuple = (Tuple2<?, ?>) o;
        if (!a.equals(tuple.a)) return false;
        return b.equals(tuple.b);
    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
