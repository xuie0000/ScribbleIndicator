Jetpack Compose - Scribble Indicator
---

my maven library project

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/xuie0000"><img alt="Author" src="https://img.shields.io/badge/Author-XuJie-red.svg?style=flat"/></a>
  <a href="https://github.com/xuie0000/ScribbleIndicator"><img alt="Profile" src="https://img.shields.io/github/v/release/xuie0000/ScribbleIndicator.svg"/></a> 
</p>

## Download
[![Maven Central](https://img.shields.io/maven-central/v/com.xuie0000/scribble-indicator.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=com.xuie0000%3Ascribble-indicator&smo=true)

available on `mavenCentral()`.

```groovy
implementation("com.xuie0000:scribble-indicator:<version>")
```

## Usage

```groovy
val pagerState: PagerState = rememberPagerState { list.size }

ScribbleIndicator(
    list = list,
    modifier = Modifier,
    pagerState = pagerState,
    title = {
    },
    content = {
    },
)
```

[效果图](https://github.com/xuie0000/ScribbleIndicator/assets/8099426/5330fc14-76df-4a89-a05d-665f35998bc2)
