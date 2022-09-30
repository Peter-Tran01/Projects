package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        // return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        // stores the shortest path tree
        HashMap<V, E> spt = new HashMap<>();
        // returns if start node is equal to end node
        if (start.equals(end)) {
            return spt;
        }
        // Set known, Map edgeTo, Map distTo
        HashSet<V> known = new HashSet<>();
        HashMap<V, V> edgeTo = new HashMap<>();
        HashMap<V, Double> distTo = new HashMap<>();
        // min heap priority queue
        ExtrinsicMinPQ<V> minHeap = createMinPQ();
        distTo.put(start, 0.0);
        minHeap.add(start, 0.0);
        V shortestVertex;
        double oldDist;
        double newDist;
        // parse through unknown vertices
        while (!known.contains(end)) {
            // add the shortest vertex to known set
            if (minHeap.isEmpty()) {
                break;
            }
            shortestVertex = minHeap.removeMin();
            known.add(shortestVertex);
            // discover new edges from the shortest vertex
            for (E edge : graph.outgoingEdgesFrom(shortestVertex)) {
                oldDist = distTo.getOrDefault(edge.to(), Double.POSITIVE_INFINITY);
                newDist = distTo.get(edge.from()) + edge.weight();
                // update distance if shorter
                if (newDist < oldDist) {
                    distTo.put(edge.to(), newDist);
                    edgeTo.put(edge.to(), edge.from());
                    spt.put(edge.to(), edge);
                    if (minHeap.contains(edge.to())) {
                        minHeap.changePriority(edge.to(), newDist);
                    } else {
                        minHeap.add(edge.to(), newDist);
                    }
                }
            }
        }
        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        // return failure if no path exists
        // return only one vertex in path
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (Objects.equals(spt.get(end), null)) {
            return new ShortestPath.Failure<>();
        }
        // add the full edge path to an array list
        List<E> path = new ArrayList<>();
        E currEdge = spt.get(end);
        path.add(currEdge);
        while (currEdge != spt.get(start)) {
            currEdge = spt.get(currEdge.from());
            if (!Objects.equals(currEdge, null)) {
                path.add(currEdge);
            }
        }
        // reverse list
        Collections.reverse(path);
        // return success
        return new ShortestPath.Success<>(path);
    }

}
