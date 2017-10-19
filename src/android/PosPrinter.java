package com.print.printer.hardware.printer;

import android.graphics.Bitmap;

public abstract class PosPrinter {
    // Print Mode
    public static final int PRINT_MODE_STANDARD = 0;
    public static final int PRINT_MODE_PAGE = 1;

    // Print Status
    public static int PRINTER_STATUS_CASHDRAWER_OPEN = 0; // CashDrawer Open
    public static int PRINTER_STATUS_OFFLINE = 1; // Off-line
    public static int PRINTER_STATUS_COVER_OPEN = 2; // Cover Open
    public static int PRINTER_STATUS_FEEDING = 3; // Feeding
    public static int PRINTER_STATUS_PRINTER_ERROR = 4; // Printer Error
    public static int PRINTER_STATUS_CUTTER_ERROR = 5; // Cutter Error
    public static int PRINTER_STATUS_PAPER_NEAR_END = 6; // Paper Near End
    public static int PRINTER_STATUS_PAPER_END = 7; // Paper End

    // PortType value
    public static final int USBPORT = 1; // USB
    public static final int SERIALPORT = 2; // COM
    public static final int WIFIPORT = 3; // WIFI
    public static final int BLUETOOTHPORT = 4; // Bluetooth

    // Print Direction
    public static final int PRINT_DIRECTION_LEFT_TO_RIGHT = 0; // 从左向右打印
    public static final int PRINT_DIRECTION_BOTTOM_TO_TOP = 1; // 从下向上打印
    public static final int PRINT_DIRECTION_RIGHT_TO_LEFT = 2; // 从右向左打印
    public static final int PRINT_DIRECTION_TOP_TO_BOTTOM = 3; // 从上向下打印

    // Text Alignment
    public static final int TEXT_ALIGNMENT_LEFT = 0; // 左对齐
    public static final int TEXT_ALIGNMENT_CENTER = 1; // 居中对齐
    public static final int TEXT_ALIGNMENT_RIGHT = 2; // 右对齐

    // Font Type
    public static final int FONT_TYPE_STANDARD_ASCII = 0; // 标准ASCII
    public static final int FONT_TYPE_COMPRESSED_ASCII = 1; // 压缩ASCII
    public static final int FONT_TYPE_USER_DEFINED = 2; // 用户自定义字符
    public static final int FONT_TYPE_CHINESE = 3; // 中文字符

    // Font Style
    // 1) 黑白反显打印模式选择时，下划线模式不起作用
    // 2) 不对顺时针旋转90度和270度的字符加下划线
    // 3) 对中文选择参数
    // FontStyleUnderlineOneDotThick和FontStyleUnderlineTwoDotThck效果相同，均打印一点粗下划线
    public static final int FONT_STYLE_NORMAL = 1; // 正常
    public static final int FONT_STYLE_REVERSE = 1 << 1; // 反显
    public static final int FONT_STYLE_BOLD = 1 << 2;
    public static final int FONT_STYLE_UNDERLINE = 1 << 3; // 下划线

    // QR Symbol Type
    public static final int QR_SYMBOL_TYPE_ORIGINAL = 1; // 原始类型
    public static final int QR_SYMBOL_TYPE_ENHANCED = 2; // 增强型（建议）

    // QR Language Model
    public static final int QR_LANGUAGE_MODEL_CHINESE = 0; // 中文
    public static final int QR_LANGUAGE_MODEL_JAPANESE = 1; // 日文

    // Barcode Type
    public static final int BARCODE_TYPE_UPCA = 65;
    public static final int BARCODE_TYPE_UPCE = 66;
    public static final int BARCODE_TYPE_EAN13 = 67;
    public static final int BARCODE_TYPE_EAN8 = 68;
    public static final int BARCODE_TYPE_CODE39 = 69;
    public static final int BARCODE_TYPE_ITF = 70;
    public static final int BARCODE_TYPE_CODABAR = 71;
    public static final int BARCODE_TYPE_CODE93 = 72;
    public static final int BARCODE_TYPE_CODE128 = 73;

    // HRI Font Type
    public static final int HRI_FONT_TYPE_STANDARD_ASCII = 0; // 标准ASCII
    public static final int HRI_FONT_TYPE_COMPRESSED_ASCII = 1; // 压缩ASCII

    // HRI Font Position
    public static final int HRI_POSITION_NONE = 0; // 不打印HRI字符
    public static final int HRI_POSITION_ABOVE = 1; // HRI字符打印于条码上方
    public static final int HRI_POSITION_BELOW = 2; // HRI字符打印于条码下方
    public static final int HRI_POSITION_ABOVE_AND_BELOW = 3; // HRI字符打印于条码上方和下方

    protected int mPrintAreaWidth;
    protected int mPrintAreaHeight;

    private static PosPrinter mPrinter;

    protected int mMaxCharCount = SNBCPosPrinterConfig.MAX_CHAR_COUNT_580;

    private static final Object mLock = new Object();

    PosPrinter() {
    }

    public static final PosPrinter getPrinter() {
        synchronized (mLock) {
            if (mPrinter == null) {
                mPrinter = new SNBCPrinter();
            }
        }
        return mPrinter;
    }

    public void setMaxCharCount(int maxCharCount) {
        mMaxCharCount = maxCharCount;
    }

    public int getMaxCharCount() {
        return mMaxCharCount;
    }

    public abstract boolean openPrinter();

    public abstract boolean closePrinter();

    public abstract boolean isAvailable();

    public abstract PrinterStatus queryStatus();

    public abstract boolean initPrinter();

    public abstract boolean flushPrinter();

    public abstract boolean feedPaper(int lineCnt);

    public abstract boolean cutPaper();

    public abstract boolean printBitmap(Bitmap bitmap);

    public abstract boolean printBarcode(String data, int barcodeType,
                                         int elementWidth, int height, int hriFontPosition);

    public abstract boolean printQR(String data, int orgx, int elementWidth);

    public abstract boolean printText(String data, int fontType, int fontStyle,
                                      int alignment, int horStartingPosition, int verStartingPosition,
                                      int lineHeight, int horizontalFontScale, int verticalFontScale);

    public abstract boolean openCashBox();
}
