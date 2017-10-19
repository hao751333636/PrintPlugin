package cn.koolcloud.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KOOLCLOUD_CLUB_PINPAD
 */

public class ClubPinPadInterface {

    static {
        /* Driver implementation, so file shall put under /system/lib */
        System.loadLibrary("koolcloudPos");
        System.loadLibrary("koolcloud_club_pinpad");
    }

    /**
     * key code reference definition, better to define in high-tier class public
     * final static int KEY_TYPE_DUKPT = 0; public final static int
     * KEY_TYPE_TDUKPT = 1; public final static int KEY_TYPE_MASTER = 2; public
     * final static int KEY_TYPE_PUBLIC = 3; public final static int
     * KEY_TYPE_FIX = 4; public final static int MAC_METHOD_X99 = 0; public
     * final static int MAC_METHOD_ECB = 1;
     */

    /* native methods as following */

    /**
     * @defgroup pinpad PINPAD_API
     * @{
     */

    /**
     * open the device
     * 
     * @return - value ==0 : success - value < 0 : error code, -1 : device
     *         doesn't exist, -10 : unknown error
     */
    public native static int open();

    /**
     * close the device
     * 
     * @return - value ==0 : success - value < 0 : error code, -1 : parameter
     *         mismatch, -2 : I/O error, -10 : unknown error
     */
    public native static int close();

    /**
     * show text on the device
     * 
     * @param[in] nLineIndex : line index, from top to down
     * @param[in] arryText : encoded string
     * @param[in] nTextLength : length of string
     * @param[in] nFlagSound : 0 : no voice prompt, 1 : voice prompt
     * @return - value < 0 : error code, maybe, your display string is too long!
     *         - value >= 0 : success (suggest 0)
     */
    public native static int showText(int nLineIndex, byte arryText[],
            int nTextLength, int nFlagSound);

    /**
     * select master key and user key (usually, one device has only one master
     * key, do not need consider multiple master keys)
     * 
     * @param[in] nKeyType : 0 : dukpt, 1: Tdukpt, 2 : master key, 3 : public
     *            key, 4 : fix key
     * @param[in] nMasterKeyID : master key ID , [0x00, ..., 0x09], make sense
     *            only when nKeyType is master-session pair,
     * @param[in] nUserKeyType : user key ID, [0x00, 0x01], 0: pin key, 1:
     *            encrypt key
     * @param[in] nAlgorith : 1 : 3DES, 0 : DES
     * @return - value < 0 : error code - value >= 0 : success (suggest 0)
     */
    public native static int selectKey(int nKeyType, int nMasterKeyID,
            int nUserKeyType, int nAlgorith);

    /**
     * encrypt password,use SHA1 or MD5 Alg
     * 
     * @param[out] arrayCipherText : cipher text
     * @param[in] nCipherTextLength : cipher text length,length >=16
     * @param[in] nEncryptKeyID : encrypt key id,id==0
     * @param[in] nCryptoType : 0x1:MD5,0x2:SHA1
     * @param[in] nPwdMaxlen : Input password max length,length<=12
     * @param[in] nPwdMinlen : Input password min length,length>=0,If NO PIN
     *            input,result is all ZERO
     * @param[in] nTimeout_MS : timeout waiting for user input in milliseconds,
     *            if it is less than zero, then wait forever,Ex:if the keypad
     *            wait 60s,nTimeout_MS = (60*1000/100) = 600
     * @param[in] nFlagSound : 0 : no voice prompt, 1 : voice prompt
     * @return - value < 0 : error code - value >= 0 : success (suggest 0)
     */
    public native static int encryptPassword(byte[] arrayCipherText,
            int nCipherTextLength, int nCryptoType, int nEncryptKeyID,
            int nPwdMaxlen, int nPwdMinlen, int nTimeout_MS, int nFlagSound);

    /**
     * update password key
     * 
     * @param[in] nEncryptKeyID : encrypt key id,[0, 99]
     * @param[in] arrayCipherEncryptKey : cipher encrypt key
     * @param[in] nCipherEncryptKeyLength : cipher encrypt key length,16bytes
     * @return - value < 0 : error code - value >= 0 : success (suggest 0)
     */
    public native static int updatePasswordKey(int nEncryptKeyID,
            byte[] arrayCipherEncryptKey, int nCipherEncryptKeyLength);
    /** }@ */
}
