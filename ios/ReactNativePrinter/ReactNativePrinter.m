//
//  ReactNativePrinter.m
//  ReactNativePrinter
//
//  Created by 高宁 on 2018/8/17.
//  Copyright © 2018年 高宁. All rights reserved.
//

#import "ReactNativePrinter.h"
#import "GCDAsyncSocket.h"
#import "PrinterManager.h"


@implementation ReactNativePrinter

- (void)socket:(GCDAsyncSocket *)sender didConnectToHost:(NSString *)host port:(UInt16)port
{
    NSLog(@"Cool, I'm connected! That was easy. %@, %hu", host, port);
}

- (void)socket:(GCDAsyncSocket *)sock didWriteDataWithTag:(long)tag
{
    if (tag == 1)
        NSLog(@"First request sent easy didWriteDataWithTag ");
    else if (tag == 2)
        NSLog(@"Second request sent");
}

- (void)socket:(GCDAsyncSocket *)sender didReadData:(NSData *)data withTag:(long)tag
{
    if (tag == 1)
    {
        NSLog(@"didReadData easy didReadData");
    }
}

- (void)socketDidDisconnect:(GCDAsyncSocket *)sock withError:(NSError *)err {
    NSLog(@"连接断开 easy socketDidDisconnect");
    
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(print:(NSString *)host port:(int)port info:(NSString *)info)
{
//    NSLog(@"Pretending to create an event %@ %d at %@", host, port, info);
    
    GCDAsyncSocket *socket = [[GCDAsyncSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    
    NSError *err = nil;
    if (![socket connectToHost:host onPort:port error:&err]) // Asynchronous!
    {
        // If there was an error, it's likely something like "already connected" or "no delegate set"
        NSLog(@"I goofed: %@", err);
    }
    
//    NSString *hh = @"192.168.31.100";
//    UInt16 port = 9100;
//    //    NSString *host = _ipTextField.text;
//    //    UInt16 port = (UInt16)[_portTextField.text integerValue];
//    NSTimeInterval timeout = 10;
//    MMReceiptManager *manager = [[MMReceiptManager alloc] initWithHost:hh port:port timeout:timeout];
//    [manager basicSetting];
//    [manager writeData_title:@"肯德基" Scale:scale_2 Type:MiddleAlignment];
//    [manager writeData_items:@[@"收银员:001", @"交易时间:2016-03-17", @"交易号:201603170001"]];
//    [manager writeData_line];
//    [manager writeData_content:@[@{@"key01":@"名称", @"key02":@"单价", @"key03":@"数量", @"key04":@"总价"}]];
//    [manager writeData_line];
//    [manager writeData_content:@[@{@"key01":@"汉堡", @"key02":@"10.00", @"key03":@"2", @"key04":@"20.00"}, @{@"key01":@"炸鸡", @"key02":@"8.00", @"key03":@"1", @"key04":@"8.00"}]];
//    [manager writeData_line];
//    [manager writeData_items:@[@"支付方式:现金", @"应收:28.00", @"实际:30.00", @"找零:2.00"]];
//    UIImage *qrImage = [MMQRCode qrCodeWithString:@"www.google.cn" logoName:@"kfc.gif" size:400];
//    [manager writeData_image:qrImage alignment:MiddleAlignment maxWidth:400];
//    [manager openCashDrawer];
//    [manager printReceipt];
    
//    self.asyncSocket = [[GCDAsyncSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    
//    NSError *error = nil;
//    if (![self.asyncSocket connectToHost:@"192.168.31.100" onPort:9100 error:&error]) {
//        NSLog(@"socket 连接失败");
//    }
//
//    //    打印机
//    Byte byte[] = {0x1B,0x40,0x0A,0x0A,0x0A,27, 105,27, 66, 3, 2};
//    Byte byte[] = {0x1B,0x40,0x1D,86,66,100};
//
//    NSData *adata = [[NSData alloc] initWithBytes:byte length:11];
    
    
    
    
    
    PrinterManager *printer = [[PrinterManager alloc] init];
    
    [printer initPrinter];
    
    
    NSString *text = @"";
    NSString *temp = nil;
    NSString *s3 = nil;
    NSString *s4 = nil;
    NSString *s5 = nil;
    
//    读取要打印的内容
//    <B>   加粗  大字体
//    <CB>  加粗  大字体 居中
//    <CM>  加粗  中字体 居中
//    <CL>  大字体 居中
//    <A1>
//    <A2>
    NSArray *array = [info componentsSeparatedByString:@"|||"];
    NSLog(@"array: %@", array);
    
    for (NSUInteger j = 0; j < array.count; j++) {
        NSString *string = array[j];
        NSUInteger len = string.length;
        
        for (NSUInteger i = 0; i < len; i++)
        {
            temp = [string substringWithRange:NSMakeRange(i, 1)];
            
            //        s3 = [string substringWithRange:NSMakeRange(i, 3)];
            //
            //        if ([s3 isEqualToString:@"|||"])
            //        {
            //            [printer printText:text];
            //            text = @"";
            //            [printer cut];
            //            i += 2;
            //        }
            
            if([temp isEqualToString:@"<"])
            {
                [printer printText:text];
                text = @"";
                
                s3 = [string substringWithRange:NSMakeRange(i, 3)];
                s4 = [string substringWithRange:NSMakeRange(i, 4)];
                s5 = [string substringWithRange:NSMakeRange(i, 5)];
                
                
                if ([s3 isEqualToString:@"<B>"])
                {
                    [printer setFontWeight:true];
                    [printer setFontSize:2];
                    i += 2;
                }
                else if ([s4 isEqualToString:@"</B>"])
                {
                    [printer setFontWeight:false];
                    [printer setFontSize:0];
                    i += 3;
                }
                else if ([s4 isEqualToString:@"<CB>"])
                {
                    [printer setFontWeight:true];
                    [printer printAlign:1];
                    [printer setFontSize:2];
                    i += 3;
                }
                else if ([s5 isEqualToString:@"</CB>"])
                {
                    [printer setFontWeight:false];
                    //                [printer printAlign:0];
                    [printer setFontSize:0];
                    i += 4;
                }
                else if ([s4 isEqualToString:@"<CM>"])
                {
                    [printer printAlign:1];
                    [printer setFontSize:1];
                    i += 3;
                }
                else if ([s5 isEqualToString:@"</CM>"])
                {
                    //                [printer printAlign:0];
                    [printer setFontSize:0];
                    i += 4;
                }
                else if ([s4 isEqualToString:@"<CL>"])
                {
                    [printer printAlign:1];
                    [printer setFontSize:2];
                    i += 3;
                }
                else if ([s5 isEqualToString:@"</CL>"])
                {
                    //                [printer printAlign:0];
                    [printer setFontSize:0];
                    i += 4;
                }
                else if ([s4 isEqualToString:@"<A1>"])
                {
                    [printer printLocation:90 nH:1];
                    [printer printText:@"  "];
                    i += 3;
                }
                else if ([s5 isEqualToString:@"</A1>"])
                {
                    i += 4;
                }
                else if ([s4 isEqualToString:@"<A2>"])
                {
                    [printer printLocation:90 nH:1];
                    [printer printText:@"  "];
                    [printer printText:@"  "];
                    [printer printText:@"  "];
                    i += 3;
                }
                else if ([s5 isEqualToString:@"</A2>"])
                {
                    i += 4;
                }
                else if ([s4 isEqualToString:@"<QR>"])
                {
                    [printer printAlign:1];
                    [printer printLine:1];
                    
                    NSUInteger location = i + 4;
                    NSUInteger length = [string rangeOfString:@"</QR>"].location - location;
                    NSString *url = [string substringWithRange:NSMakeRange(location, length)];
                    NSLog(@"url: %@", url);
                    [printer printImage:url];
                    [printer printLine:1];
                    i += (3 + length + 4 + 1);
                }
                else if ([s3 isEqualToString:@"|||"])
                {
                    [printer cut];
                    i += 2;
                }
            }
            else if ([temp isEqualToString:@"\n"])
            {
                [printer printText:text];
                [printer printLine:1];
                [printer printReset];
                text = @"";
            }
            else
            {
                text = [text stringByAppendingString:temp];
            }
        }
        
        [printer printText:text];
        text = @"";
        
        [printer cut];
    }
    
    
    [socket writeData:printer.mdata withTimeout:-1 tag:1];
    
    

//    NSMutableData *mdata = [NSMutableData dataWithCapacity:0];
//
//
////    初始化打印机
//    Byte byte[] = {0x1B,0x40,12,27,97,2,27,33,48,28,33,12};
//    [mdata appendBytes:byte length:12];
////    NSLog(@"byte: %s", byte);
//
////    字符串
////    NSString *aString = @"1324我们\n";
//    NSString *aString = info;
//    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
//    NSData *data = [aString dataUsingEncoding:enc];
//    NSLog(@"data: %@", data);
//    [mdata appendData:data];
//
////    切纸
//    Byte byte2[] = {27,33,0,28,33,0,0x1D,86,66,100};
//    [mdata appendBytes:byte2 length:10];
//    NSLog(@"mdata: %@", mdata);
//
//    [socket writeData:mdata withTimeout:-1 tag:1];
    
    
    
    
    
    
    
//    [socket disconnect];
}


@end
