//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    IO.println(String.format("Quadratic Knapsack problem"));

    int[] value = {7, 6, 13,16, 5, 10, 9, 23, 18, 12, 9, 22, 17, 32, 8};
    int[] weight= {13, 14, 14, 15, 15, 9, 26, 24, 13, 11, 9, 12, 25, 12, 26};

    int[][] p = {{0	,12	,7	,6	,13	,8	,11	,7	,15	,23	,14	,15	,17	,9	,21},
            {12	,0	,15	,13	,10	,5	,9	,10	,8	,17	,11	,13	,12	,16	,15},
            {7	,15	,0	,11	,16	,6	,8	,14	,13	,4	,14	,8	,15	,32	,16},
            {6	,13	,11	,0	,10	,13	,14	,14	,17	,15	,14	,6	,24	,13	, 4},
            {13	,10	,16	,10	,0	,9	,7	,25	,12	,6	,6	,16	,10	,15	,14},
            {8	,5	,6	,13	,9	,0	,2	,13	,12	,16	,9	,11	,23	,10	,21},
            {11	,9	,8	,14	,7	,2	,0	,8	,18	,4	,13	,14	,14	,17	,15},
            {7	,10	,14	,14	,25	,13	,8	,0	,9	,16	,42 ,3	,14	,14	,27},
            {15	,8	,13	,17	,12	,12	,18	,9	,0	,15	,16	,13	,14	,7	,17},
            {23	,17	,4	,15	,6	,16	,4	,16	,15	,0	,28	,5	,19	,6	,18},
            {14	,11	,14	,14	,6	,9	,13	,42	,16	,28	,0	,13	,4	,13	,2},
            {15	,13	,8	,6	,16	,11	,14	,3	,13	,5	,13	,0	,11	,19	,13},
            {17	,12	,15	,24	,10	,23	,14	,14	,14	,19	,4	,11	,0	,15	,12},
            {9	,16	,32	,13	,15	,10	,17	,14	,7	,6	,13	,19	,15	,0	,16},
            {21	,15	,16	,4	,14	,21	,15	,27	,17	,18	,2	,13	,12	,16	, 0}};

    //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
    // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
    Problem<ArrayList<Integer>> QK= new QuadraticKnapsack(p,value,weight,110);
    Metaheuristic<ArrayList<Integer>> LS=new Metaheuristic<>(QK);
    Results<ArrayList<Integer>> HC=LS.Hillclimber(2, 1);
    QK.PrintSolution(HC.finalSolution);
    HC.PrintBest();
}
