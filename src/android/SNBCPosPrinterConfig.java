package com.print.printer.hardware.printer;

public class SNBCPosPrinterConfig {
    public static final int PRINT_AREA_WIDTH = 640;
    public static final int PRINT_AREA_HEIGHT = 800;
    public static final int MAX_CHAR_COUNT_800 = 42;
    public static final int MAX_CHAR_COUNT_580 = 32;
    public static final int MOTION_UNIT_X = 203;
    public static final int MOTION_UNIT_Y = 203;
    public static final byte PRINT_BITMAP_COMMAND[] = { 0x1C, 0x71, 0x00 };
    public static final int DEFAULT_FEED_PAPER_LINE_HEIGHT = 25;
}
