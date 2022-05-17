# Paging Library

> 페이징 라이브러리는 로컬 저장소 또는 네트워크의 대규모 데이터를 데이터 페이지<small>(pages of data)</small> 형태로 표시하고 로드할 수 있도록 해준다.

> 페이징 라이브러리의 컴포넌트는 [권장 앱 아키텍처 가이드](https://developer.android.com/topic/architecture)에 맞게 설계되었고, 다른 Jetpack 컴포넌트와 원활하게 통합되고 최고 수준으로 코틀린을 지원한다.

## 페이징 라이브러리 사용의 이점

- **페이징된 데이터의 메모리 내 캐싱**. 이를 통해 앱이 페이징된 데이터로 작업하는 동안 시스템 리소스를 효율적으로 사용할 수 있다.
- 기본으로 제공되는 **요청 중복 제거 기능**<small>(Built-in request deduplication)</small> 덕분에 앱에서 네트워크 대역폭과 시스템 리소스를 효율적으로 사용할 수 있다.
- 유저가 로드된 데이터의 끝까지 스크롤할 때 구성 가능한<small>(Configurable)</small> 리사이클러뷰 어댑터가 **자동으로 데이터를 요청**한다.
- 코틀린 코루틴 및 Flow뿐만 아니라 LiveData와 RxJava를 최고 수준으로 지원한다.
- 새로고침 및 재시도 기능을 포함하여 오류 처리를 기본으로 지원한다.

## 라이브러리 아키텍처

페이징 라이브러리는 권장 Android 앱 아키텍처에 직접 통합된다. 라이브러리의 컴포넌트는 앱의 세 가지 레이어에서 작동한다.

- Repository 레이어
- ViewModel 레이어
- UI 레이어

<figure align = 'center'>
  <img width = '800' src = 'https://user-images.githubusercontent.com/39554623/168600562-298a7cea-3127-461d-bc71-ca99bd598be6.png'>
  <figcaption>
    <b>그림.</b> 페이징 라이브러리가 앱 아키텍처에 어떻게 적용되는지 보여주는 예
  </figcaption>
</figure>

이 섹션에서는 각 레이어에서 작동하는 페이징 라이브러리 컴포넌트와 어떻게 이 컴포넌트들이 함께 작동하여 페이징된 데이터를 로드하고 표시하는지를 설명한다.

### Repository 레이어

- Repository 레이어의 기본 페이징 라이브러리 컴포넌트는 <b><code>[PagingSource](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource)</code></b>이다.
  - 각 `PagingSource` 객체는 데이터 소스와 이 소스에서 데이터를 검색<small>(retrieve)</small>하는 방법을 정의한다.
- 사용할 수 있는 다른 페이징 라이브러리 컴포넌트로 <b><code>[RemoteMediator](https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator)</code></b>가 있다.
  - `RemoteMediator` 객체는 로컬 데이터베이스 캐시가 있는 네트워크 데이터 소스와 같이 계층화된 데이터 소스의 페이징을 처리한다.

### ViewModel 레이어

- <b><code>[Pager](https://developer.android.com/reference/kotlin/androidx/paging/Pager)</code></b> 컴포넌트는 `PagingSource` 객체와 <b><code>[PagingConfig](https://developer.android.com/reference/kotlin/androidx/paging/PagingConfig)</code></b> 구성 객체를 바탕으로 반응형 스트림에 노출되는 `PagingData` 인스턴스를 구성하기 위한 공개 API를 제공한다.
- `ViewModel` 레이어를 UI에 연결하는 구성요소는 <b><code>[PagingData](https://developer.android.com/reference/kotlin/androidx/paging/PagingData)</code></b>이다. `PagingData` 객체는 페이지로 나눈 데이터의 스냅샷을 보유하는 컨테이너이다. `PagingSource` 객체를 쿼리하여 결과를 저장한다.

### UI 레이어

- UI 레이어의 기본 페이징 라이브러리 컴포넌트는 페이지로 나눈 데이터를 처리하는 리사이클러뷰의 어댑터인 <b><code>[PagingDataAdapter](https://developer.android.com/reference/kotlin/androidx/paging/PagingDataAdapter)</code></b>이다.
- 또는 레이어에 포함된 <b><code>[AsyncPagingDataDiffer](https://developer.android.com/reference/kotlin/androidx/paging/AsyncPagingDataDiffer)</code></b> 컴포넌트를 사용하여 커스텀한 어댑터를 빌드할 수 있다.

## References

- Android Developers, Paging library overview : https://developer.android.com/topic/libraries/architecture/paging/v3-overview


# 페이징된 데이터 로드 및 표시

> 페이징 라이브러리를 사용하여 네트워크 데이터 소스에서 페이징된 데이터의 스트림을 설정하고 리사이클러뷰에 표시하는 방법을 알아본다.

## 데이터 소스 정의

- 첫 번째 단계로 데이터 소스를 식별하기 위해 `PagingSource` 구현을 정의해야 한다.
- `PagingSource` API 클래스에는 `load()` 메서드가 포함되어 있으며, 이 메서드는 상응하는 데이터 소스에서 페이징된 데이터를 검색하는 방법을 나타내기 위해 재정의<small>(override)</small>해야 한다.
- `PagingSource` 클래스를 직접 사용하면 비동기 로딩<small>(async loading)</small>에 코틀린 코루틴을 사용한다. 페이징 라이브러리는 다른 비동기 프레임워크를 지원하는 클래스도 제공한다.
  - RxJava를 사용하려면 `RxPagingSource`를 구현해야 한다.

### Key와 Value 타입 선택

`PagingSource<Key, Value>`에는 `Key`와 `Value` 두 개의 타입 파라미터가 있다. Key는 데이터를 로드하는 데 사용되는 식별자를 정의하며, Value는 데이터 자체의 타입이다. 예를 들어 `Int` 페이지 번호를 Retrofit에 전달하여 네트워크에서 `User` 객체의 페이지를 로드하는 경우 `Key` 유형으로 `Int`를, `Value` 유형으로 `User`를 선택한다..

### PagingSource 정의

다음 예에서는 페이지 번호를 기준으로 항목 페이지<small>(pages of items)</small>를 로드하는 `PagingSource`를 구현한다. `Key` 타입은 `Int`이고 `Value` 타입은 `User`이다.

#### Kotlin Coroutunes

```kotlin
class ExamplePagingSource(
    val backend: ExampleBackendService,
    val query: String
) : PagingSource<Int, User>() {
  override suspend fun load(
    params: LoadParams<Int>
  ): LoadResult<Int, User> {
    try {
      // Start refresh at page 1 if undefined.
      val nextPageNumber = params.key ?: 1
      val response = backend.searchUsers(query, nextPageNumber)
      return LoadResult.Page(
        data = response.users,
        prevKey = null, // Only paging forward.
        nextKey = response.nextPageNumber
      )
    } catch (e: Exception) {
      // Handle errors in this block and return LoadResult.Error if it is an
      // expected error (such as a network failure).
    }
  }

  override fun getRefreshKey(state: PagingState<Int, User>): Int? {
    // Try to find the page key of the closest page to anchorPosition, from
    // either the prevKey or the nextKey, but you need to handle nullability
    // here:
    //  * prevKey == null -> anchorPage is the first page.
    //  * nextKey == null -> anchorPage is the last page.
    //  * both prevKey and nextKey null -> anchorPage is the initial page, so
    //    just return null.
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }
}
```

일반적인 `PagingSource` 구현은 생성자에서 제공된 파라미터를 `load()` 메서드에 전달하여 쿼리에 적절한 데이터를 로드한다.

위의 예시의 파라미터는

- `backend`: 데이터를 제공하는 백엔드 서비스의 인스턴스
- `query`: `backend`로 표시된 서비스에 전송할 검색어

<b><code>[LoadParams](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource.LoadParams)</code></b> 객체에는 실행할 로드 작업에 관한 정보가 포함된다. 여기에는 로드할 키와 로드할 항목 수가 포함된다.

<b><code>[LoadResult](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource.LoadResult)</code></b> 객체에는 로드 작업의 결과가 포함된다. `LoadResult`는 `load()` 호출이 성공했는지에 따라 두 가지 형식 중 하나를 취하는 *sealed class*이다.

- 로드에 성공하면 `LoadResult.Page` 객체를 반환
- 로드에 실패하면 `LoadResult.Error` 객체를 반환

아래의 그림은 이 예시의 `load()` 함수가 각 로드의 키를 수신하고 후속 로드용 키를 제공하는 방법을 보여준다.

<figure align = 'center'>
   <img width = '600' src = 'https://user-images.githubusercontent.com/39554623/168615680-3aadc140-3333-442f-96ae-284e89336f7b.png'>
   <figcaption>
      <b>그림.</b> <code>load()</code>의 Key 사용 및 업데이트 방법을 보여 주는 다이어그램
   </figcaption>
</figure>

`PagingSource` 구현은 <b><code>[getRefreshKey()](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource?hl=ko#getrefreshkey)</code></b> 메서드도 반드시 구현해야 하며 이는 `PagingState` 객체를 파라미터로 취하고 데이터가 첫 로드 후 새로고침되거나 무효화되었을 때 키를 반환하여 `load()`로 전달한다. Paging 라이브러리는 다음에 데이터를 새로고침할 때 자동으로 이 메서드를 호출한다.

### 오류 처리

데이터 로드 요청은 특히 네트워크를 통해 로드하는 경우 여러 가지 이유로 실패할 수 있다. 로드하는 중에 `load()` 메서드에서 `LoadResult.Error` 객체를 반환하여 발생한 오류를 보고한다.

예를 들어 `load()` 메서드에 다음을 추가하여 이전 예시의 `ExamplePagingSource` 클래스에서 로드 오류를 포착하여 보고할 수 있다.

#### Kotlin Coroutunes

```kotlin
catch (e: IOException) {
  // IOException for network failures.
  return LoadResult.Error(e)
} catch (e: HttpException) {
  // HttpException for any non-2xx HTTP status codes.
  return LoadResult.Error(e)
}
```

Retrofit 오류 처리에 관한 자세한 내용은 `PagingSource` API 레퍼런스의 샘플을 참고하자.

`PagingSource`는 개발자가 조치를 취할 수 있도록 `LoadResult.Error` 객체를 수집하여 UI에 제공한다. UI에서 로드 상태를 노출하는 방법에 관한 자세한 내용은 [로드 상태 관리 및 표시](https://developer.android.com/topic/libraries/architecture/paging/load-state?hl=ko)를 참고하자.
