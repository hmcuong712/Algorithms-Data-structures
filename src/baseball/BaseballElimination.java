/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {

    private final int nTeam;
    private final int[][] matTeam;
    private final ST<String, Integer> st;
    private final ST<Integer, String> stInverse;
    private ST<Integer, Integer> teamST;
    private final Queue<String> teams;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        nTeam = in.readInt();
        int count = 0;

        matTeam = new int[nTeam][nTeam + 4];
        st = new ST<String, Integer>();
        stInverse = new ST<Integer, String>();

        for (int i = 0; i < nTeam; i++) {
            for (int j = 0; j < nTeam + 4; j++) {
                if (j == 0) {
                    st.put(in.readString(), count);
                    matTeam[count][j] = count;
                    count++;
                }
                else matTeam[i][j] = Integer.parseInt(in.readString());
            }
        }

        for (String team : st.keys()) {
            stInverse.put(st.get(team), team);
        }

        teams = new Queue<String>();
        for (int i = 0; i < numberOfTeams(); i++) teams.enqueue(stInverse.get(i));
    }

    // number of teams
    public int numberOfTeams() {
        return nTeam;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        validate(team);
        int t = st.get(team);
        return matTeam[t][1];
    }

    // number of losses for given team
    public int losses(String team) {
        validate(team);
        int t = st.get(team);
        return matTeam[t][2];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validate(team);
        int t = st.get(team);
        return matTeam[t][3];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);

        int t1 = st.get(team1);
        int t2 = st.get(team2);
        return matTeam[t1][t2 + 4];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        if (isTrivialEliminate(team)) return true;
        FlowNetwork fn = createFlow(team);
        FordFulkerson maxflow = new FordFulkerson(fn, 0, fn.V() - 1);
        for (FlowEdge e : fn.adj(0))
            if (0 == e.from() && e.flow() < e.capacity()) return true;
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        if (!isEliminated(team)) return null;

        Queue<String> certificate = new Queue<String>();
        if (isTrivialEliminate(team)) {
            for (String i : teams()) {
                if (wins(team) + remaining(team) < wins(i)) certificate.enqueue(i);
            }
            return certificate;
        }

        FlowNetwork fn = createFlow(team);
        FordFulkerson maxflow = new FordFulkerson(fn, 0, fn.V() - 1);

        String t;
        for (int i : teamST.keys()) {
            if (maxflow.inCut(teamST.get(i))) {
                t = stInverse.get(i);
                certificate.enqueue(t);
            }
        }
        return certificate;
    }

    private boolean isTrivialEliminate(String team) {
        for (String i : teams()) {
            if (wins(team) + remaining(team) < wins(i)) return true;
        }
        return false;
    }

    private FlowNetwork createFlow(String team) {
        int t = st.get(team);
        // int firstLayer = factorial(nTeam - 1) / (factorial(2) * factorial(nTeam - 3));
        int firstLayer = binomialCoeff(nTeam - 1);
        int secondLayer = nTeam - 1;
        int v = 2 + firstLayer + secondLayer;
        int track = 1;

        teamST = new ST<Integer, Integer>();
        int teamInd = 0;
        for (int m = firstLayer + 1; m <= secondLayer + firstLayer; m++) {
            if (teamInd == st.get(team)) teamInd++;
            teamST.put(teamInd, m);
            teamInd++;
        }

        FlowNetwork fn = new FlowNetwork(v);
        for (int j = 0; j < nTeam - 1; j++) {
            for (int k = j + 1; k < nTeam; k++) {
                if (j != t && k != t) {
                    fn.addEdge(new FlowEdge(0, track, matTeam[j][k + 4]));
                    fn.addEdge(new FlowEdge(track, teamST.get(j), matTeam[j][k + 4]));
                    fn.addEdge(new FlowEdge(track, teamST.get(k), matTeam[j][k + 4]));
                    track++;
                }
            }
        }

        for (int i : teamST.keys()) {
            fn.addEdge(new FlowEdge(teamST.get(i), v - 1,
                                    wins(team) + remaining(team) - matTeam[i][1]));
        }
        return fn;
    }

    private int binomialCoeff(int x) {
        return (x - 1) * x / 2;
    }

    private void validate(String team) {
        if (team == null) throw new IllegalArgumentException();
        if (!st.contains(team)) throw new IllegalArgumentException();
    }

    // testing
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
