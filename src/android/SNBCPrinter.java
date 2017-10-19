package com.print.printer.hardware.printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;



import java.io.UnsupportedEncodingException;

import cn.koolcloud.jni.PrinterInterface;


/**
 * This is a SNBC's printer
 * 
 * @author Leon
 */
public class SNBCPrinter extends PosPrinter {
    private static final String GB18030_CHARSET_NAME = "GB18030";
    private static final String TAG = SNBCPrinter.class.getName();

    private boolean isOpened;
    private int mPrintMode = PRINT_MODE_STANDARD;

    @Override
    public boolean openPrinter() {
        int result = PrinterInterface.open();
        if (result == -10) {
            Log.d(TAG, "DEVNODE_ERROR");
        } else if (result == -11) {
            Log.d(TAG, "ACCESS_DENIED");
        } else if (result < 0) {
            Log.d(TAG, "DEVICE_OPEN_ERROR");
        } else {
            int beginResult = PrinterInterface.begin();
            if (beginResult != -1) {
                isOpened = true;
                return true;
            } else {
                Log.d(TAG, "NOPAPER_ERROR");
            }
        }
        return false;
    }

    @Override
    public boolean closePrinter() {
        int result = PrinterInterface.end();
        if (result >= 0) {
            result = PrinterInterface.close();
            if (result < 0) {
                Log.d(TAG, "DEVICE_OPEN_ERROR");
            } else {
                isOpened = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        return isOpened;
    }

    @Override
    public PrinterStatus queryStatus() {
        return null;
    }

    /**
     * width :640 580
     */
    @Override
    public boolean initPrinter() {
        if (!isAvailable()) {
            return false;
        }

        boolean isSuccess;
        mPrintMode = PRINT_MODE_STANDARD;
        mPrintAreaWidth = SNBCPosPrinterConfig.PRINT_AREA_WIDTH;
        mPrintAreaHeight = SNBCPosPrinterConfig.PRINT_AREA_HEIGHT;

        if (mPrintMode == PRINT_MODE_PAGE) {
            // *****************************************************************************************
            // set print area
            isSuccess = pageModeSetPrintArea(0, 0, mPrintAreaWidth,
                    mPrintAreaHeight, 0);
            if (!isSuccess) {
                return false;
            }

            // set print position
            isSuccess = pageModeSetStartingPosition(0, 20);
            return isSuccess;
        } else {
            // set print area
            isSuccess = standardModeSetPrintAreaWidth(0, mPrintAreaWidth);
            return isSuccess;
        }
    }

    private boolean pageModeSetPrintArea(int x, int y, int areaWidth,
            int areaHeight, int direction) {
        boolean isSuccess = write(SNBCPrinterCommand.getPageModeSetPrintArea(x,
                y, areaWidth, areaHeight));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand
                .getPageModeSetPrintDirection(direction));
        return isSuccess;
    }

    private boolean pageModeSetStartingPosition(int x, int y) {
        boolean isSuccess = write(SNBCPrinterCommand
                .getStandardModeSetHorStartingPosition(0, x));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand.getPageModeSetVerStartingPosition(
                0, y));
        return isSuccess;
    }

