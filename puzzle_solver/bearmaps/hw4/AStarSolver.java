package bearmaps.hw4;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private double timeSpent;
    private int numExplored;
    private ArrayHeapMinPQ<Vertex> pq;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        solution = new ArrayList<>();
        pq = new ArrayHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        numExplored = -1;
        Stopwatch sw = new Stopwatch();
        if (start.equals(end)) {
            solution.add(start);
            outcome = SolverOutcome.SOLVED;
            solutionWeight = 0.0;
            timeSpent = sw.elapsedTime();
            numExplored = 0;
        } else {
            distTo.put(start, 0.0);
            pq.add(start, 0.0);
            Vertex v = start;
            while (timeSpent < timeout) {
                if (pq.size() == 0) {
                    break;
                }
                v = pq.removeSmallest();
                numExplored += 1;
                if (v.equals(end)) {
                    break;
                }
                List<WeightedEdge<Vertex>> neighbors = input.neighbors(v);
                for (WeightedEdge<Vertex> e : neighbors) {
                    relax(input, e, end);
                }
                timeSpent = sw.elapsedTime();
            }
            solutionWeight = distTo.get(v);
            findEdges(v, start, new ArrayList<>());
            solution.add(end);
            if (v.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                timeSpent = sw.elapsedTime();
            } else if (timeSpent == timeout) {
                outcome = SolverOutcome.TIMEOUT;
                timeSpent = sw.elapsedTime();
                solutionWeight = 0.0;
                solution.clear();
            } else {
                outcome = SolverOutcome.UNSOLVABLE;
                timeSpent = sw.elapsedTime();
                solutionWeight = 0.0;
                solution.clear();
            }
        }
    }

    private void findEdges(Vertex v, Vertex start, List<Vertex> reverse) {
        Vertex e = edgeTo.get(v);
        reverse.add(e);
        if (!start.equals(e) && !start.equals(v)) {
            findEdges(e, start, reverse);
        } else {
            for (int i = reverse.size() - 1; i >= 0; i -= 1) {
                solution.add(reverse.get(i));
            }
        }
    }

    private void relax(AStarGraph<Vertex> graph, WeightedEdge<Vertex> neighbor, Vertex goal) {
        Vertex p = neighbor.from();
        Vertex q = neighbor.to();
        double w = neighbor.weight();
        double qDist = Double.POSITIVE_INFINITY;
        boolean contains = false;
        if (distTo.containsKey(q)) {
            qDist = distTo.get(q);
            contains = true;
        }
        double pDist = distTo.get(p) + w;
        if (pDist < qDist) {
            qDist = pDist;
            if (contains) {
                distTo.replace(q, qDist);
                edgeTo.replace(q, p);
            } else {
                distTo.put(q, qDist);
                edgeTo.put(q, p);
            }
            double heuristic = graph.estimatedDistanceToGoal(q, goal);
            if (pq.contains(q)) {
                pq.changePriority(q, qDist + heuristic);
            } else {
                pq.add(q, qDist + heuristic);
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numExplored;
    }

    @Override
    public double explorationTime() {
        return timeSpent;
    }
}
