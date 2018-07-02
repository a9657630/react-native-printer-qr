package com.pinmi.react.printer;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.net.UnknownHostException;

public class RNNetPrinter extends ReactContextBaseJavaModule {
  private Pos pos;

  public RNNetPrinter(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "NetPrinter";
  }

  private String t = "text";

  @ReactMethod
  public void printMessage(String _ip, Integer _port, String _text) {
    final String ip = _ip;
    final Integer port = _port;
    // final String text = _text;
    final String[] arr = _text.split("\\|\\|\\|");
    new Thread() {
      public void run() {
        try {
          pos = new Pos(ip, port, "GBK");
          
          // pos.testPos();

          pos.initPos();

          for (int j = 0; j < arr.length; j++) {
            String text = arr[j];
            String temp = "";
            String ch = "";
            int l = text.length();
            int endIndex = 0;
            
            for (int i = 0; i < l; i++) {
              ch = text.charAt(i) + "";
              // ch = text.substring(i, i + 1);
              // pos.printText(ch);
              if (ch.equals("<")) {
                // pos.printText("<");
                pos.printText(temp);
                temp = "";
                
                String s3 = text.substring(i, i + 3);
                String s4 = text.substring(i, i + 4);
                String s5 = text.substring(i, i + 5);
                
                if (s3.equals("<B>")) {
                  pos.bold(true);
                  pos.fontSize(2); // 大字体
                  i += 2;
                } else if (s4.equals("</B>")) {
                  pos.bold(false);
                  pos.fontSize(0); // 恢复小字体
                  i += 3;
                } else if (s4.equals("<CB>")) {
                  pos.printLocation(1); // 居中
                  pos.fontSize(2); // 大字体
                  i += 3;
                } else if (s5.equals("</CB>")) {
                  pos.fontSize(0); // 恢复小字体
                  i += 4;
                } else if (s4.equals("<CM>")) {
                  pos.printLocation(1); // 居中
                  pos.fontSize(1); // 中字体
                  i += 3;
                } else if (s5.equals("</CM>")) {
                  pos.fontSize(0); // 恢复小字体
                  i += 4;
                } else if (s4.equals("<CL>")) {
                  pos.printLocation(1); // 居中
                  pos.fontSize(2); // 大字体
                  i += 3;
                } else if (s5.equals("</CL>")) {
                  pos.fontSize(0); // 恢复小字体
                  i += 4;
                } else if (s4.equals("<A1>")) {
                  pos.printLocation(90, 1);
                  pos.printWordSpace(1);
                  i += 3;
                } else if (s5.equals("</A1>")) {
                  // pos.printLocation(0, 0);
                  i += 4;
                } else if (s4.equals("<A2>")) {
                  // pos.printLocation(2);
                  // pos.testSpace();
                  pos.printLocation(90, 1);
                  pos.printWordSpace(3);
                  i += 3;
                } else if (s5.equals("</A2>")) {
                  // pos.printLocation(0, 0);
                  i += 4;
                } else if (s4.equals("<QR>")) {
                  pos.printLine();
                  pos.printLocation(1);
                  int end = text.indexOf("</QR>", i + 4);
                  String t = text.substring(i + 4, end);
                  if (t.startsWith("http")) {
                    pos.draw2PxPoint(pos.compressPic(pos.getBitMap(t)));
                  }
                  pos.printLine(2);
                  i = end + 5;
                }
              } else if (ch.equals("\n")) {
                temp += ch;
                pos.printText(temp);
                // pos.printText("'");
                // pos.printLocation(0);
                // pos.printLine();
                pos.reset();
                temp = "";
                // pos.printText("n");
              } else {
                // pos.printText("text");
                temp += ch;
              }
            }
  
            if (temp.length() > 0) {
              pos.printText(temp);
            }
  
            // 切纸
            pos.feedAndCut();
          }

          // pos.feedAndCut();

          pos.closeIOAndSocket();
          pos = null;
        } catch (UnknownHostException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }
}