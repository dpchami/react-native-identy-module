# React-Native Library For Identy SDK

React-Native library to integrate Identy SDK

Package name: **react-native-identy-module**

## Installation
To install this package in your react-native application

**run:**
```sh
npm install react-native-identy-module
```

## Instructions
For the package to work in your application, Kindly follow the below instructions
__instructions for android only__

1. First, you need to register on [identy.io](https://identy.io/) website and acquire license file *XXX.lic*

2. Then copy the *XXX.lic* file to src/main/assets folder of your application

3. Modify the global gradle file **build.gradle** add jcenter() and identy maven repositories with your username and password provided by the [identy.io](https://identy.io/).
![gradleBuild](https://user-images.githubusercontent.com/19475836/156916746-c96e8c7d-47f7-4d9f-a2af-4390261f0410.PNG)

4. Add application memory setting on **gradle.properties** file insert code
```
org.gradle.jvmargs=-Xmx1536m
```![gradleProperties](https://user-images.githubusercontent.com/19475836/156916775-33cde607-83ca-4855-8e5e-f55796d491d1.PNG)

## Supported Platforms
- Android
- iOS (coming soon)

## Usage

```js
import { getFingerprint } from "react-native-identy-module";

// ...

const data = await getFingerprint();
```

## License

MIT