    private boolean standardModeSetPrintAreaWidth(int LeftMargin, int Width) {
        boolean isSuccess = write(SNBCPrinterCommand
                .getStandardModeSetLeftMargin(LeftMargin));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand
                .getStandardModeSetPrintAreaWidth(Width));
        return isSuccess;
    }

    @Override
    public boolean flushPrinter() {
        if (!isAvailable()) {
            return false;
        }

        if (mPrintMode == PRINT_MODE_PAGE) {
            // ******************************************************************************************
            // print in page mode
            boolean isSuccess = write(SNBCPrinterCommand.getPageModePrint());
            if (!isSuccess) {
                return false;
            }
            // *****************************************************************************************
            // clear buffer in page mode
            isSuccess = write(SNBCPrinterCommand.getPageModeClearBuffer());
            return isSuccess;
        }
        return false;
    }

    @Override
    public boolean feedPaper(int lineCnt) {
        if (!isAvailable()) {
            return false;
        }

        boolean isSuccess = textSetLineHeight(SNBCPosPrinterConfig.DEFAULT_FEED_PAPER_LINE_HEIGHT);
        if (!isSuccess) {
            return false;
        }
        isSuccess = systemFeedLine(lineCnt);
        return isSuccess;
    }

    private boolean textSetLineHeight(int height) {
        if (height == 0) {
            return write(FormatSetting_Command.getESC2());
        } else {
            return write(FormatSetting_Command.getESC3n((byte) height));
        }
    }

    private boolean systemFeedLine(int height) {
        for (int i = 0; i < height; i++) {
            boolean isSuccess = write(SNBCPrinterCommand.systemFeedLine());
            if (!isSuccess) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean cutPaper() {
        if (!isAvailable()) {
            return false;
        }
        return systemCutPaper(66, 0);
    }

    private boolean systemCutPaper(int cutMode, int feedDistance) {
        return write(SNBCPrinterCommand.systemCutPaper(cutMode, feedDistance));
    }

    @Override
    public boolean printBitmap(Bitmap bitmap) {
        if (!isAvailable() || bitmap == null || bitmap.isRecycled()) {
            return false;
        }

        Bitmap newBitmap = imageInitWithUIImage(bitmap, mPrintAreaWidth);

        int image_h_src = 0;
        int image_w_src = 0;
        int image_row_bytes = 0;
        byte[] BitMask = { -128, 64, 32, 16, 8, 4, 2, 1 };

        int img_height = 0;
        int img_width = 0;
        int Pixel_val = 0;
        int gray_val = 0;

        image_h_src = newBitmap.getHeight();
        image_w_src = newBitmap.getWidth();

        img_width = image_h_src;
        img_height = (image_w_src + 31 >> 3 & 0xFFFFFFFC) << 3;
        image_row_bytes = img_width + 7 >> 3;

        byte[] buffer = new byte[img_height * image_row_bytes];

        int col_index_dest = img_width - 1;
        for (int row_index_src = image_h_src - 1; row_index_src >= 0; col_index_dest--) {
            byte mask_code = BitMask[(col_index_dest & 0x7)];

            for (int col_index_src = 0; col_index_src < image_w_src; col_index_src++) {
                Pixel_val = newBitmap.getPixel(col_index_src, row_index_src);
                gray_val = getGreyLevel(Pixel_val, 1.0F);
                if (gray_val < 127) {
                    int tmp195_194 = (image_row_bytes * col_index_src + (col_index_dest >> 3));
                    byte[] tmp195_184 = buffer;
                    tmp195_184[tmp195_194] = ((byte) (tmp195_184[tmp195_194] | mask_code));
                }
            }
            row_index_src--;
        }

        boolean isSuccess = write(new byte[] { 29, 35, 2 });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(new byte[] { 29, 42,
                (byte) ((byte) (img_height + 7 >> 3) & 0xFF),
                (byte) ((byte) (img_width + 7 >> 3) & 0xFF) });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(buffer);
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(SNBCPrinterCommand.imageRAMPrint(2, 0));
        if (!isSuccess) {
            return false;
        }

        return write(SNBCPrinterCommand.printBitmap());

    }

    private Bitmap imageInitWithUIImage(Bitmap bitmap, int printAreaWidth) {
        int image_height = bitmap.getHeight();
        int image_width = bitmap.getWidth();

        if ((image_width > printAreaWidth) && (printAreaWidth != 0)) {
            int image_width_dest = printAreaWidth;
            int image_height_dest = (int) (image_height * printAreaWidth / image_width);
            return Bitmap.createScaledBitmap(bitmap, image_width_dest,
                    image_height_dest, false);
        } else {
            return Bitmap.createScaledBitmap(bitmap, image_width, image_height,
                    false);
        }
    }

    private int getGreyLevel(int pixel, float intensity) {
        float red = Color.red(pixel);
        float green = Color.green(pixel);
        float blue = Color.blue(pixel);
        float parcial = red + green + blue;
        parcial = (float) (parcial / 3.0D);
        int gray = (int) (parcial * intensity);
        if (gray > 255) {
            gray = 255;
        }
        return gray;
    }

    @Override
    public boolean printBarcode(String data, int barcodeType, int elementWidth,
                                int height, int hriFontPosition) {
        boolean isSuccess = write(SNBCPrinterCommand
                .barcodeSetModuleWidth(elementWidth));
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(SNBCPrinterCommand.barcodeSelectBarcodeHeight(height));
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(SNBCPrinterCommand
                .barcodeSelectHriFontType(HRI_FONT_TYPE_STANDARD_ASCII));
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(SNBCPrinterCommand
                .barcodeSetHriFontPosition(hriFontPosition));
        if (!isSuccess) {
            return false;
        }

        byte[] buffer = null;
        try {
            buffer = data.getBytes(GB18030_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        write(SNBCPrinterCommand.barcodePrint1Dimension(barcodeType,
                buffer.length));
        return write(buffer);

    }

    @Override
    public boolean printQR(String data, int orgx, int elementWidth) {
        if (!isAvailable()) {
            return false;
        }
        // TODO
        boolean isSuccess = write(SNBCPrinterCommand
                .getStandardModeSetHorStartingPosition(0, orgx));
        if (!isSuccess) {
            return false;
        }

        // base koolcloud code
        byte[] bytshy = null;
        byte len = 0;
        try {
            bytshy = data.getBytes("utf-8");
            len = (byte) data.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
        }

        isSuccess = write(new byte[] { 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31,
                0x43, 0x08 });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(new byte[] { 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31,
                0x45, 0x30 });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(new byte[] { 0x1D, 0x28, 0x6B,
                (byte) (len + (byte) 0x03), 0x00, 0x31, 0x50, 0x30 });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(bytshy);
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(new byte[] { 0x1b, 0x61, 0x01 });
        if (!isSuccess) {
            return false;
        }

        isSuccess = write(new byte[] { 0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31,
                0x52, 0x30 });
        if (!isSuccess) {
            return false;
        }

        return write(new byte[] { 0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x51,
                0x30 });
    }

    @Override
    public boolean printText(String data, int fontType, int fontStyle,
                             int alignment, int horStartingPosition, int verStartingPosition,
                             int lineHeight, int horizontalFontScale, int verticalFontScale) {
        if (!isAvailable()) {
            return false;
        }

        if (mPrintMode == PRINT_MODE_STANDARD) {
            // set the alignment type
            boolean isSuccess = textStandardModeAlignment(alignment);
            if (!isSuccess) {
                return false;
            }
        }

        // set the horizontal and vertical motion units
        boolean isSuccess = write(SNBCPrinterCommand.systemSetMotionUnit(
                SNBCPosPrinterConfig.MOTION_UNIT_X,
                SNBCPosPrinterConfig.MOTION_UNIT_Y));
//        boolean isSuccess = write(SNBCPrinterCommand.systemSetMotionUnit(
//                horStartingPosition,
//                verStartingPosition));
        if (!isSuccess) {
            return false;
        }

        // set line height
        isSuccess = textSetLineHeight(lineHeight);
        if (!isSuccess) {
            return false;
        }

        int fontStyle2 = 0;
        if ((fontStyle & FONT_STYLE_REVERSE) > 0) {
            fontStyle2 |= 0x400;
        }
        if ((fontStyle & FONT_STYLE_BOLD) > 0) {
            fontStyle2 |= 0x08;
        }
        if ((fontStyle & FONT_STYLE_UNDERLINE) > 0) {
            fontStyle2 |= 0x80;
        }
        // set character font
        isSuccess = textSelectFont(fontType, fontStyle2);
        if (!isSuccess) {
            return false;
        }

        // set character size
        isSuccess = write(SNBCPrinterCommand.textSelectFontMagnifyTimes(
                horizontalFontScale, verticalFontScale));
        if (!isSuccess) {
            return false;
        }

        // print text
        isSuccess = printText(data);
        if (!isSuccess) {
            return false;
        }

        // feed line
        isSuccess = systemFeedLine(1);
        if (!isSuccess) {
            return false;
        }
        return isSuccess;
    }

    private boolean textStandardModeAlignment(int alignment) {
        return write(FormatSetting_Command.getESCan((byte) alignment));
    }

    private boolean textSelectFont(int fontType, int fontStyle) {
        boolean isSuccess = write(SNBCPrinterCommand
                .textSelectFontType(fontType));
        if (!isSuccess) {
            return false;
        }

        int nHigh = fontStyle / 255;
        int nLow = fontStyle % 255;

        isSuccess = write(SNBCPrinterCommand.textStyleReverse(nHigh >> 2 & 0x1));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand.textStyleSmooth(nHigh >> 3 & 0x1));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand.textStyleBold(nLow >> 3 & 0x1));
        if (!isSuccess) {
            return false;
        }
        isSuccess = write(SNBCPrinterCommand
                .textStandardModeUpsideDown(nHigh >> 1 & 0x1));
        if (!isSuccess) {
            return false;
        }

        if ((nLow & 0x80) == 128) {
            isSuccess = write(SNBCPrinterCommand
                    .textStyleUnderline(1, fontType));
        } else if ((nHigh & 0x1) == 1) {
            isSuccess = write(SNBCPrinterCommand
                    .textStyleUnderline(2, fontType));
        } else {
            isSuccess = write(SNBCPrinterCommand
                    .textStyleUnderline(0, fontType));
        }
        return isSuccess;
    }

    private boolean printText(String content) {
        try {
            byte[] buffer = content.getBytes(GB18030_CHARSET_NAME);
            if (buffer != null && buffer.length > 0) {
                return write(buffer);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return false;
    }

    @Override
    public boolean openCashBox() {
        if (!isAvailable()) {
            return false;
        }
        return write(new byte[] { 0x1B, 0x70, 0x00, (byte) 0xC8, (byte) 0xC8 });
    }

    private boolean write(byte[] cmd) {
        if (cmd != null) {
            for (byte b : cmd) {
                Log.d(TAG, b + "");
            }
            return PrinterInterface.write(cmd, cmd.length) == 0;
        }
        return false;
    }

}
