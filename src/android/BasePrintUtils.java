package com.print.printer.hardware.printer;

import android.content.Context;

/**
 * <pre>
 *      author : shihaoyu
 *      e-mail : hao751333636@qq.com
 *      time   : 2017/10/16
 *      desc   :
 *      version: 1.0
 *  </pre>
 */

public class BasePrintUtils {

    protected boolean openPrinter() {
        boolean isSuccess = PosPrinter.getPrinter().openPrinter();
        return isSuccess;
    }

    protected boolean isAvailablePrinter() {
        boolean isSuccess = PosPrinter.getPrinter().isAvailable();
        return isSuccess;
    }


    protected boolean printText(Context context,int alignment,int lineHeight, String text) {
        synchronized (context) {
            boolean isSuccess = PosPrinter.getPrinter().printText(text,
                    PosPrinter.FONT_TYPE_STANDARD_ASCII, PosPrinter.FONT_STYLE_NORMAL,
                    alignment, 0, 0, lineHeight, 1, 1);
            return isSuccess;
        }

    }

    protected boolean closePrinter() {
        boolean isSuccess = PosPrinter.getPrinter().closePrinter();
        return isSuccess;
    }


    private void addText(StringBuilder builder, String s) {
        builder.append(s);
        builder.append("\n");
    }

//    public boolean printBitmap(Context context) {
//        synchronized (context) {
//            boolean isSuccess = PosPrinter.getPrinter()
//                    .printBitmap(
//                            ((BitmapDrawable) getResources()
//                                    .getDrawable(
//                                            R.mipmap.ic_launcher))
//                                    .getBitmap());
//            return isSuccess;
//        }
//    }

    protected boolean printQrcode(Context context, String content) {
        return printQrcode(context, content, 0, 6);
    }

    protected boolean printQrcode(Context context, String content, int orgx, int elementWidth) {
        synchronized (context) {
            boolean isSuccess = PosPrinter
                    .getPrinter()
                    .printQR(
                            content,
                            orgx, elementWidth);
            return isSuccess;
        }
    }
}
