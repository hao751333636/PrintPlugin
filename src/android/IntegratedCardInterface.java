package cn.koolcloud.jni;

public class IntegratedCardInterface
{

	static
	{
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("koolcloudPos");
		System.loadLibrary("koolcloud_integratedcard");		
	}
    private static final int IC_SLOT = 0;
    private static final int VCC_MODE = 1; //5V
	/* native methods as following */	
		
	/**
	 * initialize the IC card reader
	 * return  value != 0 : correct handle
     *               =< 0 : error code, -1 : device doesn't exist, -10 : unknown error
	 */
	public native static int open();
	
	/**
	 * close the IC card reader
	 * return value >= 0 : success (suggest 0)
     *	             < 0 : error code, - 1: parameter mismatch, - 2: I/O error, -10 : unknown error
	 */
	public native static int close();
	
    /**
	 * check IC card is in slot
	 * return value >= 0 : IC card is present (suggest 0)
     *	             < 0 : error code, - 1: IC card not present, - 2: I/O error, -10 : unknown error
	 */
    public native static int check(int slot);
    
    public static int check() {
        return check(IC_SLOT);
    }
    	/**
	 * attach the ic card before transmitting APDU command
	 * In this process, the ic card is activated and return ATR
	 * @param byteArrayATR : ATR buffer, if you set it null, you can not get the data.
	 * return value >= 0 : success, return length of ATR;
	 *               < 0 : error code
	 */
	public native static int attachCard(int vccMode, byte byteArrayATR[]);
	
	public static int attachCard(byte byteArrayATR[]) {
	    return attachCard(VCC_MODE, byteArrayATR);
	}
	/**
	 * detach the ic card. If you want to send APDU again, you should attach it.
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code
	 */
	public native static int detachCard();

    /**
	 * Transmitting APDU command
	 * @param send : send APDU command buffer
	 * @param recv : receive buffer, if you set it null, you can not get the data.
	 * return value >= 0 : success, return length of reveive data;
	 *               < 0 : error code
	 */
    public native static int transferAPDU(byte[] send, byte[] recv);

}
