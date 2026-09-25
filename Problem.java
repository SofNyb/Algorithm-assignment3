import java.util.ArrayList;

public interface Problem<S> {
    int getInstanceSize();

    int getSizeSolution(S s);

    double evaluate(S solution); // Objective function

    double evaluateswap(S solution, double value, int old, int ins);

    boolean removeItemFromSolution(S s, int indexS);

    boolean InsertItemToSolution(S s, int job);

    S generateInitialSolution(int seed);

    boolean isFeasible(S solution);

    boolean isFeasible(S solution, int additem);

    boolean SwapFeasible(S solution, int i, int j);

    boolean bestswap(S solution);

    double evaluateRemoveditem(S s, double cvalue, int rmitem);

    double evaluateInsertitem(S s, double cvalue, int additem);

    void PrintSolution(S solution);
}
