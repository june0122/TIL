### Serializable, Parcelable

- **Serializable :** 객체의 내용을 바이트 단위로 변환하여 파일 또는 네트워크를 통해서 스트림(송수신)이 가능하도록 하는 것
    - Serializable은 해당 클래스가 직렬화 대상이라고 알려주기만 할 뿐, 어떠한 메서드도 가지지 않은 단순한 **마커 인터페이스 (Marker Interface)** 이므로 사용자는 매우 쉽게 사용할 수 있습니다.
    - 사용 방법이 쉽다는 것은 곧 시스템적인 비용이 비싸다는 것을 의미합니다.
    - Serializable은 내부에서 Reflection을 사용하여 직렬화를 처리합니다. Reflection은 프로세스 동작 중에 사용되며 처리 과정 중에 많은 추가 객체를 생성합니다. 이 많은 쓰레기들은 가비지 컬렉터의 타겟이 되고 가비지 컬렉터의 과도한 동작으로 인하여 성능 저하 및 배터리 소모가 발생합니다.
        - Reflection: Class 이름만으로도 클래스의 정보(필드, 메소드)를 찾거나 객체를 생성할 수 있는 기능
    - Serializable도 `writeObject()`, `readObject()` 메소드를 구현하면 Parcelable보다 혹은 그만큼 빠른 성능을 낼 수 있다고 한다.
- **Parcelable**
    - Parcelable은 직렬화를 위한 또다른 인터페이스 입니다. Serializable 과는 달리 표준 Java가 아닌 Android SDK의 인터페이스이고 Reflection을 사용하지 않는다.
    - Parcelable은 구현해야하는 필수 메서드를 포함하기 때문에 클래스에 보일러 플레이트 코드가 추가됩니다.
    - 또 코틀린에서는 `@Parcelize` 어노테이션을 통해서 쉽게 Parcelable을 구현할 수 있다.