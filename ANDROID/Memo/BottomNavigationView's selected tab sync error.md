# BottomNavigationView의 탭을 눌러도 선택되지 않는 문제 해결하기

## 문제

개인 프로젝트로 개발 중인 앱에서 별다른 버그가 없는가 밥을 먹으며 이리저리 클릭하던 중 특정 상황에 하단 탭의 선택 효과가 동작하지 않는 문제를 발견하여 이를 해결하면서 공부한 내용을 정리했습니다. 문제의 화면과 프로젝트의 내비게이션 그래프의 구성은 아래와 같습니다.

<p align = 'center'>
<img height = '600' src = 'https://user-images.githubusercontent.com/39554623/179753310-bf96c24a-912e-4ef2-8aa7-7e26bb7368cc.gif'>
<img height = '600' src = 'https://user-images.githubusercontent.com/39554623/179754094-89b1313b-d004-47f9-9626-896f57af50c9.png'>
</p>

#### 화면 및 탭 구성

- BottomNavigationView에는 `홈`, `즐겨찾기`, `설정` 총 3개의 탭이 존재
- 설정 화면의 버튼을 통해 `다크 모드 설정` 또는 `테마 설정` 화면으로 이동 가능

#### 버그 내용

1. `설정`의 하위에 속하는 `다크 모드 설정`을 선택하여 화면을 띄운다.
2. 설정 탭 이외의 다른 탭인 `즐겨찾기` 탭을 선택한다.
3. 다시 `설정` 탭을 선택하면 화면은 이전에 띄워놓았던 `다크 모드 설정` 화면이지만 탭은 `설정` 탭이 아니라 `즐겨찾기` 탭이 선택되어 있다.

## 원인

`설정` 탭의 아이템으로는 `설정` 화면이 지정되어 있지만 이의 하위 화면인 `다크 모드 설정` 화면과 연결시킬 어떠한 방법도 정의되어 있지 않기 때문입니다.

아래의 XML 코드를 살펴보면 `설정`에서 `다크 모드 설정` destination으로 탐색하는 action이 정의되어 있긴 하지만 이는 설정 프래그먼트 내부에만 정의되어 있고 다크 모드 설정 프래그먼트에는 해당 정보를 가지고 있지 않기 때문에 `설정`과 `다크 모드 설정` 사이엔 관계가 정의되어 있지 않다고 볼 수 있습니다.

### nav_graph.xml

```xml
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/home_dest">

    <fragment
        android:id="@+id/home_dest" // 홈
        android:name="com.june0122.wakplus.ui.home.HomeFragment"
        android:label="HomeFragment"
        tools:layout="@layout/fragment_home" />

    <fragment
        android:id="@+id/favorite_dest" // 즐겨찾기
        android:name="com.june0122.wakplus.ui.favorite.FavoriteFragment"
        android:label="FavoriteFragment"
        tools:layout="@layout/fragment_favorite" />

    <fragment
        android:id="@+id/settings_dest" // 설정
        android:name="com.june0122.wakplus.ui.settings.SettingsFragment"
        tools:layout="@layout/fragment_settings">
        <action
            android:id="@+id/action_settings_to_dark_mode" // 설정 -> 다크 모드 설정 action
            app:destination="@id/dark_mode_dest" />
        <action
            android:id="@+id/action_settings_to_themes" // 설정 -> 테마 설정 action
            app:destination="@id/themes_dest" />
    </fragment>

    <fragment
        android:id="@+id/dark_mode_dest" // 다크 모드 설정
        android:name="com.june0122.wakplus.ui.settings.dark.DarkModeFragment"
        android:label="@string/fragment_dark_mode"
        tools:layout="@layout/fragment_dark_mode" />

    <fragment
        android:id="@+id/themes_dest" // 테마 설정
        android:name="com.june0122.wakplus.ui.settings.theme.ThemeFragment"
        android:label="@string/fragment_themes"
        tools:layout="@layout/fragment_themes" />

</navigation>
```

#### bottom_nav_menu.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/home_tab" // 홈 탭
        android:icon="@drawable/selector_home"
        android:title="@string/fragment_home" />
    <item
        android:id="@+id/favorite_tab" // 즐겨찾기 탭
        android:icon="@drawable/selector_favorite"
        android:title="@string/fragment_favorite" />
    <item
        android:id="@+id/settings_tab" // 설정 탭
        android:icon="@drawable/selector_settings"
        android:title="@string/fragment_settings" />
