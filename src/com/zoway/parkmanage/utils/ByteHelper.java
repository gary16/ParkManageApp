package com.zoway.parkmanage.utils;

import java.io.UnsupportedEncodingException;

public class ByteHelper {

	/**
	 * 
	 * @Title: UInt16ToByte2
	 * 
	 * @Description: TODO(将UInt16转化为byte2)
	 * 
	 * 
	 * 
	 * @param Val
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static byte[] UInt16ToByte2(Integer Val) {

		byte[] bts = new byte[2];

		bts[0] = (byte) ((0xff00 & Val) >> 8);

		bts[1] = (byte) (0xff & Val);

		return bts;

	}

	/**
	 * 
	 * @Title: UInt16ToByte2
	 * 
	 * @Description: TODO(将UInt16转化为byte2)
	 * 
	 * 
	 * 
	 * @param i
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return:void
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static void UInt16ToByte2(Integer i, byte[] bts, int offset) {

		byte[] btsTemp = UInt16ToByte2(i);

		int index = 0;

		while (index < 2) {

			bts[index + offset] = btsTemp[index];

			index += 1;

		}

	}

	/**
	 * 
	 * @Title: Int16ToByte2
	 * 
	 * @Description: TODO(将Int16转化为byte2)
	 * 
	 * 
	 * 
	 * @param Val
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static byte[] Int16ToByte2(Short Val) {

		byte[] bts = new byte[2];

		bts[0] = (byte) ((Val >> 8) & 0xff);

		bts[1] = (byte) (Val & 0xff);

		return bts;

	}

	/**
	 * 
	 * @Title: Int16ToByte2
	 * 
	 * @Description: TODO(将Int16转化为byte2)
	 * 
	 * 
	 * 
	 * @param i
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return:void
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static void Int16ToByte2(Short i, byte[] bts, int offset) {

		byte[] btsTemp = Int16ToByte2(i);

		int index = 0;

		while (index < 2) {

			bts[index + offset] = btsTemp[index];

			index += 1;

		}

	}

	/**
	 * 
	 * @Title: UIntToByte4
	 * 
	 * @Description: TODO(将Uint32转化为byte4)
	 * 
	 * 
	 * 
	 * @param Val
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static byte[] UIntToByte4(Long Val) {

		byte[] bts = new byte[4];

		bts[0] = (byte) ((0xff000000L & Val) >> 24);

		bts[1] = (byte) ((0xff0000 & Val) >> 16);

		bts[2] = (byte) ((0xff00 & Val) >> 8);

		bts[3] = (byte) (0xff & Val);

		return bts;

	}

	/**
	 * 
	 * @Title: UIntToByte4
	 * 
	 * @Description: TODO(将Uint32转化为byte4)
	 * 
	 * 
	 * 
	 * @param i
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return:void
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static void UIntToByte4(Long i, byte[] bts, int offset) {

		byte[] btsTemp = UIntToByte4(i);

		int index = 0;

		while (index < 4) {

			bts[index + offset] = btsTemp[index];

			index += 1;

		}

	}

	/**
	 * 
	 * @Title: IntToByte4
	 * 
	 * @Description: TODO(将int32转化为byte4)
	 * 
	 * 
	 * 
	 * @param Val
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static byte[] IntToByte4(Integer Val) {

		byte[] bts = new byte[4];

		bts[0] = (byte) ((0xff000000 & Val) >> 24);

		bts[1] = (byte) ((0xff0000 & Val) >> 16);

		bts[2] = (byte) ((0xff00 & Val) >> 8);

		bts[3] = (byte) (0xff & Val);

		return bts;

	}

	/**
	 * 
	 * @Title: IntToByte4
	 * 
	 * @Description: TODO(将int32转化为byte4)
	 * 
	 * 
	 * 
	 * @param i
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return:void
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static void IntToByte4(Integer i, byte[] bts, int offset) {

		byte[] btsTemp = IntToByte4(i);

		int index = 0;

		while (index < 4) {

			bts[index + offset] = btsTemp[index];

			index += 1;

		}

	}

	/**
	 * 
	 * @Title: FloatToByte4
	 * 
	 * @Description: TODO(Float转byte4)
	 * 
	 * 
	 * 
	 * @param val
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static byte[] FloatToByte4(float val) {

		return IntToByte4(Float.floatToIntBits(val));

	}

	/**
	 * 
	 * @Title: FloatToByte4
	 * 
	 * @Description: TODO(Float转byte4)
	 * 
	 * 
	 * 
	 * @param val
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return:void
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-17
	 * 
	 * 
	 * 
	 *        Modification History:
	 * 
	 *        Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-17 wanjj v1.0.0 修改原因
	 */

