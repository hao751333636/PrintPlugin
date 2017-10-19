package cn.koolcloud.jni;
import cn.koolcloud.jni.IntegratedCardInterface;

public class PSAMCardInterface
{
    public static final int PSAM1 = 0x01;
    public static final int PSAM2 = 0x02;

	public static int open()
	{
	    return IntegratedCardInterface.open();
	}

	public static int close()
	{
	    return IntegratedCardInterface.close();
	}

	public static int attachCard(int slot, byte byteArrayATR[])
	{
	    if(0 == IntegratedCardInterface.check(slot)) {
	        return IntegratedCardInterface.attachCard(byteArrayATR);
	    }
	    //Not Power on
	    return -1;
	}

	public static int detachCard()
	{
	    return IntegratedCardInterface.detachCard();
	}

    public static int transferAPDU(byte[] send, byte[] recv)
	{
	    return IntegratedCardInterface.transferAPDU(send, recv);
	}
}
