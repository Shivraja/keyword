package POS;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class TagText {

    private static class Item {

        public String word;
        public int frequency;
    }

    private static class Compare implements Comparator {

        public int compare(Object aa, Object bb) {
            Item a = (Item) aa;
            Item b = (Item) bb;
            if (a.frequency < b.frequency) {
                return 1;
            } else if (a.frequency > b.frequency) {
                return -1;
            } else {
                return a.word.compareTo(b.word);
            }
        }
// end of compare method
    }
// end of Item class

    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
        //System.out.println("mani");
        // Initialize the tagger
        MaxentTagger tagger = new MaxentTagger(
                "taggers/left3words-wsj-0-18.tagger");
        //System.out.println("manikandan");
        // The sample string
        String sample = "\0", test, tagged = "\0", output = "\0";
        Scanner s = new Scanner(System.in);
        File file = new File("pos_output.txt");

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        BufferedReader in = new BufferedReader(new FileReader("input.txt"));
        while ((test = in.readLine()) != null) {
            //   System.out.println(test);
            String split[] = test.split(" ");
            int n = split.length;
            for (int i = 0; i < n; i++) {
                sample = split[i];
                tagged = tagger.tagString(split[i]);
                output += tagged;
                output += "\n";
                String t = tagged.substring(tagged.indexOf('/') + 1);
                if (t.contains("NN")) {
                    bw.write(sample + "\n");
                }
                //System.out.println(sample+" "+t);
            }
        }
        
        bw.close();

        System.out.println("Done");

        FileInputStream inp = null;

        inp = new FileInputStream("pos_output.txt");

        // find words and build frequency map
        Map<String, Integer> freq = new HashMap<String, Integer>();
        Scanner input = new Scanner(inp);
        while (input.hasNext()) {
            String word = input.next().toLowerCase();
            Integer f = freq.get(word);
            if (f == null) {
                freq.put(word, 1);
            } else {
                freq.put(word, f + 1);
            }
        }
// sort frequency map
        SortedSet<Item> list = new TreeSet<Item>(new Compare());
        for (String word : freq.keySet()) {
            Item it = new Item();
            it.word = word;
            it.frequency = freq.get(word);
            list.add(it);
        }
        BufferedWriter b = null;
        fw = new FileWriter("frequency.txt");
        b = new BufferedWriter(fw);
        for (Item it : list) {

            String st = it.frequency + "\t" + it.word;
            System.out.println(st);
            b.write(st + "\n");
        }
        b.close();
    }
}
