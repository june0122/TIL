# 프래그먼트 내부에서 `supportFragmentManager` 접근하는 방법

- Kotlin : `activity.supportFragmentManager` 를 통해 언제든 `supportFragmentManager`에 접근할 수 있다.
- Java : `getActivity().getSupportFragmentManager()` 를 통해 언제든 `getSupportFragmentManager`에 접근할 수 있다.
  
  - Activity -> Fragment로 이어지는 계층 구조이기 때문에, 프래그먼트 직접 호출하는 것이 불가능하지만 액티비티는 가능하다.
  
  - 따라서 `getActivity`를 사용하여 프래그먼트가 있는 현재 Activity를 호출하여 `supportFragmentManager`를 get 할 수 있다.

```kotlin
  activity?.supportFragmentManager
          ?.beginTransaction()
          ?.replace(R.id.fragmentContainer, busRouteFragment)
          ?.addToBackStack(null)?.commit()
```