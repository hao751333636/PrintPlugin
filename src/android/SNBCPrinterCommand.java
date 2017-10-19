package com.print.printer.hardware.printer;

import java.util.Arrays;

/**
 * 基于koolcloud提供的，并有自己增加的新command
 */
public class SNBCPrinterCommand {

    public static byte[] getStandardModeSetLeftMargin(int LeftMargin) {
        if ((LeftMargin < 0) || (LeftMargin > 65535)) {
            LeftMargin = 0;
        }
        return new byte[] { 29, 76, (byte) ((byte) LeftMargin % 256),
                (byte) ((byte) LeftMargin / 256) };
    }

    public static byte[] getStandardModeSetPrintAreaWidth(int Width) {
        if ((Width < 0) || (Width > 65535)) {
            Width = 0;
        }
        int nHigh = Width / 256;
        int nLow = Width % 256;
        return new byte[] { 29, 87, (byte) nLow, (byte) nHigh };
    }

    public static byte[] getPageModeSetPrintArea(int X, int Y, int AreaWidth,
            int AreaHeight) {
        if ((X < 0) || (X > 65535)) {
            X = 0;
        }
        if ((Y < 0) || (Y > 65535)) {
            Y = 0;
        }
        if ((AreaWidth < 0) || (AreaWidth > 65535)) {
            AreaWidth = 0;
        }
        if ((AreaHeight < 0) || (AreaHeight > 65535)) {
            AreaHeight = 0;
        }

        byte[] pszCommand = new byte[10];

        int nOrgxH = X / 256;
        int nOrgxL = X % 256;

        int nOrgyH = Y / 256;
        int nOrgyL = Y % 256;

        int nWidthH = AreaWidth / 256;
        int nWidthL = AreaWidth % 256;

        int nHighH = AreaHeight / 256;
        int nHighL = AreaHeight % 256;

        pszCommand[0] = 27;
        pszCommand[1] = 87;
        pszCommand[2] = ((byte) nOrgxL);
        pszCommand[3] = ((byte) nOrgxH);
        pszCommand[4] = ((byte) nOrgyL);
        pszCommand[5] = ((byte) nOrgyH);
        pszCommand[6] = ((byte) nWidthL);
        pszCommand[7] = ((byte) nWidthH);
        pszCommand[8] = ((byte) nHighL);
        pszCommand[9] = ((byte) nHighH);

        return pszCommand;
    }

    public static byte[] getPageModeSetPrintDirection(int direction) {
        if ((direction < 0) || (direction > 3)) {
            direction = 0;
        }
        return new byte[] { 27, 84, (byte) direction };
    }

    public static byte[] getStandardModeSetHorStartingPosition(int mode,
            int distance) {

        if ((distance < 0) || (distance > 65535)) {
            distance = 0;
        }
        if ((mode < 0) || (mode > 1)) {
            mode = 0;
        }
        int nHigh = distance / 256;
        int nLow = distance % 256;

        byte[] pszCommand = new byte[4];
        Arrays.fill(pszCommand, (byte) 0);
        pszCommand[0] = 27;
        pszCommand[1] = 0;
        pszCommand[2] = ((byte) nLow);
        pszCommand[3] = ((byte) nHigh);

        if (mode == 0) {
            pszCommand[1] = 36;
        } else {
            pszCommand[1] = 92;
        }

        return pszCommand;
    }

    public static byte[] getPageModeSetVerStartingPosition(int mode,
            int distance) {
        if ((distance < 0) || (distance > 65535)) {
            distance = 0;
        }
        if ((mode < 0) || (mode > 1)) {
            mode = 0;
        }

        int nHigh = distance / 256;
        int nLow = distance % 256;

        byte[] pszCommand = new byte[4];
        Arrays.fill(pszCommand, (byte) 0);
        pszCommand[0] = 29;
        pszCommand[1] = 0;
        pszCommand[2] = ((byte) nLow);
        pszCommand[3] = ((byte) nHigh);

        if (mode == 0) {
            pszCommand[1] = 36;
        } else {
            pszCommand[1] = 92;
        }

        return pszCommand;
    }

    public static byte[] getPageModePrint() {
        return new byte[] { 27, 12 };
    }

    public static byte[] getPageModeClearBuffer() {
        return new byte[1];
    }

    public static byte[] systemFeedLine() {
        return new byte[] { 10 };
    }

