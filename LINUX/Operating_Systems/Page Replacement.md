# Page Replacement

- When a page fault occurs, the OS loads the faulted page from disk into a page frame of memory

- At some point, processes have used all page frames available in the system

- OS must replace a page for each page faulted in
  - Evict a page (victim frame) to free up a page frame

- **Which page in physical memory should be selected as a victim?**
  - Page replacement algorithm (or policy)

---

- The page fault penalty (cost of disk access) is so high
  - E.g., memory access: 200ns, average disk accesses: 8ms

- A tiny miss rate quickly dominates the overall effective access time
  - p = page fault rate, 0 ≤ p ≤ 1
  - Effective access time = p × (page fault handling time)+ (1 – p) × memory access
  - If p = 0.001, effective access time = 8.2us
    - **×40 slower than memory access**!!

---

- Goal of page replacement policies is **to minimize the page fault rate**

- The best page to evict is the one that will never be touched again

- “Never” is a long time, so picking the page **closest to “never”** is the next best thing

- Also, memory references patterns vary between processes and applications

## OPT <sup>optimal</sup>

> Also known as **Belady’s Algorithm**

- **Replace the page that will not used for the longest period in the future**

- The optimal page replacement algorithm
  - **Has the lowest fault rate for any page reference stream** (그 어떤 page reference policy보다 더 낮은 오류율을 가짐)

- 문제점: Must predict the future
  - 미래를 추측할 수 있어야하므로 평가 기준으로 사용될 순 있으나실제로 시스템에 적용하기엔 어려움이 있다.
  - Thus, use it as a yardstick to compare other algorithms to measure the room for improvement

### Example

- With 3 page frames
  - 왼쪽에서 4번째 time에서 4를 참조하기 위해 3을 뺀 이유는 Reference string에서 `3`이 1과 2보다도 더 미래에 참조가 되므로 3을 replace한다.
  - 그 다음인 5번째 time에서 1을 참조할 때, page frame에 존재하므로 page fault가 발생하지 않아 `HIT`이다.
  - 12개의 참조 중 Hit 횟수가 5 이므로 Hit ratio는 `5/12`가 되고 Page fault ratio는 `7/12`가 된다.
    - Page fault ratio는 낮을수록 좋다.

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85954448-6ea75a00-b9b2-11ea-84fc-3dd5419285a7.png'>
</p>
<br>

## FIFO

> First-In First-Out

- Obvious and simple to implement
  - Maintain a list of pages in order they were paged in
  - On replacement, **evict the one brought in longest time ago**

- Why might this be good?
  - Maybe the one brought in the longest ago will not be used

- Why might this be bad?
  - Maybe, it’s not the case
  - We don’t have any information either way

### FIFO Example

- FIFO suffers from **Belady’s Anomaly<sup>벨레이디의 모순</sup>**
  - 메모리가 늘어날수록 page fault rate가 높아지는 현상 
  - 아래의 예제에서 3개의 frame일땐 fault가 9번, 4개의 frame일땐 fault가 10번 발생한다.
  - Belady’s Anomaly가 있는 page replacement policy는 **좋은 property를 가졌다고 볼 수 없다.**

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85954848-6d2b6100-b9b5-11ea-9e9b-42ab195d2d41.png'>
&ensp;
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/85954867-8207f480-b9b5-11ea-9559-9ae2bdaf793e.png'>
</p>
<br>


## LRU

> **Least Recently Used**
 
- 가장 최근에 사용되지 않은 page를 뺀다.
  - OPT와 달리 **과거**를 보고 미래 행적을 판단
  - LRU는 Belady’s Anomaly를 겪지 않는 간단한 page replacement algorithm 중 하나이다.
- **Evict the page that has not been used for the longest time in the [past]()**
- **Use past to predict the future**
  - Experience in the past gives us a good guess of future behavior
  - C.f., Belady’s want to look at future
- An example of **stack algorithms**
  - Any page in memory with n frames is also in memory with n + 1 frames
  - Guarantee increasing memory size does not increase the number of page faults
    - **Free from the Belady’s anomaly**
    - E.g., OPT, LRU, etc

### LRU Example

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85955319-a5806e80-b9b8-11ea-8862-c3325a3a5198.png'>
</p>
<br>

### LRU 구현

- Implementation 1: Using **clocks or counters**
  - Maintain a clock that increases for every memory reference
  - Every page frame entry has **a time-of-use field**
  - MMU updates the time-of-use field to the current clock value when accesses to a page frame
  - Evict the page frame that has the smallest counter value
  - 장단점 : Fast on memory reference, slow on replacement (especially as the size of memory grows)

- Implementation 2: Using doubly **linked list** or stack
  - Maintain a doubly linked list of page frames
  - Move the page frame to the head of the list when it is accessed
  - Evict the page at the tail
  - 장단점 : Fast on replacement, slow on memory reference

> Computer System의 Page Replacement Policy에 LRU가 Stack Algorithm이라 Belady’s anomaly가 없으므로 LRU로 page를 관리하면 좋겠으나 현실적으로 구현하기엔 overhead가 존재하므로 LRU를 직접 하드웨어에 바로 사용하는 경우는 잘 없다. → 그래서 LRU를 Approximation해서 사용한다.


## LRU Approximation Algorithms

### Additional-Reference-Bits Algorithm

- Each PTE has a **reference bit**
  - MMU automatically sets the bit when it accesses the page

<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85955500-f04eb600-b9b9-11ea-8646-ffe262a2ccca.png'>
</p>
<br>

- OS maintains an in-memory table that can store n-bits for each page frame
- OS periodically checks the reference bit of each page frame
- Shift the reference bit into the highest-order bit of the table value and discard its lowest-order bit
  - R = 1, v = 0b 10110101 → v = 0b 11011010
- Evict the page with the smallest value

### Second-Chance Algorithm

- Also known as ”clock” algorithm
- Use Reference bit in each PTE
- Arrange page frames into a big circle
- A clock **hand** points to the page to inspect on a page fault
  - If (R == 0), evict the page
  - If (R == 1), **clear the bit** and go to the next page (**second change**)

<br>
<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/85955692-4f60fa80-b9bb-11ea-947e-6d2440927e41.png'>
</p>
<br>

## Counting-based Page Replacement

> Frequency-based, Counting-based Page Replacement도 존재

- Least Frequently Used (LFU)
  - Replace the page with the smallest reference count (제일 적게 참조된 페이지를 뺀다)
  - Difficult to adopt the changes in references
  - E.g., Some pages accessed heavily during the initialization phase only

- Most Frequently Used (MFU)
  - Probably just brought in and has yet to be used

- Problems
  - Expensive to implement
  - **Workload adaptation**이 좋지 못하다.
  - Does not approximate OPT well