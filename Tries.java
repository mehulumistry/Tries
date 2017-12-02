
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;



/**
 * Created by me on 11/12/17.
 */


class TriesNode{

    char c;
    HashMap<Character, TriesNode> children = new HashMap<Character, TriesNode>();
    boolean endOfWord;


    public TriesNode() {}

    public TriesNode(char c){
        this.c = c;
    }
}

class Trie{


    private TriesNode root;
    Trie(){

        root=new TriesNode();

    }


    public void insert(String companyName){

        HashMap<Character, TriesNode> children=root.children;

        for(int i=0;i<companyName.length();i++){

            char ch=companyName.charAt(i);

            TriesNode temp;

            if(children.containsKey(ch)){

                temp=children.get(ch);
            }
            else{

                temp= new TriesNode();
                children.put(ch,temp);


            }

            children=temp.children;

            if(i==companyName.length()-1){
                temp.endOfWord=true;
                //System.out.println(companyName + " " + "Inserted");
            }


        }


    }
    public HashMap<String,Integer> searchNode(String company) {

        TriesNode temp = null;
        HashMap<String, Integer> table = new HashMap<>();
        String name = "";
        String tname = "";
        Integer count = 1;

        HashMap<Character, TriesNode> children = root.children;

        for (int i = 0; i < company.length(); i++) {

            char ch = company.charAt(i);

            if (children.containsKey(ch)) {

                name = name + ch;
                temp = children.get(ch);
                 children = temp.children;


                if (temp != null && temp.endOfWord ) { //&& company.charAt(i+1) == 32

                    if (table.containsKey(name)) {
                        count = table.get(name);
                        count++;
                        table.put(name, count);
                        //name = "";

                    } else {
                        table.put(name,1);
                        //name = "";
                        //System.out.println(name);
                    }
                }

                else{

                }
            }

            else {

                //i++;

                while ( ch != 32 && i < company.length() - 1) { //company.charAt(i-1)!=32 &&

                    i++;
                    ch = company.charAt(i);
                }

                temp = null;
                children = root.children;
                name = "";
                tname = "";


            }
        }

        // System.out.println(table);
        return table;

    }


    int totalHitCount = 0;
    int total = 0;
    float totalRelevance = 0;


    public void display(String compa,int Hit,int totalWords){


        //DecimalFormat df = new DecimalFormat("###.###");

        totalHitCount = Hit + totalHitCount;
        //  total = totalWords + total;

        float Relevance = ((float)Hit * 100)/ totalWords ;



        totalRelevance = Relevance + totalRelevance;



        System.out.println(String.format("%-20s", compa) + "   " + String.format("%6s", Hit) + "        " + String.format("%-5s", String.format("%.3f",Relevance) + "%"));

    }

}

public class Tries {


    public static void main(String[] args) throws Exception {


        // Reading company.dat file
        Trie t = new Trie();

        HashSet<String> filterMap = new HashSet<>();
        filterMap.add("a");filterMap.add("an");filterMap.add("the");filterMap.add("or");filterMap.add("and");filterMap.add("but");filterMap.add("A");filterMap.add("An");filterMap.add("Or");filterMap.add("The");filterMap.add("But");



          // String fileName = new File("companies.dat").getAbsolutePath();


           String fileName = new File("src/companies.dat").getAbsolutePath();

        // This will reference one line at a time
        String line = null;

        ////////////////////// test ////////////


        ArrayList<ArrayList<String>> companiesArr = new ArrayList<ArrayList<String>>();


        int count = 0;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(new File(fileName));

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);


            while ((line = bufferedReader.readLine()) != null) {


                // split and insert according to company name
                String subCompany[] = line.split("\\t");
                String check[];
                //companiesArr = new String[5][subCompany.length];
                companiesArr.add(new ArrayList<String>());

                for(int k = 0; k<subCompany.length; k++){


                    // see if any companies contains articles a, an the, but if it contains then eliminate them from our filter


                    companiesArr.get(count).add(subCompany[k]);

                    //companiesArr[count][k] = subCompany[k];

                    check = subCompany[k].split(" ");

                    for(int p = 0 ; p< check.length;p++){

                        if(filterMap.contains(check[p]))
                            filterMap.remove(check[p]);

                    }



                }


                for (int in = 0; in < subCompany.length; in++) {

                    subCompany[in] = subCompany[in].replaceAll("[^a-zA-Z&@$# ]+", "").trim();
                    t.insert(subCompany[in]);
                    //System.out.println(subCompany[in]);

                }
                count++;

            }

            System.out.println("Enter a news Article: ");

            String FilteredArticle = "";


            Scanner scanner = new Scanner(System.in);
            String Article = scanner.nextLine();
            while(Article!=null) {



                String tt =scanner.nextLine();

                if (tt.isEmpty()) {

                }

                if (scanner.hasNextLine()) {
                    if(tt.matches("[..]+")){
                        break;
                    }
                    else {
                        Article = Article + " " + tt;

                    }
                } else {
                    Article = null;

                }
            }

            String word[] = Article.split("[  , ]");  // loop for each word of that line to filter a an the articles

            int totalWords = 0;
            for (int j = 0; j < word.length; j++) {



                   if(word[j].length() !=0 ){

                       totalWords = totalWords + 1;
                   }



                    word[j] = word[j].replaceAll("[^a-zA-Z&@$ ]+", "").trim();

                    if(filterMap.contains(word[j])) {


                        word[j] = "";

                    }


                    else

                        FilteredArticle = FilteredArticle +" "+ word[j];

                }



            HashMap<String,Integer> Output = t.searchNode(Article);

            // do aggregation of companies which are in same line companyArr

            int HitCount = 0;

            System.out.println(String.format("%-20s", "Company") + "    " + String.format("%6s", "HitCount") + "     " +  String.format("%-5s", "Relevance"));
            System.out.println("=================       ========     ========");

            for(int y = 0 ; y < companiesArr.size() ; y++) {

                for(int r = 0; r < companiesArr.get(y).size()  ;r++) {

                    if(Output.containsKey(companiesArr.get(y).get(r))) {
                        HitCount = Output.get(companiesArr.get(y).get(r)) + HitCount;

                        //   System.out.println(Outcome[i]);
                      //  System.out.println(companiesArr.get(y).get(r));

                    }

                }

                t.display(companiesArr.get(y).get(0), HitCount, totalWords);
                HitCount = 0;

            }

            System.out.println("");
            System.out.println(String.format("%-20s","TOTAL") + "   " + String.format("%6s", t.totalHitCount) + "        " + String.format("%-5s", String.format("%.3f",t.totalRelevance) + "%"));
            System.out.println("");
            System.out.println(String.format("%-20s","TOTAL WORDS")  +"   "+ String.format("%6s", totalWords));

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }





    }



}










