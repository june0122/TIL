# Demand Paging and VM Features

## Swapping

> **Process 전체**의 address space 내용을 저장장치에 뺐다가 필요하면 다시 가지고 오는 technique

- Swap out a process temporarily to a backing store
- Bring the process back into memory later for continued execution

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85945221-48b09400-b977-11ea-9d1d-7324dfc54d29.png'>
</p>
<br>

- Advantage
  - ∑ process memory can be larger than the physical memory
    - 설치되어있는 물리 메모리보다 더 많은 양의 물리 메모리를 현실적으로 사용할 수 있다.

- Disadvantage
  - Usually take long, determined by the process size and data transfer rate of the backing store
    - E.g., Swap out a process of 2GB, SATA3 supports up to 6 Gbps → 2 GB / (6Gb / 8) = 2.7 seconds
    - Probably be doubled for swap-in

- Solution
  - Allow processes to inform OS of memory use via system calls
  - Allow to swap out only a part of processes in the memory
    - 전체를 swap out하지 않고 page 단위로 page out하는 방법은 없을까? → **Demand Paging**

## Demand Paging

- A paging system with **page-level swapping**
  - The key feature to implement virtual memory
   
- Bring a page into memory **only when** it is needed
  - C.f., swapping: move entire process
  - Less I/O needed
  - Less memory needed
  - Faster response
  - More users

> Process는 Memory에 있는 정보만 바로 읽을 수 있다. Disk에 있는 정보는 바로 읽을 수 없다.

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945550-9deda500-b979-11ea-9be4-13807f746a17.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945699-895ddc80-b97a-11ea-848d-02cdf159c69b.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945629-10f71b80-b97a-11ea-86ec-07cf093eb6f1.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945731-b9a57b00-b97a-11ea-89f9-656b49255d8d.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945791-038e6100-b97b-11ea-9bc1-cf198e1272c1.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85945925-daba9b80-b97b-11ea-9493-fc8c4feea350.png'>
</p>
<br>

> 운영체제가 최근에 자주 참조되고 있는 page만 main memory에 caching 하듯이 동작하고, 잘 사용하지 않는 것은 swap file로 빠지게 된다.

- OS uses main memory as a cache of all the data allocated and accessed by processes in the system

- Initially, pages are allocated from physical memory frames

- When physical memory fills up, allocating a page requires some other page to be evicted <sup>쫒아내다</sup> from its physical memory frame

- Evicted pages go to a swap file on disks
  - OS moves page contents between memory and disks
  - **Transparent** to the application

### Page fault

- When a page was evicted, OS sets its corresponding PTE;

    <p align = 'center'>
    <img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85946058-c4f9a600-b97c-11ea-80aa-3ca553456383.png'>
    </p>

  - Clear the valid bit (mark it as invalid)
  - Update PFN to the location of the page data on the swap file (**swap entry**)

- When the evicted page is accessed, MMU sees the cleared valid bit of the PTE, which indicates the **PTE is invalid**

- Thus, MMU throws an exception called **page fault**
  - interrupt와 비슷하며 interrupt는 interrupt service routine으로 jump한다.

- **OS** runs **page fault handler** in response
  - Check the PTE, and see that the page was evicted to the swap file
  - If invalid and PFN is NULL → invalid PTE → wrong page access → signal to the causing process `SIGSEGV`

- Where does the page that is read in go?
  - Must evict something (a.k.a., **victim frame, victim page**) else
  - **Page replacement algorithm** determines the victim page
  - OS typically keeps a pool of free pages around so that allocations do not inevitably cause evictions
    - **Pagers** perform this operation
      - E.g., `kswapd` in Linux

- What will be happened to the swap entry after the read in?
  - Discard and recycle immediately, or
  - Keep it around and use it as the backup of the page

- What will be happened to the swap entries when the process exit?
  - Should reclaim <sup>회수</sup> all swap entries from exited processes

### Pure demand paging

> 순수하게 현재 시점에 필요한 페이지만 적재한다.

- A case for pure demand paging
  - When a process first starts up, it has a brand-new page table, with all PTE valid bits “false”
    - No page is yet mapped to physical memory
  - When the process starts executing,
    - Instructions immediately fault on both code and data pages
    - Faults stop when all necessary code/data pages are in memory
    - Only the code/data that is needed (demanded!!!) by process needs to be loaded
    - What is needed changes over time, of course!!
  - **Page faults** and **OS page fault handler** implement the demanding page

### the principle of locality

