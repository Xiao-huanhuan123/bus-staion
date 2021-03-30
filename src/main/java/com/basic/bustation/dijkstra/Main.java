package com.basic.bustation.dijkstra;

/**
 * Created by hp-pc on 2021/2/10.
 */

public class Main {
    public static void main(String[] args) {
        DijkstraAlgorithm test=new DijkstraAlgorithm();
        Node start=test.init();
        test.computePath(start);
        test.printPathInfo();
    }
}
