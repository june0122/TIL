package Janggi;

import java.io.IOException;
import java.util.Scanner;

public class Board {
    static int N;
    static int cnt = 1;
    static boolean checkEnemy = false;
    static Horse nationInst;
    static Horse enemyInst;
    static String nation;
    static Horse han = new Horse();
    static Horse cho = new Horse();
    static Horse next = new Horse();

    public static void main(String[] args) {
        scanN();
        setNation();
        while (!checkEnemy) {
            switchNation();
            attack();
        }

        if (checkEnemy) {
            System.out.println("공격 성공! " + nation + " 승리!");
            System.out.println();
            System.out.println("게임이 끝났습니다.!!!" + '\n' + "다시 하시겠습니까? (y/n)");
            System.out.println("> ");

        }

    }

    public static void scanN() {
        Scanner scanner = new Scanner(System.in);   // N값을 받아오기 위한 Scanner 객체
        System.out.print("N 값을 입력하세요! ");    // 격자 보드 크기 설정
        N = scanner.nextInt();
    }

    public static void setNation() {
        nation = "한나라";
        nationInst = han;
        enemyInst = cho;

        nationInst.x = 0;
        enemyInst.x = N - 1;

        if ((N % 2) == 0) {
            nationInst.y = N / 2;
            enemyInst.y = (N / 2) + 1;
        } else {
            nationInst.y = N / 2;
            enemyInst.y = N / 2;
        }

}
    // 공격 전환
    public static void switchNation() {
        if ((cnt++ % 2) == 0) {
            nationInst = cho;
            enemyInst = han;
            nation = "초나라";

        } else {
            nationInst = han;
            enemyInst = cho;
            nation = "한나라";
        }
    }

    public static void attack() {
        int keyCode = 0;

        System.out.print("> " + nation + " 공격! enter키를 누르세요!");
        try {
            keyCode = System.in.read();
        } catch (IOException e) {
        }

        if (keyCode == 13 || keyCode == 10) {
            System.out.println("현재위치: (" + nationInst.x + ", " + nationInst.y + ")");
        }

        System.out.println("이동 가능한 위치: ");
        nationInst.nextPos(nationInst, next);

    }

}
