package com.print.printer.hardware.printer;

public class OpenCashBox {

    public static boolean openCashBox() {
        PosPrinter printer = PosPrinter.getPrinter();
        try {
            if (!printer.openPrinter()) {
                return false;
            }
            return printer.openCashBox();
        } catch (Exception e) {
        } finally {
            printer.closePrinter();
        }
        return false;
    }

}
