package com.yunzo.cocmore.utils.base;

import java.util.zip.CRC32;

public class CRC32Util {

    /**
     * CRC32 加密算法10位
     * 
     * @param s
     * @return String
     */
    public static String getCRC32(String s) {
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        return Long.toString(crc32.getValue());
    }

}