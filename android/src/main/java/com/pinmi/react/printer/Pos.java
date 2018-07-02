package com.pinmi.react.printer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.Socket;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class Pos {
    //定义编码方式
    private static String encoding = null;

    private Socket sock = null;
    // 通过socket流进行读写
    private OutputStream socketOut = null;
    private OutputStreamWriter writer = null;

    /**
     * 初始化Pos实例
     *
     * @param ip 打印机IP
     * @param port  打印机端口号
     * @param encoding  编码
     * @throws IOException
     */
    public Pos(String ip, int port, String encoding) throws IOException {
        sock = new Socket(ip, port);
        socketOut = new DataOutputStream(sock.getOutputStream());
        // socketOut = sock.getOutputStream();
        this.encoding = encoding;
        writer = new OutputStreamWriter(socketOut, encoding);
    }

    protected void testPos() throws IOException {
        writer.write(27);
        writer.write(64);
        writer.flush();
        writer.write(27);
        writer.write(50);
        writer.flush();
        writer.write(10);
        writer.flush();
        // writer.write(27);
        // writer.write(97);
        // writer.write(0);
        // writer.flush();
        writer.write(29);
        writer.write(33);
        writer.write(0);
        writer.flush();
        writer.write(27);
        writer.write(50);
        writer.flush();
        writer.write(27);
        writer.write(36);
        writer.write(0);
        writer.write(1);
        writer.flush();
        writer.write(97);
        writer.flush();
        writer.write(27);
        writer.write(36);
        writer.write(0);
        writer.write(0);
        writer.flush();
        writer.write(10);
        writer.flush();
        // writer.write(27);
        // writer.write(97);
        // writer.write(0);
        // writer.flush();
        writer.write(29);
        writer.write(33);
        writer.write(0);
        writer.flush();
        writer.write(27);
        writer.write(50);
        writer.flush();
        writer.write(10);
        writer.write(10);
        writer.write(10);
        writer.write(10);
        writer.write(10);
        writer.flush();
        writer.write(27);
        writer.write(105);
        writer.flush();
        writer.write(27);
        writer.write(66);
        writer.write(0);
        writer.write(2);
        writer.flush();

        // int[] bytes = {27, 64, 27, 50, 10, 27, 97, 0, 29, 33, 0, 27, 50, 27, 36, 200, 1, 97, 27, 36, 0, 0, 10, 27, 97, 0, 29, 33, 0, 27, 50, 10, 10, 10, 10, 10, 27, 105, 27, 66, 0, 2};

        // for (int i = 0; i < bytes.length; i++) {
        //     writer.write((byte) bytes[i]);
        // }
        // writer.flush();

        // socketOut.write(bytes);
        // socketOut.flush();

        // 初始化
        // writer.write(0x1B); // 27
        // writer.write(0x40); // 64

        // // 行间距
        // writer.write(27);
        // writer.write(50);

        // // // 绝对位置
        // writer.write(0x1B); // 27
        // writer.write(0x24); // 36
        // writer.write(100);
        // writer.write(1);

        // writer.flush();

        // // 打印文字
        // String s = "test";
        // byte[] content = s.getBytes("gbk");
        // socketOut.write(content);
        // socketOut.flush();

        // // 切纸
        // writer.write(0x1D);
        // writer.write(86);
        // writer.write(65);
        // // writer.write(100);
        // writer.flush();
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    protected void initPos() throws IOException {
      // 初始化
      writer.write(0x1B); // 27
      writer.write(0x40); // 64
      writer.write(12);
      
    //   writer.write(27);
    //   writer.write(33);
    //   writer.write(0);

      writer.flush();
    }

    /**
       * 打印位置调整
       *
       * @param position 打印位置  0：居左(默认) 1：居中 2：居右
       * @throws IOException
       */
      protected void printLocation(int position) throws IOException {
        writer.write(27);
        writer.write(97);
        writer.write(position);
        writer.flush();
    }

    /**
     * 绝对打印位置
     *
     * @throws IOException
     */
    protected void printLocation(int nL, int nH) throws IOException {
        // writer.write(29);
        // writer.write(80);
        // writer.write(25);
        // writer.write(25);

        writer.write(0x1B); // 27
        writer.write(0x24); // 36
        writer.write(nL);
        writer.write(nH);
        writer.flush();
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    protected void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            writer.write("\n");
        }
        writer.flush();
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    protected void printLine() throws IOException {
        writer.write("\n");
        writer.flush();
    }

    protected void testSpace() throws IOException {
        writer.write(27);
        writer.write(68);
        writer.write(50);
        writer.write(200);
        writer.write(0);
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    protected void printTabSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            writer.write("\t");
        }
        writer.flush();
    }

    /**
     * 打印空白（一个汉字的位置）
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    protected void printWordSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            writer.write("  ");
        }
        writer.flush();
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    protected void printText(String text) throws IOException {
        String s = text;
        byte[] content = s.getBytes("gbk");
        socketOut.write(content);
        socketOut.flush();
    }

    /**
     * 新起一行，打印文字
     *
     * @param text
     * @throws IOException
     */
    protected void printTextNewLine(String text) throws IOException {
        //换行
        writer.write("\n");
        writer.flush();

        String s = text;
        byte[] content = s.getBytes("gbk");
        socketOut.write(content);
        socketOut.flush();
    }

    protected void fontSize(int size) throws IOException {
      if (size == 0) {
        writer.write(27);
        writer.write(33);
        writer.write(0);
        writer.write(28);
        writer.write(33);
        writer.write(0);
        writer.flush();
      } else if (size == 1) { // 中字体
        writer.write(27);
        writer.write(33);
        writer.write(16);
        writer.write(28);
        writer.write(33);
        writer.write(8);
        writer.flush();
      } else if (size == 2) { // 大字体
        writer.write(27);
        writer.write(33);
        writer.write(48);
        writer.write(28);
        writer.write(33);
        writer.write(12);
        writer.flush();
      }
    }

    /**
     * 加粗
     *
     * @param flag false为不加粗
     * @return
     * @throws IOException
     */
    protected void bold(boolean flag) throws IOException {
        if (flag) {
            //常规粗细
            writer.write(0x1B);
            writer.write(69);
            writer.write(0xF);
            writer.flush();
        } else {
            //加粗
            writer.write(0x1B);
            writer.write(69);
            writer.write(0);
            writer.flush();
        }
    }

    protected void reset() throws IOException {
      // byte[] btyes = {27, 97, 0, 29, 33, 0, 27, 50};
      // socketOut.write(btyes);
      // socketOut.flush();

      writer.write(27);
      writer.write(97);
      writer.write(0);
      writer.write(29);
      writer.write(33);
      writer.write(0);
      writer.write(27);
      writer.write(50);
      writer.flush();
    }

    public Bitmap getBitMap(String url) {
      URL myFileUrl = null;
      Bitmap bitmap = null;
  
      try {
          myFileUrl = new URL(url);
      } catch (MalformedURLException e) {
          e.printStackTrace();
      }
  
      try { HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); conn.setDoInput(true); conn.connect(); InputStream is = conn.getInputStream(); bitmap = BitmapFactory.decodeStream(is); is.close(); } catch (IOException e) { e.printStackTrace(); } return bitmap;
    }

    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param
     */
    public static Bitmap compressPic(Bitmap bitmap) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 指定调整后的宽度和高度
        int newWidth = 200;
        int newHeight = 200;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmap, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    /**
     * 灰度图片黑白化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return gray;
    }

    /*************************************************************************
     * 假设一个240*240的图片，分辨率设为24, 共分10行打印
     * 每一行,是一个 240*24 的点阵, 每一列有24个点,存储在3个byte里面。
     * 每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
    /**
     * 把一张Bitmap图片转化为打印机可以打印的字节流
     *
     * @param bmp
     * @return
     */
    public void draw2PxPoint(Bitmap bmp) throws IOException {

        //用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
        //整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，
        //但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
        //所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        //设置行距为0的指令
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        // 逐行打印
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            //打印图片的指令
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256); //nL
            data[k++] = (byte) (bmp.getWidth() / 256); //nH
            //对于每一行，逐列打印
            for (int i = 0; i < bmp.getWidth(); i++) {
                //每一列24个像素点，分为3个字节存储
                for (int m = 0; m < 3; m++) {
                    //每个字节表示8个像素点，0表示白色，1表示黑色
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;//换行
        }
        socketOut.write(data);
        socketOut.flush();
    }

    /**
     * 通过url打印二维码
     *
     * @param qrData url 页面地址
     * @throws IOException
     */
    protected void qrCode(String qrData) throws IOException {
      int moduleSize = 8;
      int length = qrData.getBytes(encoding).length;

      //打印二维码矩阵
      writer.write(0x1D);
      writer.write("(k");// adjust height of barcode
      writer.write(length + 3); // pl
      writer.write(0); // ph
      writer.write(49); // cn
      writer.write(80); // fn
      writer.write(48); //
      writer.write(qrData);

      writer.write(0x1D);
      writer.write("(k");
      writer.write(3);
      writer.write(0);
      writer.write(49);
      writer.write(69);
      writer.write(48);

      writer.write(0x1D);
      writer.write("(k");
      writer.write(3);
      writer.write(0);
      writer.write(49);
      writer.write(67);
      writer.write(moduleSize);

      writer.write(0x1D);
      writer.write("(k");
      writer.write(3); // pl
      writer.write(0); // ph
      writer.write(49); // cn
      writer.write(81); // fn
      writer.write(48); // m

      writer.flush();

  }

  /**
     * 进纸并全部切割
     *
     * @return
     * @throws IOException
     */
    protected void feedAndCut() throws IOException {
    //   writer.write(10);
    //   writer.write(10);
    //   writer.write(10);

      writer.write(0x1D); // 29
      writer.write(86);
    //   writer.write(0);
      writer.write(66);
      //  切纸前走纸多少
      writer.write(100);
      writer.flush();

      //另外一种切纸的方式
//                byte[] bytes = {29, 86, 0};
//                socketOut.write(bytes);
  }

    /**
     * 关闭IO流和Socket
     *
     * @throws IOException
     */
    protected void closeIOAndSocket() throws IOException {
      writer.close();
      socketOut.close();
      sock.close();
  }
}