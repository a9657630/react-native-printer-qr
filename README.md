# react-native-printer

A React Native Library to support Net printer for Android/iOS platform 

## Installation

```
npm install react-native-printer-qr --save-dev

```

## Integrate module

To integrate `react-native-printer-qr` with the rest of your react app just execute:

```
react-native link react-native-printer-qr

```

## Usage

```javascript
import { ReactNativePrinter } from 'react-native-printer-qr';

ReactNativePrinter.print('192.168.31.242', 9100, '<CB>这是一个标题</CB>')

```
