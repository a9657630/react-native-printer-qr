import React, { NativeModules } from 'react-native';

import EPToolkit from 'escpos-printer-toolkit';

var RNUSBPrinter = NativeModules.RNUSBPrinter;
var RNBLEPrinter = NativeModules.RNBLEPrinter;
var RNNetPrinter = NativeModules.RNNetPrinter;
var Printer = NativeModules.NetPrinter;


var textTo64Buffer = (text) => {
  let options = {
    beep: false, 
    cut: false, 
    tailingLine: false,
    encoding: 'GBK'
  }
  const buffer = EPToolkit.exchange_text(text, options)
  return buffer.toString('base64');
}

var billTo64Buffer = (text) => {
  let options = {
    beep: true, 
    cut: true, 
    encoding: 'GBK',
    tailingLine: true
  }
  const buffer = EPToolkit.exchange_text(text, options)
  return buffer.toString("base64");
}

export const USBPrinter = {
  init: () => 
    new Promise((resolve, reject) => 
      RNUSBPrinter.init(() => resolve(), (error) => reject(error))),
  
  getDeviceList: () => 
    new Promise((resolve, reject) => 
      RNUSBPrinter.getDeviceList((printers) => resolve(printers), (error) => reject(error))),

  connectPrinter: (vendorId, productId) => 
    new Promise((resolve, reject) => 
      RNUSBPrinter.connectPrinter(vendorId, productId, (printer) => resolve(printer), (error) => reject(error))),

  closeConn: () => new Promise((resolve, reject) => {
    RNUSBPrinter.closeConn();
    resolve();
  }),

  printText: (text) => RNUSBPrinter.printRawData(textTo64Buffer(text), (error) => console.warn(error)),

  printBill: (text) => RNUSBPrinter.printRawData(billTo64Buffer(text), (error) => console.warn(error)),
}


export const BLEPrinter = {
  init:  () => 
    new Promise((resolve, reject) => 
      RNBLEPrinter.init(() => resolve(), (error) => reject(error))),
  
  getDeviceList: () => 
    new Promise((resolve, reject) => 
      RNBLEPrinter.getDeviceList((printers) => resolve(printers), (error) => reject(error))),

  connectPrinter: (inner_mac_address) => 
    new Promise((resolve, reject) => 
      RNBLEPrinter.connectPrinter(inner_mac_address, (printer) => resolve(printer), (error) => reject(error))),

  closeConn: () => new Promise((resolve, reject) => {
    RNBLEPrinter.closeConn();
    resolve();
  }),

  printText: (text) => RNBLEPrinter.printRawData(textTo64Buffer(text), (error) => console.warn(error)),

  printBill: (text) => RNBLEPrinter.printRawData(billTo64Buffer(text), (error) => console.warn(error)), 
}

export const NetPrinter = {
  init:  () => 
    new Promise((resolve, reject) => 
      RNNetPrinter.init(() => resolve(), (error) => reject(error))),
  
  connectPrinter: (host, port) => 
    new Promise((resolve, reject) => 
      RNNetPrinter.connectPrinter(host, port, (printer) => resolve(printer), (error) => reject(error))),

  closeConn: () => new Promise((resolve, reject) => {
    RNNetPrinter.closeConn();
    resolve();
  }),

  printText: (text) => RNNetPrinter.printRawData(textTo64Buffer(text), (error) => console.warn(error)),

  printBill: (text) => RNNetPrinter.printRawData(billTo64Buffer(text), (error) => console.warn(error)), 

  printMessage: (ip, port, text) => Printer.printMessage(ip, port, text), 
}
