package com.haoliu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;


/**
 * This class is the main class of the program.It performs plagiarism detection using a N-tuple comparison
 * algorithm allowing for synonyms in the text and give out percentage of N-tuples in one file seen in another
 * as the measurement to determine possible plagiarism.
 */

public class PlagiarismDetector {

    private static final String DEFAULT_TUPLE_SIZE = "DEFAULT_TUPLE_SIZE";
    SimilarityMeasureService service;

    public PlagiarismDetector(String synonymFileName){
        try {
            this.service = new SimilarityMeasureService(synonymFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main method as entrance of program that takes 3 mandatory arguments: synonym file containing lists of synonyms,file1
     * (file to check) and file2(base file), and 1 optional argument,tuple size, the default to be 3 if absent.
     * It loads 3 files provided, invokes plagiarism detection functionality and gives out percentage of tuples
     * in file1 appearing in file2.
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        if (args.length < 3 || args.length > 4) {
            System.err.println("Usage:<synonym_file_name> <check_file_name> <base_file_name> [<tuple_size>]");
            System.exit(-1);
        }
        String synonymFileName = args[0];
        String checkFileName = args[1];
        String baseFileName = args[2];
        PlagiarismDetector detector;
        //load default tuple size from config
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = PlagiarismDetector.class.getResourceAsStream("resources/config.properties");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }
        //set tupleSize
        int tupleSize = Integer.valueOf(prop.getProperty(DEFAULT_TUPLE_SIZE));
        if (4 == args.length)
            tupleSize = Integer.valueOf(args[3]);

        detector = new PlagiarismDetector(synonymFileName);
        detector.detectAndScore(checkFileName, baseFileName, tupleSize);
    }

    private void detectAndScore(String checkFileName, String baseFileName, int tupleSize) throws IOException {
        try {
            List<Tuple> tuplesToCheck = Tuple.getTuplesFromFile(checkFileName, tupleSize);
            List<Tuple> baseTuples = Tuple.getTuplesFromFile(baseFileName, tupleSize);
            float percentage = service.measureSimilarity(tuplesToCheck, baseTuples);
            if (percentage == Math.round(percentage))
                System.out.printf("%d%%\n", (int) percentage);
            else
                System.out.printf("%.2f%%\n", percentage);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}

