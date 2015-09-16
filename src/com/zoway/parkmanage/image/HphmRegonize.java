package com.zoway.parkmanage.image;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class HphmRegonize {

	private static HashMap<Integer, String> chrMap = new HashMap<Integer, String>();

	static {
		chrMap.put(0, "0");
		chrMap.put(1, "2");
		chrMap.put(2, "3");
		chrMap.put(3, "4");
		chrMap.put(4, "5");
		chrMap.put(5, "6");
		chrMap.put(6, "7");
		chrMap.put(7, "8");
		chrMap.put(8, "9");
		chrMap.put(9, "A");
		chrMap.put(10, "B");
		chrMap.put(11, "C");
		chrMap.put(12, "D");
		chrMap.put(13, "E");
		chrMap.put(14, "F");
		chrMap.put(15, "G");
		chrMap.put(16, "H");
		chrMap.put(17, "J");
		chrMap.put(18, "K");
		chrMap.put(19, "L");
		chrMap.put(20, "M");
		chrMap.put(21, "N");
		chrMap.put(22, "P");
		chrMap.put(23, "Q");
		chrMap.put(24, "R");
		chrMap.put(25, "S");
		chrMap.put(26, "T");
		chrMap.put(27, "U");
		chrMap.put(28, "V");
		chrMap.put(29, "W");
		chrMap.put(30, "X");
		chrMap.put(31, "Y");
		chrMap.put(32, "Z");
		chrMap.put(33, "1");
	}

	public String getSD() {
		String s = null;
		boolean flg = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (flg) {
			s = Environment.getExternalStorageDirectory().toString();
		}
		return s;
	}

	public String doOcr2(Bitmap bitmap) {
		String s = "?";
		try {
			int[] pix = new int[512];
			bitmap = Bitmap.createScaledBitmap(bitmap, 16, 32, true);
			bitmap.getPixels(pix, 0, 16, 0, 0, 16, 32);
			byte[] bSrc = new byte[512];

			for (int i = 0; i < 512; i++) {
				bSrc[i] = (byte) ((pix[i] == 0xffffffff) ? 1 : 0);
			}

			float[] all = new float[34];
			for (int i = 0; i < all.length; i++) {
				switch (i) {
				case 0:
					all[0] = this.imgMatchScale(bSrc, ChrTemplate.I_0);
					break;
				case 1:
					all[1] = this.imgMatchScale(bSrc, ChrTemplate.I_2);
					break;
				case 2:
					all[2] = this.imgMatchScale(bSrc, ChrTemplate.I_3);
					break;
				case 3:
					all[3] = this.imgMatchScale(bSrc, ChrTemplate.I_4);
					break;
				case 4:
					all[4] = this.imgMatchScale(bSrc, ChrTemplate.I_5);
					break;
				case 5:
					all[5] = this.imgMatchScale(bSrc, ChrTemplate.I_6);
					break;
				case 6:
					all[6] = this.imgMatchScale(bSrc, ChrTemplate.I_7);
					break;
				case 7:
					all[7] = this.imgMatchScale(bSrc, ChrTemplate.I_8);
					break;
				case 8:
					all[8] = this.imgMatchScale(bSrc, ChrTemplate.I_9);
					break;
				case 9:
					all[9] = this.imgMatchScale(bSrc, ChrTemplate.I_A);
					break;
				case 10:
					all[10] = this.imgMatchScale(bSrc, ChrTemplate.I_B);
					break;
				case 11:
					all[11] = this.imgMatchScale(bSrc, ChrTemplate.I_C);
					break;
				case 12:
					all[12] = this.imgMatchScale(bSrc, ChrTemplate.I_D);
					break;
				case 13:
					all[13] = this.imgMatchScale(bSrc, ChrTemplate.I_E);
					break;
				case 14:
					all[14] = this.imgMatchScale(bSrc, ChrTemplate.I_F);
					break;
				case 15:
					all[15] = this.imgMatchScale(bSrc, ChrTemplate.I_G);
					break;
				case 16:
					all[16] = this.imgMatchScale(bSrc, ChrTemplate.I_H);
					break;
				case 17:
					all[17] = this.imgMatchScale(bSrc, ChrTemplate.I_J);
					break;
				case 18:
					all[18] = this.imgMatchScale(bSrc, ChrTemplate.I_K);
					break;
				case 19:
					all[19] = this.imgMatchScale(bSrc, ChrTemplate.I_L);
					break;
				case 20:
					all[20] = this.imgMatchScale(bSrc, ChrTemplate.I_M);
					break;
				case 21:
					all[21] = this.imgMatchScale(bSrc, ChrTemplate.I_N);
					break;
				case 22:
					all[22] = this.imgMatchScale(bSrc, ChrTemplate.I_P);
					break;
				case 23:
					all[23] = this.imgMatchScale(bSrc, ChrTemplate.I_Q);
					break;
				case 24:
					all[24] = this.imgMatchScale(bSrc, ChrTemplate.I_R);
					break;
				case 25:
					all[25] = this.imgMatchScale(bSrc, ChrTemplate.I_S);
					break;
				case 26:
					all[26] = this.imgMatchScale(bSrc, ChrTemplate.I_T);
					break;
				case 27:
					all[27] = this.imgMatchScale(bSrc, ChrTemplate.I_U);
					break;
				case 28:
					all[28] = this.imgMatchScale(bSrc, ChrTemplate.I_V);
					break;
				case 29:
					all[29] = this.imgMatchScale(bSrc, ChrTemplate.I_W);
					break;
				case 30:
					all[30] = this.imgMatchScale(bSrc, ChrTemplate.I_X);
					break;
				case 31:
					all[31] = this.imgMatchScale(bSrc, ChrTemplate.I_Y);
					break;
				case 32:
					all[32] = this.imgMatchScale(bSrc, ChrTemplate.I_Z);
					break;
				case 33:
					all[33] = this.imgMatchScale(bSrc, ChrTemplate.I_1);
					break;
				}
			}
			float val = 0;
			int idx = 0;
			for (int i = 0; i < all.length; i++) {
				if (all[i] > val) {
					idx = i;
					val = all[i];
				}
			}
			s = chrMap.get(idx);
		} catch (Exception e) {
			Log.e("HphmRegonize", e.getMessage());
		}
		return s;
	}

	public float imgMatchScale(byte[] src, byte[] cmp) {
		if (src.length != 512 && cmp.length != 512) {
			return 0f;
		} else {
			int sum = 0;
			for (int i = 0; i < 512; i++) {
				sum += (src[i] ^ cmp[i]);
			}
			return (512f - sum) / 512f;
		}
	}

	// public String doOcr(Bitmap bitmap, float[] variArr) {
	// TessBaseAPI baseApi = new TessBaseAPI();
	// baseApi.init(getSD(), "eng");
	// // 必须加此行，tess-two要求BMP必须为此配置
	// bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	// baseApi.setImage(bitmap);
	// String text = baseApi.getUTF8Text();
	// baseApi.clear();
	// baseApi.end();
	// if (text.equals("?") || text.equals("") || text == null
	// || text.length() > 1) {
	// text = this.getSingleChr(variArr);
	// } else if (text.equals("o") || text.equals("O")) {
	// text = "0";
	// } else if (text.equals("i") || text.equals("I")) {
	// text = "1";
	// } else if (text.equals("@")) {
	// text = "Q";
	// } else if (text.equals("6")) {
	// String s1 = this.getSingleChr(variArr);
	// if (s1.equals("G")) {
	// text = "G";
	// }
	// } else if (text.equals("A")) {
	// String s1 = this.getSingleChr(variArr);
	// if (s1.equals("4")) {
	// text = "4";
	// }
	// } else if (text.equals("Z")) {
	// String s1 = this.getSingleChr(variArr);
	// if (s1.equals("2")) {
	// text = "2";
	// }
	// }
	// return text;
	// }

	public String getSingleLine(ArrayList<float[]> lneArr) {
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < lneArr.size(); i++) {
			String s1 = this.getSingleChr(lneArr.get(i));
			sb1.append(s1);
		}
		return sb1.toString();
	}

	public String getSingleChr(float[] chrArr) {
		String res = "?";
		if (chrArr.length != 10) {

		} else if (chrArr[0] == 0f && chrArr[1] == 0f) {
			res = "1";
		} else {
			startLbl: for (int i = 0; i < chrMap.size(); i++) {
				String cr = chrMap.get(i);
				ArrayList<float[]> ll = this.getChrTemple(cr);
				int matchSum = 0;
				for (int j = 0; j < ll.size(); j++) {
					float[] tplArr = ll.get(j);

					if (chrArr[1] > tplArr[2] && chrArr[1] < tplArr[3]) {
						matchSum++;
					}
					if (chrArr[2] > tplArr[4] && chrArr[2] < tplArr[5]) {
						matchSum++;
					}
					if (chrArr[3] > tplArr[6] && chrArr[3] < tplArr[7]) {
						matchSum++;
					}
					if (chrArr[4] > tplArr[8] && chrArr[4] < tplArr[9]) {
						matchSum++;
					}
					if (chrArr[5] > tplArr[10] && chrArr[5] < tplArr[11]) {
						matchSum++;
					}
					if (chrArr[6] > tplArr[12] && chrArr[6] < tplArr[13]) {
						matchSum++;
					}
					if (chrArr[7] > tplArr[14] && chrArr[7] < tplArr[15]) {
						matchSum++;
					}
					if (chrArr[8] > tplArr[16] && chrArr[8] < tplArr[17]) {
						matchSum++;
					}

					if (matchSum > 6) {
						res = cr;
						break startLbl;
					}
				}
			}
		}
		return res;
	}

	private ArrayList<float[]> getChrTemple(String chr) {
		ArrayList<float[]> li = new ArrayList<float[]>();
		float[] aa = new float[20];
		if (chr.equals("0")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_0[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_0[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("2")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_2[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_2[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("3")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_3[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_3[j] * 1.2f;
				}
			}
			li.add(aa);

		} else if (chr.equals("4")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_4[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_4[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("5")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_5[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_5[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("6")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_6[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_6[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("7")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_7[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_7[j] * 1.2f;
				}
			}
			li.add(aa);

		} else if (chr.equals("8")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_8[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_8[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("9")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_9[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_9[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("A")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_A[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_A[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("B")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_B[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_B[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("C")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_C[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_C[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("D")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_D[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_D[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("E")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_E[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_E[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("F")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_F[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_F[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("G")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_G[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_G[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("H")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_H[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_H[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("J")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_J[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_J[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("K")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_K[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_K[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("L")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_L[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_L[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("M")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_M[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_M[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("N")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_N[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_N[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("P")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_P[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_P[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("Q")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_Q[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_Q[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("R")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_R[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_R[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("S")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_S[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_S[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("T")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_T[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_T[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("U")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_U[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_U[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("V")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_V[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_V[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("W")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_W[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_W[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("X")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_X[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_X[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("Y")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_Y[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_Y[j] * 1.2f;
				}
			}
			li.add(aa);
		} else if (chr.equals("Z")) {
			for (int i = 0; i < 20; i++) {
				int j = (int) (i / 2);
				if (i % 2 == 0) {
					aa[i] = ChrTemplate.CHR_Z[j] * 0.8f;
				} else {
					aa[i] = ChrTemplate.CHR_Z[j] * 1.2f;
				}
			}
			li.add(aa);
		}
		return li;
	}
}
