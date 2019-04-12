package Janggi;

import java.io.IOException;
import java.util.Scanner;

public class Board {
    static int N;
    static int cnt = 1;
    static Horse han = new Horse();
    static Horse cho = new Horse();
    static Horse next = new Horse();

    public static void main(String[] args) {
        scanN();
        attack();
    }

    public static void scanN() {
        Scanner scanner = new Scanner(System.in);   // N값을 받아오기 위한 Scanner 객체
        System.out.print("N 값을 입력하세요! ");    // 격자 보드 크기 설정
        N = scanner.nextInt();
    }

    private static void attack() {
        int keyCode = 0;
        String nation;
        Horse nationInst;

        if ((cnt++ % 2) == 0) {
            nationInst = cho;
            nation = "초나라";
            if ((N % 2) == 0) {
                nationInst.y = (N / 2) + 1;
            } else {
                nationInst.y = (N / 2) + 1;
            }
        } else {
            nationInst = han;
            nation = "한나라";
            if ((N % 2) == 0) {
                nationInst.y = (N / 2) + 1;
            } else {
                nationInst.y = (N / 2);
            }
        }

        System.out.print("> " + nation + " 공격! enter키를 누르세요!");

        try {
            keyCode = System.in.read();
        } catch (IOException e) {
        }  // 키보드의 키코드를 읽음

        if (keyCode == 13 || keyCode == 10) {
            System.out.println("현재위치: (" + nationInst.x + ", " + nationInst.y + ")");
        }

        nationInst.nextPos(nationInst, next);

    }
}