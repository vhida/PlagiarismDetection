package com.haoliu;
import java.io.*;
import java.util.*;

/**
 * Class to represent similarity.Internally it has a map field that stores a string(word) and its synonyms as one
 * entry.
 */
public class Similarity {

    private Map<String,Set<String>> synonyms;

    public Similarity(String fileName) throws IOException{
        BufferedReader bufReader = null;
        try{
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line;
            synonyms = new HashMap<>();
            while((line = bufReader.readLine()) != null){
                String[] similarWords = line.split(" ");
                Set<String> set = new HashSet<>();
                for(String word: similarWords)
                    set.add(word);
                for(String word:similarWords)
                    synonyms.put(word,set);
            }
        }catch(IOException ex){
            System.err.println("please check synonym file name \""+fileName+"\"");
            System.exit(-1);
        }finally{
            if(bufReader != null)
                bufReader.close();
        }
    }

    public Map<String,Set<String>> getSynonyms(){
        return synonyms;
    }
}
