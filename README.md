# React-Native Library for Identy SDK

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


3. Modify the **CaptureFinger.java** file. Change the variable `String licenseFile` with the name of your *XXX.lic* 



4. Modify the global gradle file **build.gradle** add jcenter() and identy maven repositories with your username and password provided by the [identy.io](https://identy.io/).

5. Add application memory setting on **gradle.properties** file insert code
```
org.gradle.jvmargs=-Xmx1536m
```

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
