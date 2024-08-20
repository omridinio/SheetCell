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
        graph.put(coordinate, new LinkedList<>());
    }
    //TODO maybe change the "to" to CELL
    public void addEdge(Coordinate from, Coordinate to) {
        graph.get(from).add(to);
    }


}