    public static byte[] systemCutPaper(int cutMode, int feedDistance) {
        if (cutMode == 0 || cutMode == 1) {
            return new byte[] { 29, 86, (byte) cutMode };
        } else {
            return new byte[] { 29, 86, 66, (byte) feedDistance };
        }
    }

    public static byte[] systemSetMotionUnit(int horizontalUnit,
            int verticalUnit) {
        return new byte[] { 29, 80, (byte) horizontalUnit, (byte) verticalUnit };
    }

    public static byte[] textSelectFontType(int fontType) {
        fontType = (fontType >= 0 && fontType <= 3) ? fontType : 0;
        return new byte[] { 27, 77, (byte) fontType };
    }

    public static byte[] textStyleReverse(int Reverse) {
        Reverse = Reverse == 1 ? Reverse : 0;
        return new byte[] { 27, 66, (byte) Reverse };
    }

    public static byte[] textStyleSmooth(int smooth) {
        smooth = smooth == 1 ? smooth : 0;
        return new byte[] { 29, 98, (byte) smooth };
    }

    public static byte[] textStyleBold(int bold) {
        bold = bold == 1 ? bold : 0;
        return new byte[] { 27, 69, (byte) bold };
    }

    public static byte[] textStandardModeUpsideDown(int upsideDown) {
        upsideDown = upsideDown == 1 ? upsideDown : 0;
        return new byte[] { 27, 123, (byte) upsideDown };
    }

    public static byte[] textStyleUnderline(int underline, int fontType) {
        underline = (underline >= 0 && underline <= 2) ? underline : 0;
        fontType = (fontType >= 0 && fontType <= 3) ? fontType : 0;
        if (fontType != 3) {
            return new byte[] { 27, 45, (byte) underline };
        } else {
            return new byte[] { 28, 45, (byte) underline };
        }
    }

    public static byte[] textSelectFontMagnifyTimes(int horizontalTimes,
            int verticalTimes) {
        horizontalTimes = (horizontalTimes >= 1 && horizontalTimes <= 6) ? horizontalTimes
                : 0;
        verticalTimes = (verticalTimes >= 1 && verticalTimes <= 6) ? verticalTimes
                : 0;

        horizontalTimes--;
        horizontalTimes <<= 4;

        verticalTimes--;
        int nTimes = 0;
        nTimes |= horizontalTimes | verticalTimes;
        return new byte[] { 29, 33, (byte) nTimes };
    }

    public static byte[] imageRAMPrint(int imageId, int mode) {
        imageId = (imageId >= 0 && imageId <= 7) ? imageId : 0;
        mode = (mode >= 0 && mode <= 3) ? mode : 0;
        return new byte[] { 29, 35, (byte) imageId, 29, 47, (byte) mode };
    }

    public static byte[] printBitmap() {
        return new byte[] { 0x1C, 0x71, 0x00 };
    }

    public static byte[] barcodeSetModuleWidth(int moduleWidth) {
        moduleWidth = (moduleWidth >= 2 && moduleWidth <= 6) ? moduleWidth : 2;
        // 0x1D, 0x77, 0x02
        return new byte[] { 29, 119, (byte) moduleWidth };
    }

    public static byte[] barcodeSelectBarcodeHeight(int barcodeHeight) {
        barcodeHeight = (barcodeHeight >= 0 && barcodeHeight <= 255) ? barcodeHeight
                : 0;
        // 0x1D, 0x68, 0x00
        return new byte[] { 29, 104, (byte) barcodeHeight };
    }

    public static byte[] barcodeSelectHriFontType(int hriFontType) {
        hriFontType = hriFontType == 1 ? hriFontType : 0;
        // 0x1D, 0x66, 0x00
        return new byte[] { 29, 102, (byte) hriFontType };
    }

    public static byte[] barcodeSetHriFontPosition(int position) {
        position = (position >= 0 && position <= 3) ? position : 0;
        // 0x1D, 0x48, 0x02
        return new byte[] { 29, 72, (byte) position };
    }

    public static byte[] barcodePrint1Dimension(int barcodeType, int dataLength) {
        barcodeType = (barcodeType >= 65 && barcodeType <= 73) ? barcodeType
                : 65;
        dataLength = barcodeType == 65 ? 12 : dataLength > 0 ? dataLength : 0;
        // 0x1D, 0x6B, 0x41
        return new byte[] { 29, 107, (byte) barcodeType, (byte) dataLength };
    }
}
