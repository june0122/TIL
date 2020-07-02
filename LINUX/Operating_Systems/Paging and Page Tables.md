# Paging and Page Tables

## Paging

- Process의 Virtual address space를 일정한 크기인 Page로 나눈다.
- Physical Memory도 page와 같은 크기로 쪼갠 것을 Page frame이라 한다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85937782-c35ebc80-b941-11ea-8a22-fcd91d4b91aa.png'>
</p>
<br>

- Permit the physical address space of a process to be non-contiguous

- Divide physical memory into fixed-sized blocks called **“page frames”** or **“frames”**

- Divide logical address space into the same sized blocks called **“pages”**
  - Usually, pages (or frame) size is a power of 2 (typically 512B – 8KB)
  - Some systems allow huge pages (e.g., 2MB and 1GB for x86_64)

- To run a program of size n, find n free frames and load the program

- OS keeps track of all free frames

### 단점

- Still, some internal fragmentation exists
  - Page size = 4 KB
  - Process size = 72,766 bytes
  - 72,766 bytes / 4 KB = 17 pages + 3,134 bytes
  - Internal fragmentation: 4,096(page size) – 3,134 = 962
  - Each process may give up to one-page size – 1 to internal fragmentation
    - In average: ½ of frame(page) size

- Small frame sizes are desirable <sup>바람직한가</sup>?
   - **Need more mapping** entries in the **page table**

## Page Tables

> Page table은 VPN를 PFN로 translate하고 Offset을 그대로 가져가는 역할을 한다.

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85937959-a2976680-b943-11ea-8ea5-d3a0baebb515.png'>
</p>
<br>

- 각각의 프로세스는 자신만의 page table을 가지고 있다. (Each process has its own page table)
  - **스레드들은 address space를 공유하므로 page table도 공유한다.** (page table은 process 수준에서 정의가 되는 표이다.) 
  - **Page-table base register (PTBR)** points to the base address of the page table (e.g., **CR3** in x86 architecture)
  
- **Managed by OS, accessed by MMU**
  - Page table은 Address translation을 할 때, MMU (하드웨어)가 알아서 읽어가는 값이다.
  - Page table은 운영체제(OS)가 관리하고, 실제로 값을 읽어가는 것은 MMU
  - 운영체제과 하드웨어의 교점 부분
  
