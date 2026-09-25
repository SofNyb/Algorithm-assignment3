import javax.xml.transform.Result;
import java.util.ArrayList;

public class Metaheuristic<S> {
    protected Problem<S> problem;


    public Metaheuristic(Problem<S> problem) {
        this.problem = problem;
    }

    public Results<S> IteratedLocalSearch(double timelimits, int iterationlimit, int seed) {
        final long starttime = System.nanoTime();
        S bestSol = problem.generateInitialSolution(seed);
        Results<S> ResILS = new Results("ILS" + seed);
        ResILS.putSolution(bestSol);
        int ite = 0;
        double tempv = problem.evaluate(bestSol);
        double bestvalue = tempv;
        ResILS.putbest(tempv, System.nanoTime() - starttime, ite);

        /* Write you method here */

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
