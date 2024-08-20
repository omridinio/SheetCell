package body.impl;

import body.Cell;
import body.Coordinate;

import java.util.*;

public class Graph {
    private Map<Coordinate,List<Coordinate>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }


    public void addVertex(Coordinate coordinate) {
        graph.putIfAbsent(coordinate, new LinkedList<>());
    }
    //TODO maybe change the "to" to CELL
    public void addEdge(Coordinate from, Coordinate to) {
        graph.get(from).add(to);
    }

    public void removeEdge(Coordinate from, Coordinate to) {
        List<Coordinate> neighbors = graph.get(from);
        if (neighbors != null) {
            neighbors.remove(to);
        }
    }

    public List<Coordinate> topologicalSort() {
        List<Coordinate> sortedList = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        Stack<Coordinate> stack = new Stack<>();

        for (Coordinate vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                topologicalSortUtil(vertex, visited, stack);
            }
        }

        // Pop elements from the stack to get the sorted order
        while (!stack.isEmpty()) {
            sortedList.add(stack.pop());
        }

        return sortedList;
    }

    private void topologicalSortUtil(Coordinate vertex, Set<Coordinate> visited, Stack<Coordinate> stack) {
        visited.add(vertex);

        // Recur for all the adjacent vertices
        for (Coordinate neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(neighbor, visited, stack);
            }
        }

        // Push current vertex to stack which stores result
        stack.push(vertex);
    }

    public boolean hasCycle() {
        Set<Coordinate> visited = new HashSet<>();
        Set<Coordinate> recursionStack = new HashSet<>();

        for (Coordinate vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleUtil(vertex, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasCycleUtil(Coordinate vertex, Set<Coordinate> visited, Set<Coordinate> recursionStack) {
        // Mark the node as visited and add it to the recursion stack
        visited.add(vertex);
        recursionStack.add(vertex);

        // Recur for all neighbors
        for (Coordinate neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleUtil(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                // If neighbor is in recursion stack, then there is a cycle
                return true;
            }
        }
        // Remove from recursion stack before returning
        recursionStack.remove(vertex);
        return false;
    }
}
