package Janggi;

import java.util.Scanner;

import static Janggi.Board.N;

public class Horse {
    Board board = new Board();
    int x = 0;
    int y = 0;

    public Horse() {
    }

    public double distance(Horse p1, Horse p2) {
        return Math.sqrt(Math.pow(Math.abs(p1.x - p2.x), 2) + Math.pow(Math.abs(p1.y - p2.y), 2));
    }

    public void nextPos(Horse p1, Horse p2) {
        System.out.print("이동 가능한 위치: ");
        for (p2.y = 0; p2.y < N; p2.y++) {
            for (p2.x = 0; p2.x < N; p2.x++) {
                if ((int) Math.pow(distance(p1, p2), 2) == 5) {
                    System.out.print("(" + p2.x + ", " + p2.y + ") ");
                }
            }
        }
        System.out.println();
    }
}