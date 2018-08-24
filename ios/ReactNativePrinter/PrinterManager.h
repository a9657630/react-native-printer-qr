//
//  PrinterManager.h
//  ReactNativePrinter
//
//  Created by 高宁 on 2018/8/24.
//  Copyright © 2018年 高宁. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

//像素
typedef enum {
    ALPHA = 0,
    BLUE = 1,
    GREEN = 2,
    RED = 3
} PIXELS;
//对齐方式
typedef enum :UInt8 {
    MNAlignmentTypeLeft = 0x30,
    MNAlignmentTypeCenter = 0x31,
    MNAlignmentTypeRight = 0x32
} MNAlignmentType;
//字符放大倍数
typedef enum: UInt8 {
    MNPrintFont_1 = 0x00,
    MNPrintFont_2 = 0x11,
    MNPrintFont_3 = 0x22,
    MNPrintFont_4 = 0x33,
    MNPrintFont_5 = 0x44,
    MNPrintFont_6 = 0x55,
    MNPrintFont_7 = 0x66,
    MNPrintFont_8 = 0x77,
} MNPrintFont;
//切纸模式
typedef enum :UInt8 {
    MNCutPaperModelFull = 0x00,
    MNCutPaperModelHalf = 0x01,
    MNCutPaperModelFeedPaperHalf = 0x66
}MNCutPaperModel;

@interface PrinterManager : NSObject

@property (nonatomic, strong) NSMutableData *mdata;

// 初始化打印机
-(void)initPrinter;

// 设置绝对位置
-(void)printLocation:(int)nL nH:(int)nH;

// 文字对齐方式: 0左对齐(默认) 1居中 2右对齐
-(void)printAlign:(int)position;

// 设置字体大小: 0正常字体 1中字体 2大字体
-(void)setFontSize:(int)size;

// 设置字体粗细: true粗 false细
-(void)setFontWeight:(BOOL)weight;

// 换行
-(void)printLine:(int)lineNum;

// 打印文字
-(void)printText:(NSString *)text;

// 打印图片
-(void)printImage:(NSString *)url;

// 重置打印格式
-(void)printReset;

// 切纸
-(void)cut;

@end
