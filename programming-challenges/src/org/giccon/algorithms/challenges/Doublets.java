package org.giccon.algorithms.challenges;

/* Solution: graph, breadth first search, depth first search */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Doublets {
    private static class Graph {
        /**
         * Vertices must be number from 0..n, where n+1 is the number of
         * vertices contained in a graph.
         */
        private static class Vertex {
            private int id;
            private String name;

            public Vertex(int id, String name) {
                this.id = id;
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }

        private static class Edge {
            private Vertex v1;
            private Vertex v2;

            public Edge(Vertex v1, Vertex v2) {
                this.v1 = v1;
                this.v2 = v2;
            }

            private Vertex getOppositeVertex(Vertex v) {
                if (v.getId() == v1.getId()) {
                    return v2;
                } else if (v.getId() == v2.getId()) {
                    return v1;
                }

                return null;
            }
        }

        private List<Vertex> vertices = new ArrayList<Vertex>();
        // Contains the edges per vertex id.
        private List<List<Edge>> adjacencyList = new ArrayList<List<Edge>>();

        public int getNumberOfVertices() {
            return vertices.size();
        }

        public boolean isEmpty() {
            return vertices.size() == 0;
        }

        /**
         * Each vertex id must be unique.
         *
         * @param id   identifier of the vertex
         * @param name name of the vertex
         */
        public void addVertex(int id, String name) {
            Vertex v = new Vertex(id, name);
            vertices.add(v);
            // Initialize the adjacency list for this vertex.
            adjacencyList.add(new LinkedList<Edge>());
        }

        public void addEdge(int n1, int n2) {
            Vertex v1 = vertices.get(n1);
            Vertex v2 = vertices.get(n2);

            Edge edge = new Edge(v1, v2);
            adjacencyList.get(v1.id).add(edge);
            adjacencyList.get(v2.id).add(edge);
        }

        public void dfs(Visitor visitor, int start) {
            boolean[] visited = new boolean[vertices.size()];
            dfs(visitor, vertices.get(start), visited);
        }

        private void dfs(Visitor visitor, Vertex v1, boolean visited[]) {
            visitor.visit(v1.getName());
            int n1 = v1.getId();
            visited[n1] = true;
            for (Edge edge : adjacencyList.get(n1)) {
                Vertex v2 = edge.getOppositeVertex(v1);
                int n2 = v2.getId();

                if (!visited[n2]) {
                    dfs(visitor, v2, visited);
                }
            }
        }

        /**
         * Returns the path as a graph. The vertices in the returned path are
         * numbered in reverse order, meaning that end will have id (0) and
         * start will have id (number of vertices in the returned graph - 1).
         * BFS (breadth first search) is used to find the shortest path. If no
         * such path exists then the graph is empty and has no vertices.
         *
         * @param start the start vertex
         * @param end   the end vertex
         * @return a graph containing the shortest path from start to end.
         */
        public Graph getShortestPath(int start, int end) {
            Vertex vStart = vertices.get(start);
            Vertex vEnd = vertices.get(end);
            Queue<Vertex> queue = new ArrayDeque<Vertex>();
            boolean[] visited = new boolean[vertices.size()];
            Vertex[] parent = new Vertex[vertices.size()];

            /*
             * Find the shortest path from start to end. Store the parent
             * vertices of each vertex in the parent data structures. Stop when
             * the end vertex is reached.
             */
            queue.offer(vStart);
            boolean isFinished = false;
            while (!queue.isEmpty() && !isFinished) {
                Vertex v1 = queue.poll();
                int n1 = v1.getId();
                visited[n1] = true;

                for (Edge edge : adjacencyList.get(n1)) {
                    Vertex v2 = edge.getOppositeVertex(v1);
                    int n2 = v2.getId();

                    if (!visited[n2]) {
                        queue.offer(v2);
                        if (parent[n2] == null) {
                            parent[n2] = v1;
                        }
                    }

                    // end vertex is reached. Hence, no need to continue with
                    // the search.
                    if (n2 == end) {
                        isFinished = true;
                        break;
                    }
                }
            }

            Graph graph = new Graph();
            if (parent[vEnd.getId()] != null) {
                // Create a graph containing the path
                // from end to start.
                Vertex v1 = vEnd;
                int id = 0;
                int n1 = id;
                graph.addVertex(n1, v1.getName());
                id++;
                while (parent[v1.getId()] != null) {
                    Vertex v2 = parent[v1.getId()];
                    int n2 = id;
                    graph.addVertex(n2, v2.getName());
                    id++;
                    graph.addEdge(n1, n2);

                    v1 = v2;
                    n1 = n2;
                }
            }

            return graph;
        }
    }

    private interface Visitor {
        public void visit(String word);
    }

    private static final int MAXLETTERS = 16;

    public static void main(String[] args) {
        Doublets.begin();
    }

    private static void begin() {
        Scanner sc = new Scanner(System.in);

        // Partition the words according to their length.
        List<Set<String>> wordsDict = new ArrayList<Set<String>>();
        // Word to the vertex is mapper.
        Map<String, Integer> wordToVertexId = new HashMap<String, Integer>();
        // A unique graph for each length of a word.
        Graph[] graphs = new Graph[MAXLETTERS + 1];
        // Identifiers for the vertices per graph.
        int[] ids = new int[MAXLETTERS + 1];

        for (int i = 0; i <= MAXLETTERS; ++i) {
            wordsDict.add(new HashSet<String>());
            graphs[i] = new Graph();
        }

        // Read in the words.
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            if (word.isEmpty()) {
                break;
            }

            int length = word.length();
            Set<String> words = wordsDict.get(length);
            if (words.isEmpty()) {
                Graph graph = graphs[length];
                graph.addVertex(ids[length], word);

                wordToVertexId.put(word, ids[length]);
                words.add(word);
                ids[length]++;
            } else if (!words.contains(word)) {
                Graph graph = graphs[length];
                graph.addVertex(ids[length], word);

                for (String w : words) {
                    if (isDoublet(word, w)) {
                        graph.addEdge(wordToVertexId.get(w), ids[length]);
                    }
                }

                wordToVertexId.put(word, ids[length]);
                words.add(word);
                ids[length]++;
            }
        }

        // Read in the pair of words and output the path.
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine());
            String start = st.nextToken();
            String end = st.nextToken();

            if (!wordToVertexId.containsKey(start)
                    || !wordToVertexId.containsKey(end)) {

                System.out.println("No solution.");
            } else if (start.length() != end.length()) {
                System.out.println("No solution.");
            } else if (start.equals(end)) {
                System.out.println(start);
            } else {
                Graph graph = graphs[start.length()];
                Graph g = graph.getShortestPath(wordToVertexId.get(start),
                        wordToVertexId.get(end));

                if (g.isEmpty()) {
                    System.out.println("No solution.");
                } else {
                    // Buffer used for gaining IO efficiency.
                    final StringBuffer buffer = new StringBuffer();
                    // end = 0 and start = number of vertices - 1
                    g.dfs(new Visitor() {
                        @Override
                        public void visit(String word) {
                            buffer.append(word).append("\n");
                        }
                    }, g.getNumberOfVertices() - 1);
                    System.out.print(buffer);
                }
            }

            if (sc.hasNext()) {
                System.out.println("");
            }
        }
    }

    private static boolean isDoublet(String w1, String w2) {
        int diff = 0;
        for (int i = 0; i < w1.length(); ++i) {
            if (w1.charAt(i) != w2.charAt(i)) {
                diff++;
                if (diff > 1) {
                    return false;
                }
            }
        }

        return true;
    }
}
