/**
 * Simple class to represent a pair of elements of types S and T
 * @author kim
 * @version 1/2011
 *  revised: added package --kpc 2/13
 *
 * @param <S> type of the first element of the pair
 * @param <T> type of the second element of the pair
 */
package assignment02.src.wordsGeneric;

public class Pair<S,T> {
    // two items in pair
    private S first;
    private T second;
    
    /**
     * Create pair from fst and snd
     * @param fst  first element of pair
     * @param snd  second element of pair
     */
    public Pair(S fst,T snd) {
        first = fst;
        second = snd;
    }
    
    /**
     * @return first item of pair
     */
    public S getFirst() {
        return first;
    }
    
    /**
     * @return second item of pair
     */
    public T getSecond() {
        return second;
    }
    
    /**
     * @param other  another pair to be compared with this one
     * @return true iff both items in this pair are equal to the corresponding items in other
     */
    public boolean equals(Object other) {
        if (other instanceof Pair<?,?>) {
            Pair<?,?> otherPair = (Pair<?,?>)other;
            return first.equals(otherPair.getFirst()) && second.equals(otherPair.getSecond());
        } else {
            return false;
        }
    }
    
    /**
     * @return readable representation of pair
     */
    public String toString() {
        return "<"+first+","+second+">";
    }
}
