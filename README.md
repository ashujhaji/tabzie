# Tabzie : Tab layout for Android

Tabzie is a customized Tab layout (Android UI component) which can be used to create animated tab layout in native android apps. It can be used by attaching it with android view pager as well.

## Getting started

### Setting up the dependency

The first step is to include Tabzie into your project, for example, as a Gradle compile dependency:


1) Add in project level build.gradle file 
```bash
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2) Add in app level build.gradle file 
```bash
implementation 'com.github.ashujhaji:Tabzie:0.0.1'
```

## Usage
### 1)Add tabzie in xml file with your viewpager


```xml
<com.pixerapps.tabby.TabLayout
     android:layout_width="match_parent"
     android:layout_height="@dimen/forty_five_dp"
     android:layout_margin="@dimen/ten_dp"
     android:layout_gravity="bottom"
     android:id="@+id/mainTab"
     app:siTextSize="@dimen/eleven_sp"
     app:siTextColor="@color/light_smoke"
     app:siSelectedTextColor="?colorPrimary"
     app:siIndicatorColor="?selectedItemColor"/>
```

### 2)Add tabzie in xml file with your viewpager


```kotlin
tabzie.setViewPager(viewPager)
```

### optional : You can also add icon in tabs


```kotlin
tabzie.getTabAt(0)
            .setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_1, 0,  0)
tabzie.getTabAt(1)
            .setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_2, 0, 0)
tabzie.getTabAt(2).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_3, 0, 0)
tabzie.getTabAt(3)
            .setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_4, 0, 0)
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
