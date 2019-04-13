package Janggi;

import java.util.*;

import static Janggi.Board.*;

public class Horse {
    Board board = new Board();
    Set<Horse> horseSet = new HashSet<Horse>();  // 이동 가능한 위치를 저장하기 위한 HashSet
    int x = 0;
    int y = 0;

    // 기본 생성자
    public Horse() {
    }

    public Horse(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 말의 위치 얻어오기
    public void getPos(Horse h1) {
        System.out.print("(" + h1.x + ", " + h1.y + ") ");
    }

    // 두 좌표 사이의 거리 구하기
    public double distance(Horse h1, Horse h2) {
        return Math.sqrt(Math.pow(Math.abs(h1.x - h2.x), 2) + Math.pow(Math.abs(h1.y - h2.y), 2));
    }

    // 이동 가능 위치 구하기
    public void nextPos(Horse h1, Horse h2) {
        horseSet.clear();
        for (h2.y = 0; h2.y < N; h2.y++) {
            for (h2.x = 0; h2.x < N; h2.x++) {
                if ((int) Math.pow(distance(h1, h2), 2) == 5) {
                    horseSet.add(new Horse(h2.x, h2.y)); // horseSet.add(h2); 의 문제점은?
                }
            }
        }
        for (Horse hor : horseSet) {
            getPos(hor);
        }

        System.out.println();
        checkEnemy = horseSet.contains(enemyInst);

        if (!checkEnemy) {
            Iterator<Horse> horseIterator = horseSet.iterator();
            Horse pickNextPos = horseIterator.next();
                getPos(pickNextPos);
                System.out.println("으로 이동");
            nationInst = pickNextPos;
        }
        System.out.println();
    }

}
