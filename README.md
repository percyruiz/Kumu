# Kumu Dev Challenge

This application displays a list of iTunes items obtained from iTunes Search API and shows detail views for each items.

## Installation
Clone this repository and import into **Android Studio**
```bash
https://github.com/percyruiz/Kumu.git
```

## Screenshots
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/126055691-95cc55c3-800f-4868-a778-abdb4de2fe2f.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/126055896-27f078d3-cb44-49f7-b533-4e0dc740cd06.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/126055933-707f75c6-4ea0-4356-874f-aefe9a37c050.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/126055998-81f1c7d9-c620-4357-97ce-5de3071f698f.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Architecture
The app uses MVVM architecture which takes advantage of Android Jetpack's Android Architecture Components https://developer.android.com/topic/libraries/architecture.
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/126056079-2c0e8155-2201-45e6-bf3f-514eda1c39ff.png" width="50%" height="50%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

With MVVM, components will have a clear separation and responsibilites. Creating tests is also easier since our components has smaller and simpler testable methods.<br/>
Android Architecture Components also makes this task easier since the components are lifecycle-aware which avoids memory leaks.<br/><br/>
For MVVM disadvantages, it can be sometimes an overkill for apps with simpler UI and data flows.

## Dependencies
- Dependency injection (with [Koin](https://www.koin.com/))
- Reactive programming (with [Kotlin Flows](https://kotlinlang.org/docs/reference/coroutines/flow.html))
- Http client (with [Retrofit](https://square.github.io/retrofit/))
- Google [Material Design](https://material.io/blog/android-material-theme-color) library
- Android architecture components
- Image loading (with [Glide](https://github.com/bumptech/glide))
- Social Style date and time formatting (with [PrettyTime](https://github.com/ocpsoft/prettytime))

## Maintainers
This project is mantained by:
* [Percival Ruiz](https://github.com/percyruiz)


