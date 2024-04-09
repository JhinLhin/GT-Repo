import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;

/**
 * Your implementation of various different graph algorithms.
 *
 * @author Jinlin Yang
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth first search (bfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * You may import/use java.util.Set, java.util.List, java.util.Queue, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for BFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the bfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    //queue 里面是visited被删除是traversal的
    public static <T> List<Vertex<T>> bfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        Set<Vertex<T>> visited = new HashSet<>(); //O(1)
        List<Vertex<T>> traversal = new ArrayList<>();
        Queue<Vertex<T>> queue = new LinkedList<>();
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {// u could add visited.size() <= graph.getVertices().size(),
            // notice it is <= unlike dijk which is < because here we add to traversal then add to visited,
            // if only < then we would not be able to add the last vertex to visited
            //for exam reason, I don't add it to avoid confusion.
            Vertex<T> removed = queue.poll();
            traversal.add(removed);
            for (VertexDistance<T> element : graph.getAdjList().get(removed)) {
                if (!visited.contains((element.getVertex()))) {
                    visited.add(element.getVertex());
                    queue.add(element.getVertex());
                }
            }
        }
        return traversal;
    }


    /**
     * Performs a depth first search (dfs) on the input graph, starting at
     * the parameterized starting vertex.
     *
     * When exploring a vertex, explore in the order of neighbors returned by
     * the adjacency list. Failure to do so may cause you to lose points.
     *
     * *NOTE* You MUST implement this method recursively, or else you will lose
     * all points for this method.
     *
     * You may import/use java.util.Set, java.util.List, and
     * any classes that implement the aforementioned interfaces, as long as they
     * are efficient.
     *
     * The only instance of java.util.Map that you may use is the
     * adjacency list from graph. DO NOT create new instances of Map
     * for DFS (storing the adjacency list in a variable is fine).
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the dfs on
     * @param graph the graph to search through
     * @return list of vertices in visited order
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> dfs(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        Set<Vertex<T>> visited = new HashSet<>();
        List<Vertex<T>> traversal = new ArrayList<>();
        dfsHelper(start, graph, visited, traversal);
        return traversal;
    }

    /**
     * helper method for dfs
     *
     * @param <T> the generic typing of the data
     * @param vertex the vertex to begin the dfs on
     * @param graph the graph to search through
     * @param visited the set of visited vertex
     * @param traversal the list that is returned
     */
    private static <T> void dfsHelper(Vertex<T> vertex, Graph<T> graph,
                                      Set<Vertex<T>> visited, List<Vertex<T>> traversal) {
        visited.add(vertex);
        traversal.add(vertex);
        for (VertexDistance<T> element : graph.getAdjList().get(vertex)) {
            if (!visited.contains((element.getVertex()))) {
                dfsHelper(element.getVertex(), graph, visited, traversal);
            }
        }
    }

    /**
     * Finds the single-source shortest distance between the start vertex and
     * all vertices given a weighted graph (you may assume non-negative edge
     * weights).
     *
     * Return a map of the shortest distances such that the key of each entry
     * is a node in the graph and the value for the key is the shortest distance
     * to that node from start, or Integer.MAX_VALUE (representing
     * infinity) if no path exists.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Map, and java.util.Set and any class that
     * implements the aforementioned interfaces, as long as your use of it
     * is efficient as possible.
     *
     * You should implement the version of Dijkstra's where you use two
     * termination conditions in conjunction.
     *
     * 1) Check if all of the vertices have been visited.
     * 2) Check if the PQ is empty yet.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param start the vertex to begin the Dijkstra's on (source)
     * @param graph the graph we are applying Dijkstra's to
     * @return a map of the shortest distances from start to every
     * other node in the graph
     * @throws IllegalArgumentException if any input is null, or if start
     *                                  doesn't exist in the graph.
     */
    // for VerTex distance's get distance, what if there are more than 1 distance how do i know which on it is

    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
                                                        Graph<T> graph) {
        if (start == null || graph == null || !graph.getAdjList().containsKey(start)) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        Set<Vertex<T>> visited = new HashSet<>(); // more efficient for checking existence than arraylist
        Map<Vertex<T>, Integer> distanceMap = new HashMap<>();
        PriorityQueue<VertexDistance<T>> pQ = new PriorityQueue<>();
        for (Vertex<T> vertex: graph.getVertices()) {
            distanceMap.put(vertex, Integer.MAX_VALUE);
        }
        distanceMap.put(start, 0);
        pQ.add(new VertexDistance<>(start, 0));
        while (!pQ.isEmpty() && visited.size() < graph.getVertices().size()) { // here it is less but not <= because we visit it then we add it
            VertexDistance<T> temp = pQ.poll();
            if (!visited.contains(temp.getVertex())) {
                visited.add(temp.getVertex());
                for (VertexDistance<T> neighbor : graph.getAdjList().get(temp.getVertex())) {
                    if (!visited.contains(neighbor.getVertex())) {
                        int newDistance = distanceMap.get(temp.getVertex()) + neighbor.getDistance();
                        if (newDistance < distanceMap.get(neighbor.getVertex())) { //neighbors are vertexDistance
                            distanceMap.put(neighbor.getVertex(), newDistance);
                            pQ.add(new VertexDistance<>(neighbor.getVertex(), newDistance)); // we make a new instance to make sure PQ rank them correctly, and algo runs correctly
                        }
                    }
                }
            }
        }
        return distanceMap;
    }

    /**
     * Runs Kruskal's algorithm on the given graph and returns the Minimal
     * Spanning Tree (MST) in the form of a set of Edges. If the graph is
     * disconnected and therefore no valid MST exists, return null.
     *
     * You may assume that the passed in graph is undirected. In this framework,
     * this means that if (u, v, 3) is in the graph, then the opposite edge
     * (v, u, 3) will also be in the graph, though as a separate Edge object.
     *
     * The returned set of edges should form an undirected graph. This means
     * that every time you add an edge to your return set, you should add the
     * reverse edge to the set as well. This is for testing purposes. This
     * reverse edge does not need to be the one from the graph itself; you can
     * just make a new edge object representing the reverse edge.
     *
     * You may assume that there will only be one valid MST that can be formed.
     *
     * Kruskal's will also require you to use a Disjoint Set which has been
     * provided for you. A Disjoint Set will keep track of which vertices are
     * connected given the edges in your current MST, allowing you to easily
     * figure out whether adding an edge will create a cycle. Refer
     * to the DisjointSet and DisjointSetNode classes that
     * have been provided to you for more information.
     *
     * An MST should NOT have self-loops or parallel edges.
     *
     * By using the Disjoint Set provided, you can avoid adding self-loops and
     * parallel edges into the MST.
     *
     * You may import/use java.util.PriorityQueue,
     * java.util.Set, and any class that implements the aforementioned
     * interfaces.
     *
     * DO NOT modify the structure of the graph. The graph should be unmodified
     * after this method terminates.
     *
     * @param <T>   the generic typing of the data
     * @param graph the graph we are applying Kruskals to
     * @return the MST of the graph or null if there is no valid MST
     * @throws IllegalArgumentException if any input is null
     */
    public static <T> Set<Edge<T>> kruskals(Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot take in null!");
        }
        DisjointSet<T> disjointSet = new DisjointSet<>(); // initially all individual then combine to 1
        Set<Edge<T>> mST = new HashSet<>();
        PriorityQueue<Edge<T>> pQ = new PriorityQueue<>(graph.getEdges());
        while (!pQ.isEmpty() && mST.size() < 2 * (graph.getVertices().size() - 1)) {
            Edge<T> removed = pQ.poll();
            if (!(disjointSet.find(removed.getU().getData()).equals(disjointSet.find(removed.getV().getData())))) {
                mST.add(removed);
                mST.add(new Edge<>(removed.getV(), removed.getU(), removed.getWeight()));
                disjointSet.union(removed.getU().getData(), removed.getV().getData());
            }
        }
        if (mST.size() < 2 * (graph.getVertices().size() - 1)) {
            return null;
        }
        return mST;
    }
}
