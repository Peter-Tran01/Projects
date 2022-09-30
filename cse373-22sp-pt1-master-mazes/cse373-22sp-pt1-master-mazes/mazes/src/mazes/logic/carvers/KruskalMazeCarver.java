package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // create an array of EdgeWithData<Room, Wall>
        HashSet<Wall> newWalls = new HashSet<>();
        ArrayList<EdgeWithData<Room, Wall>> edges = new ArrayList<>();
        // iterate through walls
        for (Wall w : walls) {
            edges.add(new EdgeWithData<>(w.getRoom1(), w.getRoom2(), rand.nextDouble(), w));
        }
        // run kruskal algorithm to get the edges for removal
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> edgesForRemoval
            = this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        for (EdgeWithData<Room, Wall> w : edgesForRemoval.edges()) {
            newWalls.add(w.data());
        }
        // return edges that need to be removed
        return newWalls;
    }
}