- Demand paging leverages ***the principle of locality***
  - **Temporal locality**: locations referenced recently tend to be referenced again soon
    - 어떠한 event가 발생했는데 시간 순서상 그 event가 일어날 가능성이 높은 것
  - **Spatial locality**: locations near recently referenced locations are likely to be referenced soon
    - 어떠한 event가 발생했는데 그 주변(공간)에서 event가 일어날 가능성이 높은 것

- Locality means paging can be infrequent
  - Once a page is read in, it will be used many times
  - On average, you will use the pages that are paged in
  - But this depends on many things;
    - Application’s memory reference pattern and memory footprint
    - Page replacement policy
    - Amount of physical memory

- **Hardware support** needed for demand paging:
  - Page table with valid/invalid bit
  - 저장장치 : Secondary memory (swap device with swap space)
  - Instruction restart

## Virtual Memory

- Everything that we have been discussed…

- Use a large and contiguous **virtual address** for memory references

- CPU (and MMU) **performs address translation** at run time
  - From a virtual address to the corresponding physical address
  - Page tables and TLB are involved here
  
- Physical memory is dynamically allocated or released by pages

- **CPUs ask operating systems** to help if necessary

### Virtual Memory <sup>VM</sup> Advantages

- Separate user logical memory from physical memory
  - Abstract main memory into an extremely large, uniform array of storage
  - Free programmers from the concerns of memory-storage limitations

- Allow processes to run with only a part of its entire memory in main memory
  - Logical address space can be much larger than physical address space

- Allows address spaces to be shared by several processes

- Allows for more efficient process creation

- More programs run concurrently

- Less I/O needed to load or swap processes

## Features and issues in Virtual Memory

- Kernel address space
- Shared memory
- Copy-on-write
- Allocation of frames
- Thrashing
- Memory-mapped file
- Buddy System Allocator
- Allocating kernel memory
- Prepaging
- Page pinning

### Shared Memory

- Want to share data between processes
  - To exchange data between processes
  - To reduce memory footprint

- OS can allow processes to share the same page by manipulating their page tables
  - Load data on a page frame
  - Set the PTEs of processes to point to the same page frame
    - Can be used by more than two processes!

> 2개 이상의 프로세스가 서로 같은 메모리를 공유하기가 쉬워진다. Page table의 mapping을 같은 page로 하면 되기 때문.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85947873-4440a700-b988-11ea-984e-14f8785d6804.png'>
</p>
<br>

### Where is the operating system in the memory?

> 운영체제는 모든 프로세스의 일정 주소 공간에 공통적으로 mapping이 되어 존재한다.<br>**운영체제는 모든 프로세스에 공통적으로 mapping**이 되어 있는 kernel address space에서 동작한다.

- In general, OSes split the virtual address space into two parts
  - **User address space** and **kernel address space**

- **User processes share the same kernel address space**
  - Kernel address space is laid over the processes’ address spaces

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85947944-c03aef00-b988-11ea-884b-f7f0cf0fa255.png'>
</p>
<br>

## Copy-on-Write

> 두 개의 프로세스가 같은 값을 가지고 시작을 하되 나중에는 각자 독립된 값을 가져야하는 상황 <sup>fork() etc..</sup>이 발생할 때, page allocation을 최대한 미뤄놓고 **같은 내용을 최대한 공통적으로 사용하게 만들어 메모리를 효과적으로 사용할 수 있다.**

- Defer memory copies as long as possible, hoping to avoid them altogether

- Heavily used in modern operating systems

- Implementation
  - Instead of copying pages, create shared mappings to the same page frames in physical memory
  - Shared pages are protected as read-only
  - When data is written to these pages, OS allocates new space in physical memory and direct the write to it

### Copy-on-write during `fork()`

- Instead of copying all pages, create **shared mappings of parent pages** in child address space
- Shared pages are protected as read-only in both parent and child
- Read happen as usual
- Writes generate a protection fault
- OS copies the page, changes page mapping, and restart write instruction

> parent process를 `fork()` 하여 child를 생성

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85948772-933d0b00-b98d-11ea-84f5-db28739ce8c0.png'>
</p>
<br>

> 모든 page들을 복사하는 대신, parent process에 있는 shared mapping들만 공유하도록 child address space를 생성<br>Page의 `write` permission은 모두 꺼놓는다.

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85948773-93d5a180-b98d-11ea-8e3e-b647aa42aff7.png'>
</p>
<br>

> Child가 write를 하려하면 새로운 page를 copy해놓고 copy해놓은 page에 mapping을 한 뒤 `write`가 가능하도록 만든다. (Copy-on-write)

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85948774-946e3800-b98d-11ea-89e8-61cd523a646a.png'>
</p>
<br>

