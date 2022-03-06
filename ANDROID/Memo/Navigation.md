# Android Navigation 관련 용어 정리

## 목차
- [NavHost](#navhost)
- [NavHostFragment](#navhostfragment)
- [NavController](#navcontroller)
- [NavGraph](#navgraph)
- [NavDestination](#navdestination)
- [Navigator](#navigator)

## [NavHost](https://developer.android.com/reference/androidx/navigation/NavHost)

호스트는 single context 또는 `NavController`를 통해 탐색하기 위한 컨테이너이다.

NavHost를 위한 추가 API들을 제공하는 `NavHostController`를 인스턴스화하여 nav controller를 구성하는 것이 좋다. NavHostController는 `NavHostController`로 직접 노출되지 않고 외부에서 `NavController`로만 액세스할 수 있어야 한다.

내비게이션 호스트는 반드시
- 컨트롤러의 상태의 `저장` 및 `복원`을 처리해야 한다.
- 호스트의 루트 뷰에서 `Navigation.setViewNavController`를 호출해야 한다.
- `NavController.popBackStack`을 수동으로 호출하거나 NavController를 구성할 때 `NavHostController.setOnBackPressedDispatcher`를 호출하여 시스템 백 버튼 이벤트를 NavController로 라우팅해야 한다.

선택적으로, 내비게이션 호스트는 다음 호출을 고려해야 한다.
- `NavHostController.setLifecycleOwner`를 호출하여 NavController를 특정 수명 주기와 연결한다.
- `NavHostController.setViewModelStore`를 호출하여 `NavController.getViewModelStoreOwner` 및 ViewModel을 관찰하는 내비게이션 그래프 사용을 활성화한다. (Call `NavHostController.setViewModelStore` to enable usage of `NavController.getViewModelStoreOwner` and navigation graph scoped ViewModels.)

## [NavHostFragment](https://developer.android.com/reference/androidx/navigation/fragment/NavHostFragment)

NavHostFragment는 자신을 포함해서 레이아웃 내에서 내비게이션이 발생할 수 있는 영역을 제공한다.

NavHostFragment는 주변에 앱의 chorme<small>(XML의 파생 언어인 [XUL](https://en.wikipedia.org/wiki/XUL)을 chrome이라 부르는 것 같다)</small>을 정의하는 레이아웃 리소스 내에서 콘텐츠 영역으로 사용하기 위한 것이다.

```xml
<androidx.drawerlayout.widget.DrawerLayout
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:layout_width="match_parent"
android:layout_height="match_parent">
<androidx.fragment.app.FragmentContainerView
android:layout_width="match_parent"
android:layout_height="match_parent"
android:id="@+id/my_nav_host_fragment"
android:name="androidx.navigation.fragment.NavHostFragment"
app:navGraph="@navigation/nav_sample"
app:defaultNavHost="true" />
<com.google.android.material.navigation.NavigationView
android:layout_width="wrap_content"
android:layout_height="match_parent"
android:layout_gravity="start"/>;
</androidx.drawerlayout.widget.DrawerLayout>
```

각 NavHostFragment는 내비게이션 호스트 내에서 유효한 내비게이션을 정의하는 NavController가 있다. NavController는 NavGraph뿐만 아니라 NavHostFragment 자체와 함께 저장 및 복원되는 현재 위치 및 백 스택과 같은 내비게이션 상태를 포함하고 있다.

NavHostFragment는 `Navigation.findNavController`와 같은 `Navigation` 헬퍼 클래스의 메서드를 통해 하위 항목이 컨트롤러 인스턴스를 얻을 수 있도록 뷰 서브 트리의 루트에 내비게이션 컨트롤러를 등록한다. 탐색 대상 프래그먼트 내의 `android.view.View.OnClickListener`와 같은 뷰 이벤트 리스너 구현은 이러한 헬퍼들을 사용하여 내비게이션 호스트에 대한 긴밀한 결합을 만들지 않고 사용자 상호 작용을 기반으로 탐색할 수 있다.

```kotlin
class HomeFragment : Fragment() {
    ...
    override fun onViewCreated(…) {
        ...
        button.setOnClickListener {
            findNavController().navigate(R.id.flow_step_one_dest, null)
        }
    }
    ...
}
```

## [NavController](https://developer.android.com/reference/androidx/navigation/NavController)

NavController는 NavHost 내에서 앱 탐색을 관리한다.

앱은 일반적으로 호스트에서 직접 컨트롤러를 얻거나, 컨트롤러를 직접 만드는 대신 `Navigation` 클래스의 유틸리티 메서드 중 하나를 사용하여 컨트롤러를 얻을 수 있다.

내비게이션의 흐름 및 대상은 컨트롤러가 소유한 [내비게이션 그래프](https://developer.android.com/reference/androidx/navigation/NavGraph)에 의해 결정된다. 이러한 그래프는 일반적으로 안드로이드 리소스에서 `inflated` 되지만, 뷰와 마찬가지로 프로그래밍 방식으로 구성하거나 결합할 수도 있다. (또는 원격 서버에서 얻은 live data에 의해 애플리케이션의 탐색 구조가 결정되는 경우처럼 동적 탐색 구조로 구성하거나 결합할 수도 있다.)

## [NavGraph](https://developer.android.com/reference/androidx/navigation/NavGraph)

NavGraph는 ID로 가져올 수 있는<small>(fetchable)</small> `NavDestination` 노드의 모음이다.

NavGraph는 '가상' 대상 역할을 한다: NavGraph 자체는 백 스택에 나타나지 않지만 NavGraph로 탐색하면 시작 대상<small>(starting destination)</small>이 백 스택에 추가된다.

새 NavGraph를 구성하면 이 NavGraph는 [대상을 추가](https://developer.android.com/reference/androidx/navigation/NavGraph#addDestination(androidx.navigation.NavDestination))<small>(addDestination)</small>하고 [시작 대상을 설정](https://developer.android.com/reference/androidx/navigation/NavGraph#setStartDestination(kotlin.Int))<small>(setStartDestination)</small>할 때까지 유효하지 않다.

## [NavDestination](https://developer.android.com/reference/androidx/navigation/NavDestination)

NavDestination은 전체 탐색 그래프 내에서 하나의 노드를 나타낸다.

각 대상은 이 특정 대상으로 이동하는 방법을 알고 있는 [Navigator](https://developer.android.com/reference/androidx/navigation/Navigator)와 연결된다.

대상은 지원하는 `action`의 모음을 선언한다. 이러한 action은 대상에 대한 탐색 API를 형성한다. 유사한 역할을 수행하는 다른 대상에 선언된 동일한 action을 통해 애플리케이션 코드가 의미론적 의도<small>(semantic intent)</small>에 따라 탐색할 수 있도록 해준다.

각 대상에는 해당 대상으로 이동할 때<small>(navigating)</small> 적용될 `argument`의 집합을 가지고 있다. 탐색 시 해당 인수의 기본값을 재정의할 수 있다.

NavDestination은 `Navigator.createDestination`을 통해 생성되어야 한다.

## [Navigator](https://developer.android.com/reference/androidx/navigation/Navigator)

Navigator는 앱 내에서 탐색하기 위한 메커니즘을 정의한다.

각 Navigator는 특정 유형의 탐색에 대한 정책을 설정한다. 예를 들어 [`ActivityNavigator`](https://developer.android.com/reference/androidx/navigation/ActivityNavigator)는 Context.startActivity를 사용하여 액티비티가 지원하는 대상으로 시작하는 방법을 알고 있다.

Navigator는 해당 Navigator에 속한 두 대상 사이를 탐색할 때 자신의 백 스택을 관리할 수 있어야 한다. `NavController`는 모든 Navigator에서 현재 탐색 스택을 나타내는 Navigator의 백 스택을 관리한다.

각 Navigator는 클래스에 [`Navigator.Name annotation`](https://developer.android.com/reference/androidx/navigation/Navigator.Name)을 추가해야한다. 연결된 대상 하위 클래스에서 사용하는 모든 커스텀 속성<small>(attributes)</small>은 Navigator와 일치하는 이름을 가져야 한다. 예를 들어 `ActivityNavigator`는 `<declare-styleable name="ActivityNavigator">`를 사용한다.