</menu>
```

## 해결 방법

### 1. Nested navigation graphs 사용

[Nested navigation graphs](https://developer.android.com/guide/navigation/navigation-nested-graphs), 즉 중첩된 내비게이션 그래프를 사용하여 일련의 destination들을 그룹화해주는 것으로 각 destination 사이에 관계를 정의할 수 있습니다.

이를 통해 `다크 모드 설정` destination이 `설정` 탭 내부에 포함되어 있다는 관계를 정의하였으므로 위의 버그 상황에서처럼 `설정`의 하위인 `다크 모드 설정` 화면을 띄운 채로 다른 탭을 클릭하였다가 `설정` 탭으로 돌아와도 BottomNavigationView의 `설정` 탭이 선택되지 않는 버그는 발생하지 않습니다.

```xml
// Root NavGraph(Parent NavGraph)
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/home_tab">

    // 중첩된 내비게이션 그래프를 통해 설정 탭에 연관된 destination들을 정의한다.
    <navigation
        android:id="@+id/settings_tab" // 설정 탭
        app:startDestination="@id/settings_dest">
        <fragment
            android:id="@+id/settings_dest"
            android:name="com.june0122.wakplus.ui.settings.SettingsFragment"
            tools:layout="@layout/fragment_settings">
            <action
                android:id="@+id/action_settings_to_dark_mode"
                app:destination="@id/dark_mode_dest" />
            <action
                android:id="@+id/action_settings_to_themes"
                app:destination="@id/themes_dest" />
        </fragment>
        <fragment
            android:id="@+id/dark_mode_dest" // 다크 모드 설정
            android:name="com.june0122.wakplus.ui.settings.dark.DarkModeFragment"
            android:label="@string/fragment_dark_mode"
            tools:layout="@layout/fragment_dark_mode" />

        <fragment
            android:id="@+id/themes_dest" // 테마 설정
            android:name="com.june0122.wakplus.ui.settings.theme.ThemeFragment"
            android:label="@string/fragment_themes"
            tools:layout="@layout/fragment_themes" />
    </navigation>

    <navigation
        android:id="@+id/home_tab" // 홈 탭
        app:startDestination="@id/home_dest">
        <fragment
            android:id="@+id/home_dest"
            android:name="com.june0122.wakplus.ui.home.HomeFragment"
            android:label="HomeFragment"
            tools:layout="@layout/fragment_home" />
    </navigation>

    <navigation
        android:id="@+id/favorite_tab" // 즐겨찾기 탭
        app:startDestination="@id/favorite_dest">
        <fragment
            android:id="@+id/favorite_dest"
            android:name="com.june0122.wakplus.ui.favorite.FavoriteFragment"
            android:label="FavoriteFragment"
            tools:layout="@layout/fragment_favorite" />
    </navigation>

</navigation>
```

하지만 해당 방법은 기존의 내비게이션 그래프를 여러 개의 내비게이션 그래프로 분리해야 한다는 불편함이 존재합니다. 여러 Activity가 존재하고 그 안에 또 여러 개의 Fragment들이 존재하는 구조라면 수정이 많이 번거로울 것이고 내비게이션 그래프의 XML 파일의 전체적인 Design을 한눈에 살펴보기가 어려워질 수도 있습니다.

하지만 XML 코드를 번거롭게 수정할 필요없이 BottomNavigationView에 리스너를 추가하는 것만으로 간단히 해결할 수 있는 방법이 있습니다.

### 2. setOnItemSelectedListener 사용

```kotlin
// true를 반환하여 BottomNavigationView에서 선택한 item을 선택한 item으로 표시합니다.
bottomNav.setOnItemSelectedListener { item ->
    // 예상된 동작을 얻으려면 default Navigation 메서드를 수동으로 호출해야 합니다.
    NavigationUI.onNavDestinationSelected(item, navController)
    return@setOnItemSelectedListener true
}
```

위의 코드를 작성하면 항상 하단 탭의 메뉴, 즉 BottomNavigationView의 item을 선택하고 **multiple back stacks**을 유지하는 동안은 해당 item과 연결된 destination으로 탐색합니다.

[Multiple back stacks](https://developer.android.com/guide/navigation/multi-back-stacks)는 Navigation 2.4.0 버전 이상에서만 지원하는 기능입니다. Navigation 컴포넌트는 Navigation Graph에서 destination의 상태를 저장하고 복원하여 multiple back stacks을 지원하는 API를 제공합니다. `NavigationUI` 클래스에는 이를 자동으로 처리하는 메서드가 포함되지만, 기본 API를 직접 사용하여 더 커스텀한 메서드를 구현할 수 있습니다.

참고로 Multiple back stacks을 지원하는 Navigation 2.4.0 이상의 버전을 사용하지 않으면 위에서 문제가 되었던 탭 선택 자체는 잘 동작하지만 해당 탭을 선택했을 때 이전 destination을 유지하지 못합니다. 본문의 마지막에 동작 화면과 함께 설명해놓았으니 참고해주세요.

그럼 위의 코드를 조금 자세히 살펴봅시다.

1. `setOnItemSelectedListener`를 통해 리스너를 등록하여 BottomNavigationView의 Navigation item이 선택될 때 알림을 받습니다. 여기서 Navigation item은 BottomNavigationView의 Menu item을 의미합니다.
2. 알림을 받으면 `onNavDestinationSelected(…)`을 통해 **주어진 item과 연결된 NavDesitination으로 탐색을 시도**합니다. 
3. Menu item을 선택된 item으로 표시하기 위해 마지막에 true를 반환합니다.

#### setOnItemSelectedListener

```java
/**
 * Set a listener that will be notified when a navigation item is selected. This listener will
 * also be notified when the currently selected item is reselected, unless an {@link
 * OnItemReselectedListener} has also been set.
 *
 * @param listener The listener to notify
 * @see #setOnItemReselectedListener(OnItemReselectedListener)
 */
