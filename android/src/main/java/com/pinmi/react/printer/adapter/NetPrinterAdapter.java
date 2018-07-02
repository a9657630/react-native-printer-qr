package com.pinmi.react.printer.adapter;

import android.util.Base64;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.util.Collections;
import java.util.List;

// import java.com.pinmi.react.printer.Toolkit;

/**
 * Created by xiesubin on 2017/9/22.
 */

public class NetPrinterAdapter implements PrinterAdapter {
    private static NetPrinterAdapter mInstance;
    private ReactApplicationContext mContext;
    private String LOG_TAG = "RNNetPrinter";
    private NetPrinterDevice mNetDevice;

    private Socket mSocket;
    // private OutputStream socketOut;

    // private Toolkit toolkit;

    private NetPrinterAdapter (){

    }
    public static NetPrinterAdapter getInstance() {
        if(mInstance == null) {
            mInstance = new NetPrinterAdapter();

        }
        return mInstance;
    }

    @Override
    public void init(ReactApplicationContext reactContext, Callback successCallback, Callback errorCallback) {
        this.mContext = reactContext;
        successCallback.invoke();
    }

    @Override
    public List<PrinterDevice> getDeviceList(Callback errorCallback) {
        errorCallback.invoke("do not need to invoke get device list for net printer");
        return null;
    }

    @Override
    public void selectDevice(PrinterDeviceId printerDeviceId, Callback sucessCallback, Callback errorCallback) {
        NetPrinterDeviceId netPrinterDeviceId = (NetPrinterDeviceId)printerDeviceId;

        if(this.mSocket != null && !this.mSocket.isClosed() && mNetDevice.getPrinterDeviceId().equals(netPrinterDeviceId)){
            Log.i(LOG_TAG, "already selected device, do not need repeat to connect");
            sucessCallback.invoke(this.mNetDevice.toRNWritableMap());
            return;
        }

        try{
            Socket socket = new Socket(netPrinterDeviceId.getHost(), netPrinterDeviceId.getPort());
            if(socket.isConnected()) {
                closeConnectionIfExists();
                this.mSocket = socket;
                // socketout = new DataOutputStream(socket.getOutputStream());
                this.mNetDevice = new NetPrinterDevice(netPrinterDeviceId.getHost(), netPrinterDeviceId.getPort());
                sucessCallback.invoke(this.mNetDevice.toRNWritableMap());
            }else{
                errorCallback.invoke("unable to build connection with host: " + netPrinterDeviceId.getHost() + ", port: " + netPrinterDeviceId.getPort());
                return;
            }
        }catch (IOException e){
            e.printStackTrace();
            errorCallback.invoke("failed to connect printer: " + e.getMessage());
        }
    }

    @Override
    public void closeConnectionIfExists() {
        if(this.mSocket != null) {
            if(!this.mSocket.isClosed()){
                try{
                    this.mSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            this.mSocket = null;

        }
    }

    @Override
    public void printRawData(String rawBase64Data, Callback errorCallback) {
        if(this.mSocket == null){
            errorCallback.invoke("bluetooth connection is not built, may be you forgot to connectPrinter");
            return;
        }
        final String rawData = rawBase64Data;
        final Socket socket = this.mSocket;
        Log.v(LOG_TAG, "start to print raw data " + rawBase64Data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    byte [] bytes = Base64.decode(rawData, Base64.DEFAULT);
                    OutputStream printerOutputStream = socket.getOutputStream();
                    printerOutputStream.write(bytes, 0, bytes.length);
                    printerOutputStream.flush();
                }catch (IOException e){
                    Log.e(LOG_TAG, "failed to print data" + rawData);
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // @Override
    // public void printMessage(String text) {
    //     byte[] bytes;
    //     // byte[] bytes = toolkit.compile(text);
    //     String temp = "";
    //     String s = "";

    //     // 初始化打印机
    //     byte[] init = [0x1B, 0x40];
    //     socketout.write(init);
    //     socketout.flush();

    //     for (int i = 0, l = text.length; i < l; i++) {
    //         s = text.charAt(i);

    //         if (s == "<") {

    //         } else if (s == "\n") {

    //         } else {
    //             temp += s;
    //         }

    //     }
    // }
}