	public static void FloatToByte4(float val, byte[] bts, int offset) {

		byte[] btsTemp = FloatToByte4(val);

		int index = 0;

		while (index < 4) {

			bts[index + offset] = btsTemp[index];

			index += 1;

		}

	}

	/**
	 * 
	 * @Title: Byte2ToInt16
	 * 
	 * @Description: TODO(Byte2转int16)
	 * 
	 * 
	 * 
	 * @param bt1
	 * 
	 * @param bt2
	 * 
	 * @return
	 * 
	 * @return:short
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static short Byte2ToInt16(byte bt1, byte bt2) {

		return (short) ((bt1 & 0xff) << 8 | bt2 & 0xff);

	}

	/**
	 * 
	 * @Title: Byte2ToInt16
	 * 
	 * @Description: TODO(Byte2转int16)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @return:short
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static short Byte2ToInt16(byte[] bts, int offset) {

		return Byte2ToInt16(bts[offset], bts[offset + 1]);

	}

	/**
	 * 
	 * @Title: Byte2ToUInt16
	 * 
	 * @Description: TODO(Byte2转uint16)
	 * 
	 * 
	 * 
	 * @param bt1
	 * 
	 * @param bt2
	 * 
	 * @return
	 * 
	 * @return:Integer
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static Integer Byte2ToUInt16(byte bt1, byte bt2) {

		return (int) ((bt1 & 0xff) << 8 | bt2 & 0xff);

	}

	/**
	 * 
	 * @Title: Byte2ToUInt16
	 * 
	 * @Description: TODO( Byte2转uint16)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @return:Integer
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static Integer Byte2ToUInt16(byte[] bts, int offset) {

		return Byte2ToUInt16(bts[offset], bts[offset + 1]);

	}

	/**
	 * 
	 * @Title: Byte4ToInt32
	 * 
	 * @Description: TODO(Byte4转int32)
	 * 
	 * 
	 * 
	 * @param bt1
	 * 
	 * @param bt2
	 * 
	 * @param bt3
	 * 
	 * @param bt4
	 * 
	 * @return
	 * 
	 * @return:Integer
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static Integer Byte4ToInt32(byte bt1, byte bt2, byte bt3, byte bt4) {

		return (bt1 & 0xff) << 24 | (bt2 & 0xff) << 16 | (bt3 & 0xff) << 8

		| bt4 & 0xff;

	}

	/**
	 * 
	 * @Title: Byte4ToInt32
	 * 
	 * @Description: TODO(Byte4转int32)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @return:Integer
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static Integer Byte4ToInt32(byte[] bts, int offset) {

		return Byte4ToInt32(bts[offset], bts[offset + 1], bts[offset + 2],

		bts[offset + 3]);

	}

	/**
	 * 
	 * @Title: Byte4ToUInt32
	 * 
	 * @Description: TODO(Byte4转uint32)
	 * 
	 * 
	 * 
	 * @param bt1
	 * 
	 * @param bt2
	 * 
	 * @param bt3
	 * 
	 * @param bt4
	 * 
	 * @return
	 * 
	 * @return:long
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static long Byte4ToUInt32(byte bt1, byte bt2, byte bt3, byte bt4) {

		return (long) Byte4ToInt32(bt1, bt2, bt3, bt4);

	}

	/**
	 * 
	 * @Title: Byte4ToUInt32
	 * 
	 * @Description: TODO(Byte4转uint32)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @return:long
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static long Byte4ToUInt32(byte[] bts, int offset) {

		return Byte4ToUInt32(bts[offset], bts[offset + 1], bts[offset + 2],

		bts[offset + 3]);

	}

	/**
	 * 
	 * @Title: Byte4ToFloat
	 * 
	 * @Description: TODO(Byte4转Float)
	 * 
	 * 
	 * 
	 * @param bt1
	 * 
	 * @param bt2
	 * 
	 * @param bt3
	 * 
	 * @param bt4
	 * 
	 * @return
	 * 
	 * @return:float
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static float Byte4ToFloat(byte bt1, byte bt2, byte bt3, byte bt4) {

		return Float.intBitsToFloat(Byte4ToInt32(bt1, bt2, bt3, bt4));

	}

	/**
	 * 
	 * @Title: Byte4ToFloat
	 * 
	 * @Description: TODO(Byte4转Float)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @return:float
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static float Byte4ToFloat(byte[] bts, int offset) {

		return Byte4ToFloat(bts[offset], bts[offset + 1], bts[offset + 2],

		bts[offset + 3]);

	}

	/**
	 * 
	 * @Title: StringToGBK
	 * 
	 * @Description: TODO(字符串转GBK编码)
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static byte[] StringToGBK(String str)

	throws UnsupportedEncodingException {

		return str.getBytes("GBK");

	}

	/**
	 * 
	 * @Title: FillSGBK
	 * 
	 * @Description: TODO(填充GBK编码)
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:int
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static int FillSGBK(String str, byte[] bts, int offset)

	throws UnsupportedEncodingException {

		return FillSGBK(str, bts, offset, 0);

	}

	/**
	 * 
	 * @Title: FillSGBK
	 * 
	 * @Description: TODO(填充GBK编码)
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @param length
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:int
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static int FillSGBK(String str, byte[] bts, int offset, int length)

	throws UnsupportedEncodingException {

		int len = 0;

		if (str != null) {

			byte[] tmp = StringToGBK(str);

			// 填充数据

			for (int i = 0; i < tmp.length; i++) {

				bts[offset + i] = tmp[i];

			}

			len = tmp.length;

		}

		// �?0

		for (int i = len; i < length; i++) {

			bts[offset + i] = 0;

		}

		return len > length ? len : length;

	}

	/**
	 * 
	 * @Title: GBKToString
	 * 
	 * @Description: TODO(GBK编码转字符串)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:String
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static String GBKToString(byte[] bts)

	throws UnsupportedEncodingException {

		return GBKToString(bts, 0);

	}

	/**
	 * 
	 * @Title: GBKToString
	 * 
	 * @Description: TODO(GBK编码转字符串)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:String
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static String GBKToString(byte[] bts, int offset)

	throws UnsupportedEncodingException {

		return SGBKToString(bts, offset, bts.length - offset);

	}

	/**
	 * 
	 * @Title: SGBKToString
	 * 
	 * @Description: TODO(GBK编码转字符串(定长字符�?))
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @param count
	 * 
	 * @return
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return:String
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static String SGBKToString(byte[] bts, int offset, int count)

	throws UnsupportedEncodingException {

		return new String(bts, offset, count, "GBK");

	}

	/**
	 * 
	 * @Title: HexStringToBytes
	 * 
	 * @Description: TODO(HEX字符串转BYTE数组)
	 * 
	 * 
	 * 
	 * @param hex
	 * 
	 * @return
	 * 
	 * @return:byte[]
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static byte[] HexStringToBytes(String hex) {

		byte[] bts = new byte[hex.length() / 2];

		for (int i = 0; i < bts.length; i++) {

			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),

			16);

		}

		return bts;

	}

	/**
	 * 
	 * @Title: BytesToHexString
	 * 
	 * @Description: TODO(BYTE数组转HEX字符�?)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @return
	 * 
	 * @return:String
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static String BytesToHexString(byte[] bts) {

		return BytesToHexString(bts, 0, bts.length);

	}

	/**
	 * 
	 * @Title: BytesToHexString
	 * 
	 * @Description: TODO(BYTE数组转HEX字符�?)
	 * 
	 * 
	 * 
	 * @param bts
	 * 
	 * @param offset
	 * 
	 * @param count
	 * 
	 * @return
	 * 
	 * @return:String
	 * 
	 * 
	 * 
	 * @version: v1.0.0
	 * 
	 * @author: WANJJ
	 * 
	 * @date: 2011-11-8
	 * 
	 * 
	 * 
	 *        Modification History: Date Author Version Description
	 * 
	 *        ---------------------------------------------------------*
	 * 
	 *        2011-11-8 wanjj v1.0.0 修改原因
	 */

	public static String BytesToHexString(byte[] bts, int offset, int count) {

		StringBuilder sb = new StringBuilder(bts.length * 2);

		for (int i = 0; i < count; i++) {
			sb.append(String.format("%02X", bts[i + offset]));

		}

		return sb.toString();

	}

	public static int BoolToByte(boolean bl) {

		return bl ? 1 : 0 << 0;

	}

}
