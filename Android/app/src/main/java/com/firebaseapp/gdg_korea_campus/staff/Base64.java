package com.firebaseapp.gdg_korea_campus.staff;

public class Base64 {
	private static final String tbl = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	
	public static byte[] encode(String string) {
		return encode(string, false);
	}
	
	public static byte[] encode(byte[] data) {
		return encode(data, false);
	}
	
	public static byte[] encode(String string, boolean doSplit) {
		//try to use the appropriate encoding string, but fallback to default locale
		try { return encode(string.getBytes("UTF-8"), doSplit);
		} catch (Exception ex) { }
		return encode(string.getBytes(), doSplit);
	}
	
	/**
	 * Base64 encode a byte array
	 * @param data The data to encode
	 * @param doSplit should we split the data into 76-character lines?
	 * @return base64-encoded byte array
	 * @author [John Comeau, Fred Sanchez, Cipher_nemo, Teresa and 9 others]<br/>
	 * Modified from <a href="http://www.wikihow.com/Encode-a-String-to-Base64-With-Java">WikiHow</a>
	 */
	public static byte[] encode(byte[] data, boolean doSplit) {
		String encoded = "", split = "";
		//determine how many padding bytes to add to the end
		int paddingCount = (3 - (data.length % 3)) % 3;
		//add any necessary padding to the input
		byte[] padded = new byte[data.length + paddingCount];
		System.arraycopy(data, 0, padded, 0, data.length);
		data = padded;
		//process 3 bytes at a time, output 4 bytes at a time
		for (int i = 0; i < data.length; i += 3) {
			int j = ((data[i] & 0xff) << 16) + ((data[i + 1] & 0xff) << 8) + (data[i + 2] & 0xff);
			encoded = encoded + tbl.charAt((j >> 18) & 0x3f) + tbl.charAt((j >> 12) & 0x3f) + tbl.charAt((j >> 6) & 0x3f) + tbl.charAt(j & 0x3f);
		}
		//replace encoded padding nulls with "="
		encoded = encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount);
		//split into multiple lines
		if (doSplit) {
			for (int i = 0; i < encoded.length(); i += 76) {
				split += encoded.substring(i, Math.min(encoded.length(), i + 76)) + "\r\n";
			}
		}
		//return the result, cleaning it up with a trim() if needed
		return (doSplit ? split.trim() : encoded).getBytes();
	}
	
	/**
	 * @param data The data to decode
	 * @return base64-decoded byte array
	 * @author [Unknown author]<br/>
	 * Modified from <a href="http://en.wikibooks.org/wiki/Algorithm_Implementation/Miscellaneous/Base64#Java_2">WikiBooks</a>
	 */
	public static byte[] decode(byte[] data) {
		String decoded = "";
		//replace any incoming padding with a zero pad (the 'A' character is zero)
		String pad = (data[data.length - 1] == '=' ? (data[data.length - 2] == '=' ? "AA" : "A") : "");
		data = (new String(data).substring(0, data.length - pad.length()) + pad).getBytes();
		//increment over the length of this encrypted string, four characters
		//at a time
		for (int i = 0; i < data.length; i += 4) {
			//each of these four characters represents a 6-bit index in the base64 characters list
			//which, when concatenated, will give the 24-bit number for the original 3 characters
			int j = (tbl.indexOf(data[i]) << 18) + (tbl.indexOf(data[i+1]) << 12) + (tbl.indexOf(data[i+2]) << 6) + tbl.indexOf(data[i+3]);
			//split the 24-bit number into the original three 8-bit (ASCII) characters
			decoded += "" + (char) ((j >>> 16) & 0xFF) + (char) ((j >>> 8) & 0xFF) + (char) (j & 0xFF);
		}
		//remove any zero pad that was added to make this a multiple of 24 bits
		return (decoded.substring(0, decoded.length() - pad.length())).getBytes();
	}
}