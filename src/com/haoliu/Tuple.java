package com.haoliu;
import java.io.*;
import java.util.*;

/**
 * Class to represent a tuple(a wrjva -cpap over list). It has class method to load files and extract tuples
 * from them.
 */
public class Tuple {

    private List<String> tuple = new ArrayList<>();

    private void add(String word){
        tuple.add(word);
    }
    public String get(int i){
        return tuple.get(i);
    }
    public int size(){
        return tuple.size();
    }
    public void subsitute(int position,String word){
        tuple.set(position,word);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple that = (Tuple) o;
        if(that.size()!=this.size())
            return false;
        for(int i = 0; i< this.size();i++){
            if(!this.get(i).equals(that.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i = 0;i<this.size();i++)
            hash+=this.get(i).hashCode();
        return hash;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i<tuple.size();i++)
            sb.append(get(i)).append(",");
        sb.append("]");
        return sb.toString();
    }

    /**
     * This class method creates a deep copy of Tuple t.
     *
     * @param t
     * @return a deep copy of t
     */
    public static Tuple copy(Tuple t){
        Tuple copy = new Tuple();
        for(int i = 0; i< t.size();i++)
            copy.add(t.get(i));
        return copy;
    }

    /**
     * This method is to load a file and extract all tuples of given size from words in the file
     *
     * @param fileName
     * @param tupleSize
     * @return list of tuples extracted from words in the file
     * @throws IOException
     */
    public static List<Tuple> getTuplesFromFile(String fileName,int tupleSize) throws IOException{
        BufferedReader bufReader = null;
        List<String> listOfAllWords = new ArrayList<>();
        List<Tuple> tuples = new ArrayList<>();

        try{
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line;
            while((line = bufReader.readLine()) != null){
                listOfAllWords.addAll(Arrays.asList(line.split((" "))));
            }
        }catch(IOException ex){
            System.err.println("please check file name \""+fileName+"\"");
            System.exit(-1);
        }finally {
            if(bufReader != null)
                bufReader.close();
        }
        tuples = wordsToTuples(listOfAllWords,tupleSize);
        return tuples;
    }

    /**
     * This method is to extract tuples from words in a line. For given tuple size N, assume that each line contains
     * at least words
     *
     * @param listOfWords
     * @param tupleSize
     * @return list of tuples
     */
    private static List<Tuple> wordsToTuples(List<String> listOfWords, int tupleSize) {

        if(listOfWords.size()<tupleSize) {
            System.err.println("Tuple size is too large");
            System.exit(-1);
        }

        List<Tuple> tuples = new ArrayList<>();
        for(int i =0;i<listOfWords.size()-tupleSize+1;i++){
            Tuple tuple = new Tuple();
            for(int j=0;j<tupleSize;j++)
                tuple.add(listOfWords.get(i+j));
            tuples.add(tuple);
        }
        return tuples;
    }

}
