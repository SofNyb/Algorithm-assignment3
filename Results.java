import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Results<S> {
    ArrayList<Double> bestresults= new ArrayList<>();
    ArrayList<Long> bestTime=new ArrayList<>();
    ArrayList<Integer> bestIte=new ArrayList<>();
    ArrayList<Double> results =new ArrayList<>();
    ArrayList<Long> AllTime =new ArrayList<>();
    ArrayList<Integer> AllIte=new ArrayList<>();
    S finalSolution=null;
    String name;

    public Results(String name){
        this.name=name;
    }
    void putbest(Double result,long time,int ite){
        bestresults.add(result);
        bestTime.add(time);
        bestIte.add(ite);
    }
    void putCurrent(Double result, long time,int ite){
        results.add(result);
        AllTime.add(time);
        AllIte.add(ite);
    }
    void putSolution(S solution){
        finalSolution=solution;
    }
    void PrintBest() {
        try{
            System.out.println("Starting up File");
            String filename = name+"Best.csv";
            File file=new File(filename);
            FileWriter fw =new FileWriter(file, false);
            System.out.println("Write header");
            fw.write("value, time ,  iteration \n");
            System.out.println("Write Results to  File size: "+bestresults.size());

            for(int i=0;i<bestresults.size();i++){
                  fw.write(bestresults.get(i)+","+bestTime.get(i)+", "+bestIte.get(i)+"\n");
             }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    void PrintAllResults(){
        try{
            String filename=name+"All.csv";
            File file=new File(filename);
            FileWriter fw =new FileWriter(file,false);
            fw.write("Value, Time, Iteration \n");
            for(int i=0;i<results.size();i++){
                fw.write(results.get(i)+","+ AllTime.get(i)+","+ AllIte.get(i)+"\n");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
