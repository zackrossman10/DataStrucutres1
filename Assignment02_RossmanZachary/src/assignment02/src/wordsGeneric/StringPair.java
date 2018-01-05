/**
 * Convenience class representing a pair of strings.
 * Defined so can write StringPair instead of Pair<String,String>
 * This works because no methods of type Pair return another element
 * of type Pair.
 * @author kim
 * @version 1/2011
 *  revised: added package --kpc 2/13
 *
 */
package assignment02.src.wordsGeneric;

public class StringPair extends Pair<String, String> {
    
    /**
     * Create new pair from first, second
     * @param fst  first element of pair
     * @param snd  second element of pair
     */
    public StringPair(String fst, String snd) {
        super(fst,snd);
    }

}
