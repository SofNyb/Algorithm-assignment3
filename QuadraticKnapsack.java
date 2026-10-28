import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuadraticKnapsack implements Problem<ArrayList<Integer>>{
        int[][] p;
        int[] value;
        int[] weight;
        int Jobs;
        int weightLimit;

        int[][] getp(){return p;}
        int[] getValue(){return value;}
        int[] getTimes(){return weight;}
        //checkfeasibility
        public QuadraticKnapsack(int[][] qdata,int[] values, int[] weights, int weightLimit){
            p=qdata;
            value=values;
            weight=weights;
            this.weightLimit=weightLimit;
            Jobs=values.length;
        }

        @Override
        public int getInstanceSize(){
            return Jobs;
        }

        @Override
        public int getSizeSolution( ArrayList<Integer> s){
            return s.size();
        }

        public double solutionWeight(ArrayList<Integer> s){
            Integer sweight=0;
            for (int i=0;i<s.size();i++)
            {
                sweight+=weight[s.get(i)];
            }
            return sweight;
        }

        @Override
        public boolean isFeasible(ArrayList<Integer> s){
            Integer sweight=0;
            for (int i=0;i<s.size();i++)
            {
                sweight+=weight[s.get(i)];
                if (sweight>weightLimit)
                    return false;
            }
            return true;
        }

        @Override
        public boolean isFeasible(ArrayList<Integer> s,int additem){
            Integer sweight=weight[additem];
            for (int i=0;i<s.size();i++) {
                sweight+=weight[s.get(i)];
                if (sweight>weightLimit)
                    return false;
            }
            return true;
        }
        //calculate value
        @Override
        public double evaluate(ArrayList<Integer> s){ // fitness
            int Totalprofit=0;
            for (int i=0;i<s.size();i++)
            {
                Totalprofit+=value[s.get(i)];
                for(int j=0;j<i;j++) {
                    Totalprofit += p[s.get(i)][s.get(j)];
                }
            }
            return Totalprofit;
        }

       @Override
       public ArrayList<Integer> generateInitialSolution(int seed){
           double totalweight=0.0;
           Random random = new Random(seed);  // make a random according to seed to ensure reproducibility
           ArrayList<Integer> RandomSequence=new ArrayList<>();
           ArrayList<Integer> initialsolution=new ArrayList<>();
           for(int i=0;i< Jobs;i++)
           {
               RandomSequence.add(i);
           }
           Collections.shuffle(RandomSequence,random); // should be the same with same seed
           int j=0;
           while (InsertItemToSolution(initialsolution,RandomSequence.get(j)))
           {
               j++;
           }
           return initialsolution;
       }

       @Override
       public double evaluateswap(ArrayList<Integer> s, double cvalue,int old, int ins) {
           double Totalprofit = cvalue - value[old] + value[ins];
           for (int i = 0; i < s.size(); i++) { // quadratic values for new and remove for old item
               int item = s.get(i);
               if (item != old) {
                   Totalprofit += p[item][ins];
                   Totalprofit -= p[item][old];
               }
           }
           return Totalprofit;
       }

       @Override
        public  boolean SwapFeasible(ArrayList<Integer> s, int i, int j) {
            if (!s.contains(j)) {// only swap with items not in current solution
                return solutionWeight(s) - weight[i] + weight[j] <= weightLimit;
            }
            return false;
        }

        @Override
        public boolean removeItemFromSolution(ArrayList<Integer> s, int indexS){
            if(indexS<s.size()){
                s.remove(indexS);
                return true;
            }
            return false;
        }

        @Override
        public boolean InsertItemToSolution(ArrayList<Integer> s, int job){
            if(job<Jobs && !s.contains(job) && (solutionWeight(s) + weight[job] <= weightLimit)){
                s.add(job);
                return true;
            }
            return false;
        }

        @Override
        public double evaluateRemoveditem(ArrayList<Integer> s, double cvalue, int rmitem){
            double newvalue=cvalue-value[rmitem];
            for (int i=0;i<s.size();i++){ // quadratic values for new and remove for old item
                int item=s.get(i);
                if(item!=rmitem){
                    newvalue-=p[item][rmitem];
                }
            }
            return newvalue;
        }

        @Override
        public double evaluateInsertitem(ArrayList<Integer> s, double cvalue, int additem){
            double newvalue=0;
            if (isFeasible(s,additem)){
                 newvalue=cvalue+value[additem];
                for (int i=0;i<s.size();i++){ // quadratic values for new and remove for old item
                    int item=s.get(i);
                    if(item!=additem){
                        newvalue+=p[item][additem];
                    }
                }
            }
            return newvalue;
        }

        @Override
        public boolean bestswap(ArrayList<Integer> s) {
            double cvalue = evaluate(s);
            double bestvalue = cvalue;
            double tempv;
            int removeditemS = Jobs;
            int inserteditem = Jobs;
            for (int i = 0; i < s.size(); i++) {// items in list
                for (int j = 0; j < Jobs; j++) { // possible items
                    if (SwapFeasible(s, s.get(i), j)) {                   // this has been changed because SwapFeasable need the items number to get the weigt, s.get(i) gets the number on placement nr. i
                        tempv = evaluateswap(s, cvalue, s.get(i), j); // Calculate value of new solution
                        if (tempv > bestvalue) { // if swap leads to better solution than current best
                            bestvalue = tempv;
                            removeditemS = i;
                            inserteditem = j;
                        }
                    }
                }
            }
            if (removeditemS < s.size() && inserteditem < Jobs){ //update s to new solution
                s.set(removeditemS, inserteditem);
                return true;
            }
            return false;
        }

        @Override
        public void PrintSolution(ArrayList<Integer> s){
            System.out.println("Print solution list: \n [");
            System.out.println(" Jobs: "+ Jobs);
            if (s==null){
                System.out.println(" No solution found");
                return;
            }
            for(int i=0;i<s.size();i++) {
                System.out.print(s.get(i));
                System.out.print(" ");
            }
            System.out.print("\n");
            System.out.println(" Solution  Weight: "+ solutionWeight(s));
            System.out.println(" Solution  Value: "+ evaluate(s));
        }

        @Override 
        public boolean bestadd(ArrayList<Integer> s) {
            double cvalue = evaluate(s);
            double bestvalue= cvalue;
            int bestItem = -1;
            for (int j = 0; j < Jobs; j++) {
                if (!s.contains(j) && isFeasible(s, j)) {
                    double v = evaluateInsertitem(s, cvalue, j);
                    if (v > bestvalue) {
                        bestvalue = v;
                        bestItem = j;
                    }
                }
            }
            if (bestItem >= 0) {
                s.add(bestItem);
                return true;
            }
            return false;
        }

        @Override
        public boolean localStep(ArrayList<Integer> s) {
            if (bestadd(s)) return true;
            return bestswap(s);
        }

        // one small random move: add an item, drop an item, or swap an item
        @Override 
        public boolean randomMoves(ArrayList<Integer> s, Random rand) {
            int type = rand.nextInt(3);                                     // 0: add, 1: remove, 2: swap
            if (type == 0) {                                                // add
                return InsertItemToSolution(s, rand.nextInt(Jobs));
                } else if (type == 1) {                                     // remove
                if (s.isEmpty()) return false;
                    return removeItemFromSolution(s, rand.nextInt(s.size()));
            } else {                                                        // swap
                if (s.isEmpty()) return false;
                int i = rand.nextInt(s.size());
                int j = rand.nextInt(Jobs);
                if (SwapFeasible(s, s.get(i), j)) {
                    s.set(i, j);
                    return true;
                }
                return false;
                
            }
        }

    @Override
    public ArrayList<Integer> copy(ArrayList<Integer> s) {
        return new ArrayList<>(s);
    }
}