public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
  selectedListener = listener;
}
```

`setOnItemSelectedListener`는 Navigation item이 선택될 때 알림을 받을 리스너를 설정하는 역할을 합니다. 리스너는 `OnItemReselectedListener`가 설정되어 있지 않다면 현재 선택된 항목이 다시 선택될 때도 알림을 받습니다.

```java
/** Listener for handling selection events on navigation items. */
public interface OnItemSelectedListener {
  /**
   * Called when an item in the navigation menu is selected.
   *
   * @param item The selected item
   * @return true to display the item as the selected item and false if the item should not be
   *     selected. Consider setting non-selectable items as disabled preemptively to make them
   *     appear non-interactive.
   */
  boolean onNavigationItemSelected(@NonNull MenuItem item);
}
```

`OnItemSelectedListener`는 Navigation item에 대한 선택 이벤트를 처리하는 리스너로 내비게이션 메뉴의 아이템이 선택될 때 호출됩니다.

- Params
  - item: 선택된 아이템
- Returns
  - 아이템을 선택된 아이템으로 표시하려면 true를 반환
  - 아이템을 선택하지 않아야 한다면 false를 반환

#### NavigationUI

[NavigationUI](https://developer.android.com/reference/kotlin/androidx/navigation/ui/NavigationUI?hl=en)는 Navigation drawer 또는 Bottom nav bar처럼 `NavController`가 있는 global navigation 패턴과 같은 일반적으로 애플리케이션의 [chrome](https://june0122.tistory.com/19) 요소를 연결하는 클래스입니다.

<small>원문: Class which hooks up elements typically in the 'chrome' of your application such as global navigation patterns like a navigation drawer or bottom nav bar with your NavController.</small>

#### onNavDestinationSelected(…)

주어진 MenuItem과 연결된 NavDesitination으로 탐색을 시도합니다. 이 MenuItem은 NavigationUI 클래스의 헬퍼 메서드 중 하나를 통해 추가되어야 합니다.

중요한 점은 menu item id가 탐색할 유효한 action id 또는 destination id와 일치한다고 가정한다는 것입니다.

기본적으로 백 스택은 navigation graph의 start destination으로 다시 팝업<small>(popped back)</small>됩니다. `android:menuCategory="secondary"`가 있는 Menu items는 백 스택을 팝업하지 않습니다.

- Params
  - item: 선택된 MenuItem
  - navController: destination을 호스팅하는 NavController
- Returns
  - NavController가 주어진 MenuItem과 연결된 destination으로 탐색할 수 있으면 True입니다.

## 정리

아래의 동작 화면처럼 Nested navigation graphs 또는 setOnItemSelectedListener를 사용하는 것으로 BottomNavigationView의 아이템이 선택되지 않는 문제점을 해결할 수 있습니다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/179753298-a31218c7-c902-4b4a-b781-0f6ab9f6c375.gif'>
</p>

그런데 주의할 점은 위에서 언급했듯이 Navigation 2.4.0 버전 이상이 아닐 경우 Multiple back stacks를 지원하지 않으므로 위와 같은 동작을 얻지 못합니다. 아래는 Navigation 2.3.5 버전에서 두 가지 해결 방법을 적용했을 때의 화면으로 BottomNavigationView의 탭은 잘 선택이 되지만 이전 destination의 상태는 유지하지 못하고 해당 탭의 초기 destination으로 이동하는 것을 확인할 수 있습니다.

<p align = 'center'>
<img width = '300' src = 'https://user-images.githubusercontent.com/39554623/180016007-690f8d4f-8fa9-47e1-8e64-215a8484d106.gif'>
</p>

본문에서 다룬 이슈는 아래에 첨부한 Stackoverflow 또는 Google Issue Tracker에서도 자세히 다루고 있으므로 참고하시면 좋을 것 같습니다.

## References

- Stackoverflow: [Android Navigation Component : BottomNavigationView's selected tab icon is not updated](https://stackoverflow.com/q/71089052/12364882)
- Google Issue Tracker: [Tab selection does not update 
](https://issuetracker.google.com/issues/210687967#comment2)
- Android Developers: [Support multiple back stacks](https://developer.android.com/guide/navigation/multi-back-stacks)
- Android Developers: [Nested navigation graphs](https://developer.android.com/guide/navigation/navigation-nested-graphs)