<br>
<p align = 'center'>
<img width = '500' src = 'https://user-images.githubusercontent.com/39554623/85948775-9506ce80-b98d-11ea-978f-beb5c774d5a9.png'>
</p>
<br>

### Map count

- page를 가리키고 있는 process가 하나밖에 없을 때는 해당 page를 그대로 쓰면 되는 것을 환기하기 위해 page마다 Map count를 관리한다.
  - MMU의 page fault handler에서 fault가 발생했을때, 위를 고려해서 새로운 값을 가져갈지 말지를 결정해야한다.

### Copy-on-write during allocating data and heap pages

> data와 heap page가 Copy-on-write 기반으로 동작

- Have a special page frame called **“zero page”**
  - Contain only zeroes

- When extending the heap, the extended address space is mapped to the zero page without write permission
  - E.g., `malloc()`
    - malloc(1GB)를 통해 1GB를 allocation하더라도 **실제로 사용하고 있는 page의 크기는 zero page의 크기인 4KB 뿐이다.**
    - 여기에 write를 할때마다 동적으로 page가 하나씩 만들어져서 copy-on-write된 후 page에 붙게 된다.

- When process reads data: zeroes

- When process writes to the heap: populate the page with copy-on-write

<br>

## Paging Virtual Memory

> [**Process address space의 segement에 따라 Demand paging에서 처리해야하는 방식과 적용하는 방식이 다르다.**]() ★

- Code
  - **Read-only, sharable**
  - Backed by executable file (**file-backed pages** <sup>Disk의 파일에서 읽어져서 오는 내용</sup>)
    - File-backed pages들은 Page replacement policy에 의해서 paging out을 시킬때 **굳이 swap에다 작성하지 않아도 된다.**
    - Discard한 뒤 file에서 다시 읽어오면 된다.
  - Can be reclaimed easily (just discard)

- Stack, heap (**anonymous pages** <sup>어디로부터 page가 왔는지 모름</sup>)
  - Start from zero page, CoW <sup>Copy-on-write</sup>
  - Should be backed to swap file

- Data
  - Started from file data, CoW upon update
  - Should be backed to **swap file after CoW**

<br>
<p align = 'center'>
<img width = '250' src = 'https://user-images.githubusercontent.com/39554623/85950808-ffbe0700-b999-11ea-8df4-03106e14af99.png'>
</p>
<br>

## Frame Allocation

- Ways of distributing limited page frames to processes
  - Equal allocation
    - Every process gets the same amount
  
  - Proportional allocation
    - Adapt to the process sizes dynamically
  
  - Priority allocation
    - Proportional allocation + some prioritized replacement policy

## Global vs. Local Page Replacement

- Global replacement
  - Process selects a victim frame **from all frames**
  - One process can take over a frame from another processes
  - Execution time can vary significantly, better throughput
    - 성능이 일정하게 나오지 않을 수 있는 단점 존재

- Local replacement
  - Replace the **process’s page frame**
  - Consistent <sup>일관된</sup> performance, but underutilized memory
    - 다른 프로세스에서 사용하지 않는 frame이 있더라도 자신의 프로세스에서 victim을 선택해야하기에 메모리를 효과적으로 사용하지 못하는 경우도 발생

## Thrashing

>  Working set보다 Physical memory의 크기가 작아지기 시작하면 성능이 급격하게 저하한다.

<br>
<p align = 'center'>
<img width = '400' src = 'https://user-images.githubusercontent.com/39554623/85951243-d9e63180-b99c-11ea-856c-22840835c56e.png'>
</p>
<br>

- What happens when physical memory is not enough to hold all the **'working sets <sup>set of hot pages</sup>'** of processes
  
  - Working set: a set of pages that a process is using actively
  
  - Most of time is spent by an OS paging data back and forth from disks
  
  - Possible solutions:
    - Kill processes
    - Buy more memory

## Buddy System Allocator

> 가능한 적당하게 메모리 요청을 만족하도록 메모리를 여러 부분으로 나누는 메모리 할당 알고리즘이다. 이 시스템은 메모리의 크기를 절반씩 분할을 하면서 가장 잘 맞는 크기의 메모리를 찾는다.

- To **control/reduce external fragmentation** of the memory

- Allocate memory from fixed-size segment consisting of physically **contiguous pages**

- Only allocate power-of-2 consecutive pages
  - 1(2<sup>0</sup>), 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024(2<sup>10</sup>), 2048, … pages
  - A request is rounded up <sup>반올림</sup> to next highest power of 2
    - E.g., requesting 13 pages will be served with 16 pages
    - Thus, can be internally fragmented
  - When smaller allocation is needed than what is available, current chunk is split into two **buddies** of next-lower power of 2
    - Continue until appropriately sized chunk available

