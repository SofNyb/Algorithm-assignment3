import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Random;

public class Metaheuristic<S> {
    protected Problem<S> problem;


    public Metaheuristic(Problem<S> problem) {
        this.problem = problem;
    }

    public Results<S> IteratedLocalSearch(double timelimits, int iterationlimit, int seed) {
        final long starttime = System.nanoTime();
        S bestSol = problem.generateInitialSolution(seed);          // s
        Results<S> ResILS = new Results("ILS" + seed);
        int ite = 0;
        Random rand = new Random(seed);

        // s := Local-Search(s)
        while (problem.bestswap(bestSol)) { }
        double bestvalue = problem.evaluate(bestSol);               // f(s)
        ResILS.putSolution(bestSol);
        ResILS.putbest(bestvalue, System.nanoTime() - starttime, ite);

        while (ite < iterationlimit
                && (System.nanoTime() - starttime) / 1e9 < timelimits) {


            // new random start solution instead of mutating the best solution
            S newSol = problem.generateInitialSolution(rand.nextInt());
            
            // local search on the new solution
            while (problem.bestswap(newSol)) { }
            double tempv = problem.evaluate(newSol);

            // keep it if it's better than the best solution found so far
            if (tempv > bestvalue) {
                bestSol = newSol;
                bestvalue = tempv;
                ResILS.putSolution(bestSol);
                ResILS.putbest(bestvalue, System.nanoTime() - starttime, ite);
            }

            ite++;                                                  // n := n + 1
        }

        return ResILS;
    }

    public Results<S> SimulatedAnnealing(double timelimits, int iterationlimit, int seed, double T0, double cooling) {
        final long starttime = System.nanoTime();
        S bestSol = problem.generateInitialSolution(seed);
        double tempv = problem.evaluate(bestSol);
        int ite = 0;
        Random rand = new Random(seed);
        S currSol = problem.copy(bestSol) ;
        double currValue = problem.evaluate(currSol);

        //Start results lists
        Results<S> ResSA = new Results("SA" + seed);
        ResSA.putSolution(bestSol);
        ResSA.putbest(tempv, System.nanoTime() - starttime, ite);

        double bestvalue = tempv;

        double T = T0;

        while (ite < iterationlimit
                && (System.nanoTime() - starttime) / 1e9 < timelimits) {

            // neighbour move on a copy of currSol
            S newSol = problem.copy(currSol);
            int removes = 1 + rand.nextInt(2);               // remove 1-2
            for (int r = 0; r < removes; r++) {
                int size = problem.getSizeSolution(newSol);
                if (size > 0) {
                    problem.removeItemFromSolution(newSol, rand.nextInt(size));
                }
            }
            for (int t = 0; t < 5; t++) {                           // insert at random
                problem.InsertItemToSolution(newSol, rand.nextInt(problem.getInstanceSize()));
            }

            double newValue = problem.evaluate(newSol);
            double delta = newValue - currValue;                    // positive = neighbour is better

            // accept if better, or with probability exp(delta / T) if worse
            if (delta > 0 || rand.nextDouble() < Math.exp(delta / T)) {
                currSol = newSol;
                currValue = newValue;

                // new best? save a COPY so later changes don't affect it
                if (currValue > bestvalue) {
                    bestSol = problem.copy(currSol);
                    bestvalue = currValue;
                    ResSA.putSolution(bestSol);
                    ResSA.putbest(bestvalue, System.nanoTime() - starttime, ite);
                }
            }


            T = T * cooling;
            ite++;
        }

        return ResSA;
    }

    public Results<S> Hillclimber(double timelimits, int seed) {
        //Start time
        final long starttime = System.nanoTime();
        S bestSol = problem.generateInitialSolution(seed);
        Results<S> HillRes = new Results<S>("Hill" + seed);
        HillRes.putSolution(bestSol);
        int ite = 0;
        double tempv = problem.evaluate(bestSol);
        double bestvalue = tempv;
        HillRes.putbest(tempv, System.nanoTime() - starttime, ite);
        S currentSol = bestSol;

        //Start Local search
        while (System.nanoTime() - starttime < (timelimits * 1000000000)) {
            ite++;
            if (!problem.bestswap(currentSol)) {
                break;
            }
            // New best found
            HillRes.putbest(problem.evaluate(currentSol), System.nanoTime() - starttime, ite);
        }
        HillRes.putSolution(currentSol);
        return HillRes;
    }
}
