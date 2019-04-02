# Chapter 23. 쓰레드(Thread)와 동기화

### *23-1. 쓰레드의 이해와 생성*

프로세스(Process) : 현재 **실행 중**에 있는 모든 프로그램. 컴퓨터에서 연속적으로 실행되고 있는 컴퓨터 프로그램

쓰레드(Thread) : 어떠한 프로그램 내에서, 특히 프로세스 내에서 실행되는 흐름의 단위

'프로세스는 쓰레드를 담는 그릇이다.'

> 프로세스의 메모리 공간, 프로세스를 구성하는 메모리 공간의 모습

![Structure of Process](https://t1.daumcdn.net/cfile/tistory/2453685056EEACBE22)

[프로세스의 개념](http://bowbowbow.tistory.com/16) 

<br><br>
![Structure of Thread](https://2.bp.blogspot.com/-3AB4sE53Dfw/VMVNdWa_V0I/AAAAAAAAACo/UAGFO7f6_UA/s1600/euva3a00.p54z.gif)

> 위 그림은 프로세스와 쓰레드의 메모리 구조의 차이점을 보여준다. 왼쪽의 프로세스는 이미 봤기 때문에 설명하지 않고, 오른쪽의 thread에 주목하자. 앞서 말한 것처럼 쓰레드는 프로세스 안에 존재하는 실행흐름이다. 메모리 구조 역시 그러하다. 하지만 특이한 점은 쓰레드는 프로세스의 heap, static, code 영역 등을 공유한다는 사실이다. 각각의 프로세스가 독립적인 stack, heap, code, data 영역을 가진 반면에, 한 프로세스에 속한 쓰레드는 stack 영역을 제외한 메모리 영역은 공유한다. 

쓰레드가 별도의 실행 흐름을 갖기 때문에 스택(stack)을 별도로 관리할 수 밖에 없다. (쓰레드가 생성이 되면 스택이 별도로 생성된다.)

메소드(data) 영역과 힙(heap)은 모든 쓰레드가 접근·활용·공유가 가능하다. 

<br><br>
***■ 쓰레드의 생성***

> Thread 클래스를 상속 → Thread용 클래스를 디자인한다 → 쓰레드의 main 메소드가 되는 run 메소드를 정의한다.

Thread 클래스 : 쓰레드를 생성하기 위해서 쓰레드가 할 일을 정의해놓은 클래스




```java
class ShowTread extends Thread
{
    String threadName;
    public ShowThread(String name)
    {
        threadName=name;
    }
    
    // 쓰레드의 main 메소드가 run 이다.
    public void run()
    {
       for(int i=0; i<100; i++)
       {
           System.out.println("안녕하세요. "+threadName+"입니다.");
           try
           {
              sleep(100);
           }
           catch(InterruptedException e)
           {
              e.printStackTrace();
           }
       }
    }
}
```

```java
public static void main(String[] args)
{
    ShowThread st1=new ShowThread("멋진 쓰레드");
    ShowThread st2=new ShowThread("예쁜 쓰레드");
    st1.start();
    st2.start();
}

// start 메소드가 호출되면 쓰레드가 생성되고, 생성된 쓰레드는 run 메소드를 호출한다.
```

<br><br>
***■ 쓰레드를 생성하는 두 번째 방법***

Runnable 인터페이스를 구현하는 클래스의 인스턴스를 대상으로 Thread 클래스의 객체를 생성한다. 이 방법은 상속할 클래스가 존재할 때 유용하게 사용된다.

`쓰레드 인스턴스의 생성, run 메소드의 정의` : 쓰레드를 생성하는 두 가지 방법 모두 위의 조건을 만족하므로 사실상 동일한 방법임을 알 수 있다.

```java
class Sum
{
    int num;
    public Sum() { num=0; }
    public void addNum(int n) { num+=n; }
    public int getNum() { return num; }
}

class AdderThread extends Sum implements Runnable
{
    int start, end;
    public AdderThread(int s, int e)
    {
        start=s;
        end=e;
    }
    public void run()
    {
        for(int i=start; i<=end; i++)
            addNum(i);
    }
}
```

```java
public static void main(String[] args)
{
    AdderThread at1=new AdderThread(1, 50);
    AdderThread at2=new AdderThread(51, 100);
    Thread tr1=new Thread(at1);
    Thread tr2=new Thread(at2);
    tr1.start();
    tr2.start();
    
    // join 메소드가 호출되면, 해당 쓰레드의 종료를 기다리게 된다.
    
    try
    {
        tr1.join();
        tr2.join();
    }
    catch(InterruptedException e)
    {
        e.printStackTrace();
    }
    System.out.println("1~100부터 까지의 합 : "+(at1.getNum()+at2.getNum()));   
}
```

<br><br>
### *23-2. 쓰레드의 특성*

***■ 쓰레드의 스케줄링과 우선순위 컨트롤***

> 쓰레드 스케줄링의 두 가지 기준

- 우선순위가 높은 쓰레드의 실행을 우선시한다.

- 우선순위가 동일할 때는 CPU의 할당시간을 나눈다.

```java
class MessageSendingThread extends Thread
{
    String message;
    
    public MessageSendingThread(String str)
    {
        message=str;
    }
    public void run()
    {
        for(int i=0; i<1000000; i++)
            System.out.println(message+"("+getPriority()+")");
    }
}
```

```java
public static void main(String[] args)
{
    MessageSendingThread tr1= new MessageSendingThread("First");
    MessageSendingThread tr2= new MessageSendingThread("Second");
    MessageSendingThread tr3= new MessageSendingThread("Third");
    tr1.start();
    tr2.start();
    tr3.start();
}
```

```java
// 실행결과

First(5)
First(5)
Second(5)
......
Third(5)
First(5)
......
Third(5)
```

메소드 getPriority의 반환값을 통해서 쓰레드의 우선순위를 확인할 수 있다.

위의 실행결과에서 보이듯이, 우선순위와 관련해서 별도의 지시를 하지 않으면, 동일한 우선순위의 쓰레드들이 생성된다.

<br><br>
***■ 우선순위가 다른 쓰레드들의 실행***

```java
class MessageSendingThread extends Thread
{
    String message;
    public MessageSendingThread(String str)
    {
        message=str;
        
        // 우선순위 설정
        setPriority(prio);
    }
    public void run()
    {
        for(int i=0; i<1000000; i++)
            System.out.println(message+"("+getPriority()+")");
    }
}
```

```java
public static void main(String[] args)
{
    MessageSendingThread tr1= new MessageSendingThread("First", Thread.MAX_PRIORITY);
    MessageSendingThread tr2= new MessageSendingThread("Second", Thread.NORM_PRIORITY);
    MessageSendingThread tr3= new MessageSendingThread("Third", Thread.MIN_PRIORITY);
    tr1.start();
    tr2.start();
    tr3.start();
}
```

```java
// 실행결과

First(10)
First(10)
......
Second(5)
Second(5)
......
Third(1)
```

**Thread.MAX_PRIORITY**는 상수로 10

**Thread.NORM_PRIORITY**는 상수로 5

**Thread.MIN_PRIORITY**는 상수로 1

실행결과에서 보이듯 쓰레드의 실행시간은 우선순위의 비율대로 나뉘지 않는다.

높은 우선순위의 쓰레드가 종료되어야 낮은 우선순위의 쓰레드가 실행된다.

<br><br>
***■ 쓰레드의 라이프 사이클***

![Life Cycle of Thread](https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory&fname=http%3A%2F%2Fcfile23.uf.tistory.com%2Fimage%2F123BBA494F699E82126112)

<br><br>

|상태|설명|
|---|---|
|New|- 객체 생성<br>- 스레드가 만들어진 상태<br>- 아직 start() 메소드가 호출되지 않은 상태|
|Runnable|- 실행 대기<br>- 실행 상태로 언제든지 갈 수 있는 상태<br> - 스레드 객체가 생선된 후에 start() 메서드를 호출하면 Runnable 상태로 이동|
|Running|- 실행 상태<br>- Runnable 상태에서 스레드 스케줄러에 의해 Running 상태로 이동<br>- 스케줄러는 Running 상태의 스레드 중 하나를 선택해서 실행|
|Blocked|- 일시 정지<br>- 사용하고자 하는 객체의 lock이 풀릴 때까지 기다리는 상태<br>- 스레드가 다른 특정한 이유로 Running 상태에서 Blocked 상태로 이동|
|WAITING|- 일시 정지<br>- 다른 스레드가 통지할 때까지 기다리는 상태|
|TIMED_WAITING|- 일시 정지<br>- 주어진 시간 동안 기다리는 상태|
|TERMINATED(DEAD)|- 실행을 마친 상태(종료)<br>- run() 메소드 완료시 스레드가 종료되면 그 스레드는 다시 시작할 수 없게 된다.|

<br>

Runnable 상태의 쓰레드만이 스케줄러에 의해 스케줄링이 가능하다.

그리고 앞서 보인 sleep, join 메소드의 호출로 인해서 쓰레드는 Blocked 상태가 된다.

한 번 종료된 쓰레드는 다시 Runnable 상태가 될 수 없지만, Blocked 상태의 쓰레드는 조건이 성립되면 다시 Runnable 상태가 된다.

<br>

### *23-3. 동기화(Synchronization)*

***■ 쓰레드의 메모리 접근방식과 그에 따른 문제점***




***■ Thread-safe 한가?***

`Note that this implementation is not synchronized`

API 문서에는 해당 클래스의 인스턴스가 둘 이상의 쓰레드가 동시에 접근을 해도 문제가 발생하지 않는 지를 명시하고 있다.

따라서 쓰레드 기반의 프로그래밍을 한다면, 특정 클래스의 사용에 앞서 쓰레드에 안전한 지를 확인해야 한다.

<br><br>
***■ 쓰레드의 동기화 기법 1 : synchronize 기반 동기화 메소드***


<br><br>
***■ synchronized 기반 동기화 메소드의 정확한 이해***


<br><br>
***■ 쓰레드의 동기화 기법 1 : synchronize 기반 동기화 블록***

> 동기화 메소드 기반

```java
public synchronized int add(int n1, int n2)
{
    opCnt++;    // 동기화가 필요한 문장
    return n1+n2;
}

public synchronized int min(int n1, int n2)
{
    opCnt++;    // 동기화가 필요한 문장
    return n1-n2;
}
```

> 동기화 블록 기반

```java
public int add(int n1, int n2)
{
    synchronized(this)
    {
        opCnt++;    // 동기화 된 문장
    }
    return n1+n2;
}

public int min(int n1, int n2)
{
    synchronized(this)
    {
        opCnt++;    // 동기화 된 문장
    }
    return n1-n2;
}
```

동기화 블록을 이용하면 동기화의 대상이 되는 영역을 세밀하게 제한할 수 있다.

synchronized(this)에서 this는 동기화의 대상을 알리는 용도로 사용이 되었다. 즉, 메소드가 호출된 인스턴스 자신의 열쇠를 대상으로 동기화를 진행하는 문장이다.

<br><br>
***■ 동기화 블록의 예***

```java
public void addOneNum1()
{
    synchronized(key1)
    {
        num1+=1;
    }
}

public void addTwoNum1()
{
    synchronized(key1)
    {
        num1+=2;
    }
}

public void addOneNum2()
{
    synchronized(key2)
    {
    num2+=1;
    }
}

public void addTwoNum2()
{
    synchronized(key2)
    {
        num2+=2;
    }
}

. . . . .

Object key1=new Object();
Object key2=new Object();

```

```java
// 보다 일반적인 형태
// 위의 예제와 달리 둘 다 key를 지정하는 것이 아닌, 두 개의 동기화 인스턴스 중 하나는 this로 지정

class IHaveTwoNum
{
    . . . . .
    public void addOneNum1()
    {
        synchronized(this) { num1+=1; }
    }
    
    public void addTwoNum1()
    {
        synchronized(this) { num1+=2; }
    }
    
    public void addOneNum2()
    {
        synchronized(key) { num2+=1; }
    }
    
    public void addTwoNum2()
    {
        synchronized(key) { num2+=2; }
    }
    
    . . . . .
    
    Object key=new Object();
}
```

<br><br>
***■ 쓰레드 접근순서의 동기화 필요성***

> 수정 전 

```java
class NewWriter extends Thread
{
    NewsPaper paper;
    public NewsWriter(NewsPaper paper)
    {
        this.paper=paper;
    }
    public void run()
    {
        paper.setTodayNews("자바의 열기가 뜨겁습니다.");
    }
}

class NewsReader extends Thread
{
    NewsPaper paper;
    public NewReader(NewsPaper paper)
    {
        this.paper=paper;
    }
    public void run()
    {
        System.out.println("오늘의 뉴스 : "+paper.getTodayNews())
    }
}
```

```java
class NewsPaper
{
    String todayNews;
    public void setTodayNews(String news)
    {
        todayNews=news;
    }
    public String getTodayNews()
    {
        return todayNews;
    }
}
```

```java
public static void main(String[] args)
{
    NewsPaper paper=new NewsPaper();
    NewsReader reader=new NewsReader(paper);
    NewsWriter writer=new NewsWriter(paper);
    
    reader.start();
    writer.start();
    
    try
    {
        reader.join();
        writer.join();
    }
    
    catch(InterruptedException e)
    {
        e.printStackTrace();
    }
}
```

> 수정 후

```java
class NewsPaper
{
    String todayNews;
    boolean isTodayNews=false;
    
    public void setTodayNews(String news)
    {
        todayNews=news;
        isTodayNews=ture;
        synchronizde(this)
        {
            notifyAll();
        }
    }
    
    public String getTodayNews()
    {
        if(isTodayNews==false)
        {
            try
            {
                synchronizde(this)
                {
                    wait();
                }
            }
            
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        return todayNews;
    }
}

/*
wait와 notifyAll 메소드에 의한 동기화가 진행될  때, 이전 예제에서 달라지는 부분은 쓰레드 클래스가 아닌
쓰레드에 의해 접근이 이뤄지는 NewsPaper 클래스라는 사실에 주목.
*/
```


<br><br>
***■ wait, notify, notifyAll에 의한 실행순서 동기화***

- public final void wait() throws InterruptedException

위의 함수를 호출한 쓰레드는 notify 또는 notifyAll 메소드가 호출될 때까지 블로킹 상태에 놓이게 된다.

- public final void notify()

wait 함수의 호출을 통해서 블로킹 상태에 놓여있는 쓰레드 하나를 깨운다.

- public final void notifyAll()

wait 함수의 호출을 통해서 블로킹 상태에 놓여있는 모든 쓰레드를 깨운다.

<br>

```java
synchronized(this)
{
    wait();
}

// 위의 함수들은 왼쪽에서 보이는 바와 같이 한 순간에 하나의 쓰레드만 호출할 수 있도록 동기화 처리를 해야 한다.
```

<br><br>
### *23-4. 새로운 동기화 방식*

***■ synchronized 키워드의 대체***

ReentrantLock 인스턴스를 이용한 동기화 기법

Java Ver 5.0 이후로 제공된 동기화 방식이다. lock 메소드와 unlock 메소드의 호출을 통해서 동기화 블록을 구성한다.

```java
class MyClass
{
    private final ReentrantLock criticObj=new ReentrantLock();
    . . . . .
    void myMethod(int arg)
    {
        criticObj.lock();   // 다른 쓰레드가 진입하지 못하게 문을 잠근다.
        . . . . .
        . . . . .
        criticObj.unlock(); // 다른 쓰레드가 진입이 가능하게 문을 연다
    }
}
```

<br>
↓ 보다 안정적인 구현모델을 위해서는 반드시 unlock 메소드가 호출되도록 모델을 설계한다.



```java
void myMethod(int arg)
{
    criticObj.lock();   // 다른 쓰레드가 진입하지 못하게 문을 잠근다.
    try
    {
        . . . . .
        . . . . .
    }
    finally
    {
        criticObj.unlock(); // 다른 쓰레드의 진입이 가능하게 문을 연다.
    }
}
```

<br><br>
***■ await, signal, signalAll에 의한 실행순서의 동기화***

- await : 낮잠을 취한다(wait 메소드에 대응)

- signal : 낮잠 자는 쓰레드 하나를 깨운다(notify 메소드에 대응)

- signalAll : 낮잠 자는 모든 쓰레드를 깨운다(notifyAll 메소드에 대응)

<br>

ReentrantLock 인스턴스 대상으로 newCondition 메소드 호출 시, Condition 인터페이스를 구현하는 인스턴스의 참조 값 반환

이 인스턴스를 대상으로 위의 메소드를 호출하여, 쓰레드의 실행순서를 동기화 한다.


```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Scanner;

class StringComm
{
	String newString;
	boolean isNewString=false;
	
	private final ReentrantLock entLock=new ReentrantLock();
	private final Condition readCond=entLock.newCondition();
	private final Condition writeCond=entLock.newCondition();
	
	public void setNewString(String news)
	{
		entLock.lock();
		try
		{
			if(isNewString==true)
				writeCond.await();
				
			newString=news;
			isNewString=true;
			readCond.signal();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		finally
		{
			entLock.unlock();
		}
	}
	
	public String getNewString()
	{
		String retStr=null;
		
		entLock.lock();
		try
		{
			if(isNewString==false)
				readCond.await();
			
			retStr=newString;
			isNewString=false;		
			writeCond.signal();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}		
		finally
		{
			entLock.unlock();
		}
			
		return retStr;
	}
}

class StringReader extends Thread
{
	StringComm comm;
	
	public StringReader(StringComm comm)
	{
		this.comm=comm;
	}
	public void run()
	{
		Scanner keyboard=new Scanner(System.in);
		String readStr;
		
		for(int i=0; i<5; i++)
		{
			readStr=keyboard.nextLine();
			comm.setNewString(readStr);
		}
	}
}

class StringWriter extends Thread
{
	StringComm comm;
	
	public StringWriter(StringComm comm)
	{
		this.comm=comm;
	}
	public void run()
	{
		for(int i=0; i<5; i++)
			System.out.println("read string: "+comm.getNewString());
	}
}

class ConditionSyncStringReadWrite
{
	public static void main(String[] args)
	{
		StringComm strComm=new StringComm();
		StringReader sr=new StringReader(strComm);
		StringWriter sw=new StringWriter(strComm);
	
		System.out.println("자바의 새로운 동기화...");
		sr.start();
		sw.start();
	}
}
```