> Allocating 2 consecutive <sup>연이은</sup> pages

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85951774-46aefb00-b9a0-11ea-91fb-15b61ee07f8c.png'>
</p>
<br>

### Buddy System Allocator Example

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85951816-a4dbde00-b9a0-11ea-9fa9-2ed643760894.png'>
</p>
<br>

### Buddy System Allocator에서 다 쓴 page의 반환

> buddy가 Free 상태일때 둘을 merge한다. Chunk를 최대한 크게 관리해나가면서 External fragmentation을 어느정도 Control 할 수 있다.

- To free a chunk, **check whether its buddy is free or not**
  - If not, just mark this chunk as free
  - If the buddy is also free,
    - Merge with the buddy, and check the merged chunk from one order higher
    - Can be nested on higher orders

<br>
<p align = 'center'>
<img width = '600' src = 'https://user-images.githubusercontent.com/39554623/85951949-495e2000-b9a1-11ea-9026-b74d1fa01a33.png'>
</p>
<br>

## Allocating Kernel Memory

- Slab allocator : **특정 크기의 allocation을 빠르게 처리**할 수 있도록 최적화되어 있다.
  - A sort of cache for object allocation
  - **Slab** is one or more physically contiguous pages
    - Usually provided by underlying buddy system allocator
  - **Cache** consists of one or more slabs
  - One kernel data structure is allocated from its cache
    - A cache instance for task_struct, another cache instance for mm_struct, yet another cache instance for 8K pages, …
  - Cache acquires an empty slab from the system and allocates objects from the slab
  - Returned objects can be recycled quickly

## Prepaging <sup>(aka prefetching)</sup>

> 앞으로 사용될 Page를 예측해서 미리 Main Memory로 로드하는 기법

- Initiate paging in advance
  - To avoid the large number of page faults when a process starts up

- It is important to bring in which pages and how many pages
  - Unused pages waste I/O and memory

- In the Linux kernel, the **fault-around** scheme brings in nearby pages as well as the faulty page
  - Exploit the spatial locality

## Page Pinning

> GPU와 같은 device들은 page table을 읽어서 못보는 경우가 많다. (IOMMU를 사용해서 가능한 장치들도 존재)

- 그렇기 때문에 특정 page들을 demand paging해서 swap out 하지 못하도록 하는 방식을 Page Pinning이라 한다.

<br>
<p align = 'center'>
<img width = '200' src = 'https://user-images.githubusercontent.com/39554623/85952219-c8a02380-b9a2-11ea-947f-629abd3769ab.png'>
</p>
<br>

- Sometimes, pages should be pinned to the memory
  - E.g., I/O buffers (Device가 참조하는 Buffer)
    ```c
    // Texture mapping

    buffer = malloc(1MB);
    load_texture(file, buffer, 1MB);
    project_texture(GPU, buffer);
    ```

  - What happen if the page is evicted in the
  middle of I/O?

## Memory Management on Mobiles

- Using mobiles are very different from using desktops
  - Mcrosoft Research study, 2008 – 2009
    - 33 Android users, 222 Windows Mobile users, 7—21 weeks
  
  - LiveLab study by Rice University, 2011
    - iPhone 3GS, 25 users, 1 year

  - Users briefly and frequently access many apps with preference
  - Launch apps 10—200 times per day among 10—90 installed apps
  - Use 1—2 apps for 10—250 seconds per session
  - Top 5 apps account for ~80% of launches

- Critical to launch apps as quickly as possible
  - Apps are paused in the memory in a compact form
  - Allow to resume quickly on next access

- Classify apps according to their importance
  - Role, status, and the recency of use

- When free memory gets below a threshold,
  - Kindly ask apps to voluntarily release allocated memory
  - Select the largest app belonging to the least important app class
  - Kill the victim app to reclaim its memory
  - Repeat until the free memory gets over the threshold

- 사용자 앱을 띄웠다가 다른 앱으로 변경해도 바로 Kill하지 않고 Memory에 Caching 해놓는다.
  - Caching하는 대신 얼마나 최근에 실행하였는가 순차적으로 줄을 세워둠.
  - Memory를 많이 소모하는 앱을 실행했을 경우 Caching된 앱 중에서 가장 옛날에 사용하고 메모리를 더 차지하는 앱을 Kill한다.

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85952738-5c272380-b9a6-11ea-94a6-7fea28cdc65b.png'>
</p>
<br>