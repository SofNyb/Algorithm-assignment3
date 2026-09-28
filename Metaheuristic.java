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

            // s' := Mutate(s), makes a copi
            S newSol = problem.copy(bestSol);

            int removes = 1 + rand.nextInt(2);                      // remove 1-2
            for (int r = 0; r < removes; r++) {
                int size = problem.getSizeSolution(newSol);
                if (size > 0) {
                    problem.removeItemFromSolution(newSol, rand.nextInt(size));
                }
            }
            for (int t = 0; t < 5; t++) {                           // inset at random
                problem.InsertItemToSolution(newSol, rand.nextInt(problem.getInstanceSize()));
            }

            // s' := Local-Search(s')
            while (problem.bestswap(newSol)) { }

            double tempv = problem.evaluate(newSol);                // f(s') after evaluating s'

            // if f(s') > f(s) then s := s'   (maximizing!)
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

    public Results<S> SimulatedAnnealing(double timelimits, int iterationlimit, int seed) {
        final long starttime = System.nanoTime();
        S bestSol = problem.generateInitialSolution(seed);
        double tempv = problem.evaluate(bestSol);
        int ite = 0;
        //Start results lists
        Results<S> ResSA = new Results("SA" + seed);
        ResSA.putSolution(bestSol);
        ResSA.putbest(tempv, System.nanoTime() - starttime, ite);

        double bestvalue = tempv;

        /* Write you method here */

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
//          New best found
            HillRes.putbest(problem.evaluate(currentSol), System.nanoTime() - starttime, ite);
        }
        HillRes.putSolution(currentSol);
        return HillRes;
    }
}
