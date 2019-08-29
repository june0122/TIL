# Retrofit with Kotlin

## [Retrofit](http://devflow.github.io/retrofit-kr/)

> Retrofit은 Android/Java용으로 개발된 오픈 소스 Request 라이브러리로, 주로 API Server에서 원하는 RESTful API에 맞는 요청을 할 때, 사용이 된다.

- 주된 기능은 RDBMS<sup> 관계형 데이터베이스 관리 시스템(relational database management system, RDBMS)</sup>에서 요청이 오면 처리하는 CRUD<sup> Create, Read, Update, Delete</sup> 기능 요청을 할 수 있게 제공한다.

- RESTful API에서 많이 사용하는 포맷인 JSON/XML을 사용하기 위한 라이브러리를 지원한다.

  - 지원하는 라이브러리는 Gson, Jackson, Wire, Simple XML, Scalars 등이 있다.

### Retrofit의 장점

1) 서버를 향해서 네트워크 통신 연결/해제에 대해서 체계적으로 다뤄줘야하는데, 이러한 관리를 해준다.

2) 서버로 연결이 된 후에는, 요청을 데이터 Format에 맞춰서 요청해 주고, 데이터를 parsing하여 응답을 받을 수 있다.

3) 데이터를 가져와서 각종 parsing 라이브러리를 통해서 원하는 요구에 맞게 사용이 가능하다.

4) 각종 에러처리를 원활하고 쉽게 해준다.

### [Android 통신 라이브러리의 역사](https://pluu.github.io/blog/android/2016/12/25/android-network/)

- Retrofit 이전에는 안드로이드 통신 라이브러리로 DefaultHttpClient, HttpUrlConnection, Volley, OkHttp 등을 사용하였다.

- 통신을 할 Repository의 인터페이스를 구현하는데 있어 Retrofit은 어노테이션을 통해 프로그래머가 일일이 작성해야 했던 **보일러 플레이트를 제거**해주고 **스레드 관련 비동기 처리를 간편하게** 해준다.

  - Call 인스턴스는 동기 혹은 비동기로 요청 실행이 가능하며, 각 인스턴스들은 동기 혹은 비동기 중 한가지 방식만 사용이 가능하다. 하지만 `clone()` 메소드를 통해 새 인스턴스를 생성하면 이전과 다른 방식으로 사용이 가능하다.

    ```kotlin
    call.enqueue(object : Callback<Result> {
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.isSuccessful) {
                        response.body()?.let { result ->
                            val users = result.items
                            postListAdapter.items = users
                        }
                    }
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.e("XXX", "XXX - ${t.localizedMessage}")
                }
            })
    ```

  - Volley 같은 경우는 비동기 처리를 감싸고 있는데(강제하고 있음), 동기 - 비동기 처리에 대한 부분은 라이브러리가 아닌 프로그래머가 선택하는 것이 좋다. (Retrofit이 그러하다.)



<br>

## Proguard

> ### ProGuard 사용 시, 라이브러리를 추가할 때 `proguard-rules.pro` 에 옵션을 추가해줘야 한다.

- 해당 라이브러리 문서를 보면 추가해야 할 옵션이 설명되어 있다,

  - [Retrofit](https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro) : If you are using R8 or ProGuard add the options from [this file](https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro).

    ```
    # Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
    # EnclosingMethod is required to use InnerClasses.
    -keepattributes Signature, InnerClasses, EnclosingMethod

    # Retrofit does reflection on method and parameter annotations.
    -keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

    <... 후략 ...> 
    ```

> ### Proguard의 유의사항

- Proguard 적용을 통해 코드의 난독화가 이루어지면 변수의 이름을 통해 동작하던 JSON, GSON 등의 클래스는 멤버 변수 이름이 변경되면서 데이터를 받지 못하는 상황이 발생할 수 있다.

- 그러므로 `proguard-rules.pro` 에 JSON 파싱에 이용되는 DTO, VO, POJO 클래스들은 난독화하지 않도록 설정해준다.

```
# Model
-keep class kr.ac.ajou.retroftpractice.model.** { *; }
```