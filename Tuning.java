import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tuning {

    static double mean(double[] x) {
        double s = 0;
        for (double v : x) s += v;
        return s / x.length;
    }

    static double std(double[] x) {              // sample standard deviation
        double m = mean(x), s = 0;
        for (double v : x) s += (v - m) * (v - m);
        return Math.sqrt(s / (x.length - 1));
    }

    static void tuneSA(Metaheuristic<ArrayList<Integer>> LS, double optimum) {
        double[] T0s       = {10, 50, 100, 200};
        double[] coolings  = {0.9, 0.95, 0.99, 0.999};
        int[]    iterList  = {500, 1000, 5000};
        int      nSeeds    = 30;

        try (FileWriter fw = new FileWriter("SA_tuning.csv")) {
            fw.write("T0,cooling,iterations,meanValue,stdValue,meanTimeMs,stdTimeMs,meanBestIter,hitRate\n");

            for (int iters : iterList) {
                for (double T0 : T0s) {
                    for (double cooling : coolings) {
                        double[] vals = new double[nSeeds];
                        double[] times = new double[nSeeds];
                        double[] bestIte = new double[nSeeds];
                        int hits = 0;

                        for (int seed = 1; seed <= nSeeds; seed++) {
                            long t = System.nanoTime();
                            Results<ArrayList<Integer>> r =
                                    LS.SimulatedAnnealing(60.0, iters, seed, T0, cooling);
                            times[seed - 1] = (System.nanoTime() - t) / 1e6;   // total run time in ms

                            int last = r.bestresults.size() - 1;             // last entry = best found
                            vals[seed - 1] = r.bestresults.get(last);
                            bestIte[seed - 1] = r.bestIte.get(last);
                            if (vals[seed - 1] >= optimum) hits++;
                        }

                        fw.write(T0 + "," + cooling + "," + iters + ","
                                + mean(vals) + "," + std(vals) + ","
                                + mean(times) + "," + std(times) + ","
                                + mean(bestIte) + "," + (hits / (double) nSeeds) + "\n");
                        System.out.printf("iters=%d T0=%.0f cool=%.3f  mean=%.1f std=%.1f hit=%d/%d%n",
                                iters, T0, cooling, mean(vals), std(vals), hits, nSeeds);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}   