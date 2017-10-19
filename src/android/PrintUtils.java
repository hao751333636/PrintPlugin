package com.print.printer.hardware.printer;

import android.content.Context;

/**
 * <pre>
 *      author : shihaoyu
 *      e-mail : shihaoyu@propersoft.cn
 *      time   : 2017/10/16
 *      desc   :
 *      version: 1.0
 *  </pre>
 */

public class PrintUtils extends BasePrintUtils {

    private static PrintUtils mPrinter;
    private static final Object mLock = new Object();

    public static final PrintUtils getPrinter() {
        synchronized (mLock) {
            if (mPrinter == null) {
                mPrinter = new PrintUtils();
            }
        }
        return mPrinter;
    }

    public boolean printTexts(Context context, String text) {
        return printTexts(context, PosPrinter.TEXT_ALIGNMENT_LEFT, 30, text);
    }

    public boolean printTexts(Context context, int alignment, int lineHeight, String text) {
        synchronized (context) {
            if (initPrinter()) {
                boolean isSuccess = printText(context, alignment, lineHeight, text);
                return isSuccess;
            } else {
                return false;
            }
        }
    }

    private boolean initPrinter() {
        if (isAvailablePrinter()) {
            return true;
        } else {
            //打开成功
            if (openPrinter()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean closePrinter() {
        return closePrinter();
    }

}
