 
 ```
 3 #include <stdio.h>
  4
  5 // 상호 배타적으로 표현을 해야하므로 0000 0011 같은 형태가 아니라 00    00 0100으로 표현
  6
  7 // 함수로 만들면 성능에 저하가 생기므로 매크로로 생성
  8
  9 #define ADD_ITEM(items, item)
 10
 11
 12
 13 #define HAS_ITEM(items, item) \
 14   items & item
 15
 16 enum {
 17   SWORD = 1 << 0,
 18   DAGGER = 1 << 1,
 19   SHIELD = 1 << 2, //  0000 0100
 20   MAX_ITEM_COUNT = 100,
 21 }
 22
 23 typedef unsigned int bitset[MAX_ITEM_COUNT / (sizeof(unsigned int) *     8) + 1];
 24 // 나눠 떨어지는 숫자라면 +1 안해줘도 된다 ex) 1024
 25
 26
 27 // 0 ~ 99
 28 // 50
 29 //   index: 50 / 32 => 1
 30 //  offset: 50 % 32 => 18
 31
 32
 33 struct user {
34   unsigned char items;  // 아이템을 8개밖에 표현 못하는 문제점이 있>    다.
 35   bitset items;   // unsigned int [갯수] 타입 (배열)
 36 };
 37
 38
 39 int main() {
 40
 41
 42   user.items &= ~SWORD;
 43
 44   // & : 비트단위로 AND 연산을 한다
 45   // | : 비트단위로 OR 연산을 한다
 46   // ~ : 단항 연산자로서 피연산자의 모든 비트를 반전시킨다.
 47
 48   // |=  아이템을 더하기
 49   // &   아이템 있는 지 확인
 50   // &= ~  아이템 지우기
 51
 52
 53   // 배열의 decay 때문에 구조체로 자료형 선언
 54
 55 #define ADD_ITEM(items, item) \
 56   items[item /32] |= 1 << (item % 32)
 57
 58
 59 #define REMOVE_ITEM(items, item) \
 60   items[item / 32] &= ~(1 << (item % 32))
 61
 62 #define HAS_ITEM(items, item)
 63
 64     // 을 아래로 변환
 65
 66 #define ADD_ITEM(bitset, item) \
 67     (bitset)->data[(item) / 32] |= 1 << ((item) % 32)
 68
 69 #define REMOVE_ITEM(bitset, item) \
 70     (bitset)->data[(item) / 32] &= ~((item) %32)
 71
 72     // 0100 1001
 73     // ~(0000 000)
 74     // 1111 1110
 75
 76
 77 }
~
```
