import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.IntFunction;

public class Runner {

    static void runAll(Metaheuristic<ArrayList<Integer>> LS, int nSeeds,
                       double saT0, double saCooling, int saIters, int ilsIters) {
        try (FileWriter fw = new FileWriter("AllRuns.csv");
             FileWriter fs = new FileWriter("Summary.csv")) {
            fw.write("algorithm,seed,value,timeToBestMs,bestIter,totalTimeMs\n");
            fs.write("algorithm,meanValue,stdValue,meanTimeToBestMs,stdTimeToBestMs,"
                    + "meanBestIter,stdBestIter,meanTotalMs,stdTotalMs\n");

            runOne("Hillclimber", nSeeds, seed -> LS.Hillclimber(10.0, seed), fw, fs);
            runOne("ILS", nSeeds, seed -> LS.IteratedLocalSearch(60.0, ilsIters, seed), fw, fs);
            runOne("SA", nSeeds,
                    seed -> LS.SimulatedAnnealing(60.0, saIters, seed, saT0, saCooling), fw, fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void runOne(String name, int nSeeds,
                       IntFunction<Results<ArrayList<Integer>>> algo,
                       FileWriter fw, FileWriter fs) throws IOException {
        algo.apply(0);                                   // warm-up run, not counted

        double[] vals = new double[nSeeds];
        double[] tBest = new double[nSeeds];
        double[] iBest = new double[nSeeds];
        double[] tTotal = new double[nSeeds];

        for (int seed = 1; seed <= nSeeds; seed++) {
            long t = System.nanoTime();
            Results<ArrayList<Integer>> r = algo.apply(seed);
            tTotal[seed - 1] = (System.nanoTime() - t) / 1e6;

            int last = r.bestresults.size() - 1;         // last entry = best found
            vals[seed - 1] = r.bestresults.get(last);
            tBest[seed - 1] = r.bestTime.get(last) / 1e6;
            iBest[seed - 1] = r.bestIte.get(last);

            fw.write(name + "," + seed + "," + vals[seed - 1] + "," + tBest[seed - 1] + ","
                    + iBest[seed - 1] + "," + tTotal[seed - 1] + "\n");
        }

        fs.write(name + "," + Tuning.mean(vals) + "," + Tuning.std(vals) + ","
                + Tuning.mean(tBest) + "," + Tuning.std(tBest) + ","
                + Tuning.mean(iBest) + "," + Tuning.std(iBest) + ","
                + Tuning.mean(tTotal) + "," + Tuning.std(tTotal) + "\n");
        System.out.printf("%s: mean=%.1f std=%.1f%n", name, Tuning.mean(vals), Tuning.std(vals));
    }
}