 
 ```
 #include <stdio.h>
 // 상호 배타적으로 표현을 해야하므로 0000 0011 같은 형태가 아니라 00    00 0100으로 표현

 // 함수로 만들면 성능에 저하가 생기므로 매크로로 생성

#define ADD_ITEM(items, item)


 #define HAS_ITEM(items, item) \
   items & item

 enum {
 SWORD = 1 << 0,
 DAGGER = 1 << 1,
    SHIELD = 1 << 2, //  0000 0100
    MAX_ITEM_COUNT = 100,
  }
 
 typedef unsigned int bitset[MAX_ITEM_COUNT / (sizeof(unsigned int) *     8) + 1];
  // 나눠 떨어지는 숫자라면 +1 안해줘도 된다 ex) 1024
 
 
  // 0 ~ 99
  // 50
  //   index: 50 / 32 => 1
  //  offset: 50 % 32 => 18
 
 
  struct user {
   unsigned char items;  // 아이템을 8개밖에 표현 못하는 문제점이 있>    다.
    bitset items;   // unsigned int [갯수] 타입 (배열)
  };
 
 
  int main() {
 
 
    user.items &= ~SWORD;
 
    // & : 비트단위로 AND 연산을 한다
    // | : 비트단위로 OR 연산을 한다
    // ~ : 단항 연산자로서 피연산자의 모든 비트를 반전시킨다.
 
    // |=  아이템을 더하기
   // &   아이템 있는 지 확인
    // &= ~  아이템 지우기
 
    // 배열의 decay 때문에 구조체로 자료형 선언
 
  #define ADD_ITEM(items, item) \
    items[item /32] |= 1 << (item % 32)
 
  #define REMOVE_ITEM(items, item) \
    items[item / 32] &= ~(1 << (item % 32))
 
  #define HAS_ITEM(items, item)
 
    // 을 아래로 변환
 
 #define ADD_ITEM(bitset, item) \
      (bitset)->data[(item) / 32] |= 1 << ((item) % 32)
 
  #define REMOVE_ITEM(bitset, item) \
      (bitset)->data[(item) / 32] &= ~((item) %32)
 
      // 0100 1001
      // ~(0000 000)
      // 1111 1110
 
 
  }
~
```
