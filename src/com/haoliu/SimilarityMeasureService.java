package com.haoliu;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to measure similarity based on a composed Similarity object which is created based on the synonym file
 */
public class SimilarityMeasureService {

    Similarity similarity;
    Set<Tuple> baseTupleSet;

    public SimilarityMeasureService(String fileName) throws IOException{
        similarity = new Similarity(fileName);
    }

    /**
     * This method measures similarity by giving a percentage of tuples in the file to check that "appears" in the
     * base file.
     *
     * @param tuplesToCheck
     * @param baseTuples
     * @return percentage of tuples in the file to detect plagiarism appearing in the base file
     */
    public float measureSimilarity(List<Tuple> tuplesToCheck, List<Tuple> baseTuples) {
        initializeBase(baseTuples);
        int count = 0;
        for (Tuple t : tuplesToCheck) {
            if (isAppearingInBase(t))
                count++;
        }
        return (float) 100 * count / tuplesToCheck.size();
    }

    /**
     * This methods determines whether a specif tuple "appears" in the base file.
     * A tuple "appears" in the base file if either it is contained in the set of tuples extracted from the base file
     * or a tuple made of its synonyms as defined in the synonym file is contained.
     *
     * @param t
     * @return whether t "appears" in the base file
     */
    private boolean isAppearingInBase(Tuple t) {
        if (baseTupleSet.contains(t))
            return true;
        Map<String, Set<String>> synonyms = similarity.getSynonyms();
        for (int i = 0; i < t.size(); i++) {
            String word = t.get(i);
            //for each tuple, iterate over all possible tuples made of its synonyms
            if (synonyms.containsKey(word)) {
                Set<String> synonym = synonyms.get(word);
                for (String similarWord : synonym) {
                    Tuple tmp = Tuple.copy(t);
                    tmp.subsitute(i, similarWord);
                    if (baseTupleSet.contains(tmp))
                        return true;
                }
            }
        }

        return false;
    }

    private void initializeBase(List<Tuple> base) {
        baseTupleSet = new HashSet<>(base);
    }

}
