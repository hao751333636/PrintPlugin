package com.print.printer.hardware.printer;

public class PrinterStatus {
    public final boolean isCashDrawerOpen;
    public final boolean isOffLine;
    public final boolean isCoverOpen;
    public final boolean isFeeding;
    public final boolean isPrinterError;
    public final boolean isCutterError;
    public final boolean isPaperNearEnd;
    public final boolean isPaperEnd;

    public PrinterStatus(boolean cashDrawer, boolean offline,
            boolean coverOpen, boolean feeding, boolean printerError,
            boolean cutterError, boolean paperNearEnd, boolean paperEnd) {
        this.isCashDrawerOpen = cashDrawer;
        this.isOffLine = offline;
        this.isCoverOpen = coverOpen;
        this.isFeeding = feeding;
        this.isPrinterError = printerError;
        this.isCutterError = cutterError;
        this.isPaperNearEnd = paperNearEnd;
        this.isPaperEnd = paperEnd;
    }
}