- One **Page Table Entry (PTE, page table's low)** per page in virtual address space
  
- Map VPN <sup>Virtual Page Number</sup> to PFN <sup>Page Frame Number</sup>
  - Virtual address = <Virtual Page Number (VPN), Offset>
    - VPN: An index into the page table
  - Physical address = <Page Frame Number (PFN), Offset>
  - Page table maps VPN to PFN
  - Usually, |VPN| >= |PFN|

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85938134-2867e180-b945-11ea-93f6-d735f7ecfee3.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85938296-e049be80-b946-11ea-9b16-ece1e5954d2e.png'>
</p>
<br>

- 프로세스가 뿜어내는 Virtual address가 있는데, paging을 사용하면 이 중에 page number part와 남아있는 offset part가 있을 것이다.
- VPN는 PFN로 translate되기 위해 page table로 가야하는데, page table은 Page-table base register <sup>PTBR</sup>가 가리키고 있다.
- Page table은 Offset을 그대로 가져간다.

### Address Translation Example

<br>
<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/85938322-2dc62b80-b947-11ea-8cb9-9da6c20e7f55.png'>
</p>
<br>

- Virtual address: 32 bits
  - 32bits - 12bits = 20bits → VPN
  - 2<sup>20</sup>개의 VPN

- Physical address: 20 bits
  - 20bits - 12bits = 8bits → PFN
  - 2<sup>8</sup>개의 PFN

- Page size: 4 KB
  - 2<sup>2</sup> × 2<sup>10</sup> = 2<sup>12</sup> = 12bits
  - 4Kb → 12bits → 16진수의 3자리
  - 2<sup>10</sup> = Kb, 2<sup>20</sup> = Mb, 2<sup>30</sup> = Gb, 2<sup>40</sup> = Tb ...

- Offset: log<sub>2</sub> 4096 = 12
- VPN: 32 – 12 = 20
- Page table entries: 2<sup>20</sup>

- Page table size : 8bits = 1byte

### PTE

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85938474-c315ef80-b948-11ea-9bee-8c7042ef6b6c.png'>
</p>
<br>

- Page Table Entry
  - **V (Valid)** bit says whether the PTE can be used or not
    - It is checked each time a virtual address is used
  - **R (Reference)** bit says whether the page has been accessed
    - It is set when a read or write to the page occurs
  - **M (Modify)** bit says whether the page is dirty
    - It is set when a write to the page occurs
  - **Prot (Protection)** bits control which operations are allowed
    - Read, Write, Execute, etc.
  - **PFN (Page Frame Number)** determines the physical frame

### Protection

- **Separate** page table for each process
  - On context switch, PTBR is set to the corresponding process’s one
  - No way to access the physical memory of other processes
  
- Page-level protection
  - Memory protection is implemented by associating protection bits with each PTE
  - Valid / invalid bit
    - “Valid”: the page is in the process’ address space and in use
    - “Invalid”: the page is not allocated
  - Finer level of protection is possible for valid pages
    - Read-only, read-write, or execute-only protections

## Page Table Structure

### Linear Page Table

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85938595-c067ca00-b949-11ea-8ad4-86d8208c637d.png'>
</p>

- High space overhead for page tables
  - A 32-bit address space with 4KB pages, 4 bytes/PTE:
    - 2<sup>20</sup> x 4 = 4 MB per process
  - A 64-bit address space with 8KB pages, 8 bytes/PTE:
    - 2<sup>51</sup> x 8 = 2<sup>54</sup> = 16 PB per process!
    - 메모리가 16Pb??? Linear Page Table은 실제 OS나 computer architecture에선 사용이 불가능하다.
  
- Page tables are too big to fit in a single page
  - 4 byte/PTE →  1024 PTEs/4 KB page → 4 MB address space
  
- How can we organize page tables to resolve these issues?

#### Observation

- Only a small portion of the address space is used, and most of the address space is not used at all

#### 해결책

- Split the entire page table into smaller page table pieces
  - Split so that each page table piece fits in a page

- Only map the used portion
- Deploy additional indirection levels to locate the page table pieces
  - 전체 page table을 작은 page 크기로 쪼갠 다음 그 중에서 사용하는 부분만 mapping해서 사용하고, 그렇지 않은 부분은 비워두게 하는 방식.
  - 이렇게 함으로써 multi-level로 page table을 구성할 수 있게 된다.
  - 이런 page table을 **forward-mapped page table (= Hierarchical Page Tables)** 이라 한다.

#### 장점

- page 단위로 쪼갬으로써 page table도 contiguous하게 있을 필요가 없게 되고, 페이지 단위로 여기저기 분산시킬 수 있게 됨. (Thus, the split page tables can be allocated non-contiguously)
- 사용하지 않는 page entry에 대해서는 mapping을 하지 않아도 되기 때문에 page table을 작게 만들 수 있다.
- page table의 조각조각이 각각의 page로 될 수 있기 때문에 운영체제가 memory allocation을 할 때 paging을 사용하면 memory management를 효율적으로 할 수 있게 된다.

<br>

### [Hierarchical Page Tables ★]()

> 현존 하는 거의 모든 Architecture들이 사용하는 Page Table 방식

- Radix tree Data Structure가 기반
- E.g., Two-level Page Tables

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85939062-679a3080-b94d-11ea-8765-bb7de9908eda.png'>
</p>
<br>

- Virtual addresses = <outer page #, page #, page offset>
  - Hierarchical Page Table을 만들면 `기존의 Virtual Address`<VPN, offset> 의 일부를 더 쪼개어 Outer page table을 indexing 하는 용도로 사용할 수 있다.

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85939203-7cc38f00-b94e-11ea-84d7-f3bb409c549d.png'>
</p>
<br>

#### Hierarchical Page Tables Example 1

- 32-bit address space, 4 KB pages, 4 bytes/PTE
  - **4 KB** = 2<sup>12</sup> → `12 bits`
  - 총 커버해야하는 PTE <sup>Page Table Entry</sup>는 2<sup>20</sup>개
  - 2<sup>20</sup>개의 PTE를 4KB의 page 크기로 쪼갬
  - 하나의 page에 들어갈 수 있는 PTE의 개수 = 4 KB / 4 bytes = 1 KB = 2<sup>10</sup>
- Split the page table to fit every page table piece into a page

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85939352-a0d3a000-b94f-11ea-8bc6-3cb7a2870bce.png'>
</p>
<br>

#### Hierarchical Page Tables Example 2

> 53-bit address space, 8 KB pages, 8 bytes/PTE

- To address 8 KB, we need 13 bits
- Thus, offset part is 13 bits, and 40 bits are remaining

- Each page can contain 8 KB / 8 bytes = 1K (2<sup>10</sup>) PTEs
- Thus, we need 10 bits per translation
- 10 + 10 + 10 + 10 + 13

- Thus, the system requires 4-level page table

#### Address translation in Intel 64 architecture

- **48-bit** *linear* address → 52-bit physical address (4KB page)
  - 2<sup>9</sup>개의 page table entry
  - 4KB / 2<sup>9</sup> = 2<sup>3</sup> = 8 bytes
  - 4KB page에 PTE 하나하나는 8 bytes
  
  - 4-level page table
  - 9 + 9 + 9 + 9 + 12 = 48 bits

- **Page-table base register (PTBR)** points to the base address of the page table (e.g., **CR3** in x86 architecture)
  - 52-bit physical address를 사용하고 page가 4KB일때 PFN는 `40 bits`가 필요하다.

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85939792-36bcfa00-b953-11ea-9b18-a870f577aaae.png'>
</p>
<br>

#### Address translation in Alpha AXP architecture

- Three-level page tables
- Alpha 21064
  - Page size: 8KB
  - Virtual address: 43 bits
  - Each page table isone page long

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85940189-e2674980-b955-11ea-885d-ab43093e77b3.png'>
</p>
<br>

#### Hierarchical Page Tables의 장단점

-  장점
   -  Compact while supporting sparse-address space (뛰엄뛰엄 되어있는 address space)
      -  Page-table space in proportion to the amount of address space used
   -  Easier to manage physical memory
      -  Each page table usually fits within a page
   -  Easier for hardware to walk though page tables
   -  No external fragmentation
      - 일정한 크기의 page로 쪼개어 사용하기때문에 external fragmentation 발생하지 않음

-  단점
   -  More complex than a simple linear page-table lookup

### Hashed Page Table

- Virtual Page Number is hashed into a page table
    - The page table contains a chain of VPN that falls into the same hash bucket

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85940820-c6fe3d80-b959-11ea-922e-b436656a33bf.png'>
</p>
<br>

### Inverted Page Tables

- **Reverse** mapping from **PFN → <VPN, PID>**
  - One entry for each page frame in physical memory
  - Entry consists of the virtual page number with information about the process that owns that page
  - Need to search through the table to find match
  - Use hashing to limit the search to one, or at most a few, page-table entries

- Pros & Cons
  - Decrease memory needed to store page tables: No need to have per-process page tables (프로세스마다 page table 관리할 필요 X)
  - Increase time needed to search the table on a TLB miss

## TLB <sup>Translation Look-aside Buffer</sup>

> **Hierarchical Page Table에서는 하나의 VPN을 translate하기 위해 각 level 당 1번의 memory access가 필요로 하다.**

- Address translation through page tables **takes long** (시간이 너무 많이 걸린다)
  - E.g., On a 3-level page table, an address lookup requires 3 memory accesses, one access for each level

- 최근에 접근한 Memory Address translation은 가지고 있으면 어떨까?
  - Address translation을 할 때, 결과값이 적혀있는 cheating sheet을 가지고 있으면 translation을 쉽게 할 수 있지 않을까?
  - 그래서 나온 것이 **TLB**

- Translation Look-aside Buffer
  - **Hardware support to accelerate the address translation**
  - MMU의 일부로 구현이 되어있는 경우가 많다.
  - A cache with 32—1024 entries
    - TLB entries: <page #, frame #>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85941084-9a4b2580-b95b-11ea-9f74-90281c40a665.png'>
</p>
<br>

- On a TLB **miss**, MMU walks through the page table, and store the translation result in TLB

- On a TLB **hit**, MMU can convert VA to PA without walking through the page table

- **On a context switch, TLB should be flushed** → overhead
  - Each process has its own VA to PA mapping
  - Thus, cannot share TLB entries between processes

- Some TLBs with **address-space Identifiers (ASIDs) <sup>pid</sup>** allows to coexist TLB entries from different processes