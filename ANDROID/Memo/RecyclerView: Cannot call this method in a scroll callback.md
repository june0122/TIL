# RecyclerView: Cannot call this method in a scroll callback.

Pagination 구현을 위해 리사이클러뷰에 `OnScrollListener`를 추가하여 마지막 아이템에 도달하면 프로그래스바가 포함된 아이템을 제거 후, `notifyItemRemoved(position)` 메서드를 통해 Adapter에 데이터의 변경을 알리는 코드를 작성하였다.

따로 에러가 발생하지는 않았지만 로그창에 스크롤 콜백에서 해당 메서드를 호출할 수 없다는 아래와 같은 메시지가 표시되었다.

> RecyclerView: Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.

스크롤 콜백은 리사이클러뷰의 데이터를 변경할 수 없는 측정 및 레이아웃 단계 중에 실행될 수 있으며, 리사이클러뷰의 구조 또는 어댑터의 내용을 변경할 수 있는 메서드 호출은 다음 프레임으로 연기해야 한다는 내용이다.

아래의 코드를 보면 스크롤 이벤트가 발생하는 도중에 어댑터의 내용을 변경한 것을 확인할 수 있다.

```kotlin
plantRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
        if (!binding.rvPlantList.canScrollVertically(1) && lastVisibleItemPosition == itemCount) {
            plantListAdapter.deleteProgress() // 문제 발생 지점
            currentPage++
            if (currentPage <= lastPage) getUserList()
        }
    }
})
```

```kotlin
fun deleteProgress() {
    if (items.last().description == STATUS_LOADING) {
        items.removeAt(items.lastIndex)
        notifyItemRemoved(items.lastIndex + 1) // 어댑터의 내용을 변경
    }
}
```

즉, 어댑터의 내용을 변경하는 메서드 호출이 UI 스레드를 차단하지 않도록 다른 스레드에서 UI 스레드에 액세스해야 한다는 말이다. 다음은 [안드로이드 문서](https://developer.android.com/guide/components/processes-and-threads#WorkerThreads)에서 제시하는 메서드 목록이다.

- `Activity.runOnUiThread(Runnable)`
- `View.post(Runnable)`
- `View.postDelayed(Runnable, long)`

이 중 `post` 메서드를 이용한 방식을 사용할 수 있는데 공식 문서에선 [post](https://developer.android.com/reference/android/view/View#post(java.lang.Runnable)) 메서드에 대해 다음과 같이 설명하고 있다.

<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/155093468-1e5bc6bb-b56c-435f-9806-e1b1dc9a1769.png'>
</p>

> Causes the Runnable to be added to the message queue. The runnable will be run on the user interface thread.

"Runnable을 메시지 큐에 추가하여 UI 스레드에서 실행될 수 있게 해준다"라는 설명인데 이것을 더 풀어서 이야기하자면, 다른 스레드에서 핸들러를 통해 메시지<small>(Runnable)</small>를 메시지큐에 추가해서 루퍼를 통해 메인 스레드로 메시지<small>(Runnable)</small>를 넘긴다는 이야기다.

코틀린에선 아래와 같이 간단하게 처리할 수 있다.

```kotlin
recyclerView.post {
    mRecyclerViewAdapter.notifyDataSetChanged()
}
```

## References

- https://developer.android.com/guide/components/processes-and-threads
- https://stackoverflow.com/a/13840315/12364882
- https://stackoverflow.com/q/42944005