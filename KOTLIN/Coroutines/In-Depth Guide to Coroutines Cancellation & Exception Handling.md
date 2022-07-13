# In-Depth Guide to Coroutine Cancellation & Exception Handling

코루틴을 막 학습한 사람에게 코루틴은 매우 간단하고 자바스크립트의 async, await와 비슷하게 보이기도 해서 비동기 프로그래밍을 위한 아주 쉽고 훌륭한 도구로 보일 수 있습니다. 실제로 쉽고 훌륭한 도구이긴 하지만요.

하지만 코루틴을 더 깊게 살펴보면 실제로 걸리기 쉬운 함정들이 많이 존재합니다. 예외 처리나 취소를 `try-catch` 블록을 통해 간단히 할 수 있으리라 생각하지만 실제로는 복잡한 매커니즘으로 동작하고 있기에 많은 것들이 잘못될 수도 있습니다.

본문에서는 유튜브의 [In-Depth Guide to Coroutine Cancellation & Exception Handling](https://youtu.be/VWlwkqmTLHc) 영상을 바탕으로 다음의 내용을 다룹니다.
- 코루틴에서 어떻게 예외를 잡고 처리해야 하는지
- 코루틴에서 예외 처리가 일반적으로 어떻게 작동하는지
- 코루틴이 취소되거나 코루틴을 취소할 때 무엇을 고려해야 하는지
