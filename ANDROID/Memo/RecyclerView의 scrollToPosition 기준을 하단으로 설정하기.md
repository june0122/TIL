# RecyclerView의 scrollToPosition 기준을 하단으로 설정하기

#### 상단 기준

```kotlin
layoutManager.scrollToPosition(32)
```

#### 하단 기준

```kotlin
val smoothScroller = object : LinearSmoothScroller(context) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_END
    }
}

smoothScroller.targetPosition = 32
layoutManager.startSmoothScroll(smoothScroller)
```

- 수직 스크롤 뷰 : `getVerticalSnapPreference()` 오버라이드
- 수평 스크롤 뷰 : `getHorizontalSnapPreference()` 오버라이드


## References

- https://kimch3617.tistory.com/entry/Android-RecyclerView-의-scrollToPosition-하단오른쪽기준으로-변경하기