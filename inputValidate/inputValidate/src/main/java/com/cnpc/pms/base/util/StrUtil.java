package com.cnpc.pms.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The Class StrUtil.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @author lushu
 * @since 2010/10/29
 */
public final class StrUtil {

	/** Prefix for system property place-holders: "${". */
	public static final String PLACEHOLDER_PREFIX = "${";
	public static final int PLACEHOLDER_PREFIX_LENGTH = PLACEHOLDER_PREFIX.length();

	/** Suffix for system property place-holders: "}". */
	public static final String PLACEHOLDER_SUFFIX = "}";
	public static final int PLACEHOLDER_SUFFIX_LENGTH = PLACEHOLDER_SUFFIX.length();
	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(StrUtil.class);

	// #################### Utility ###############

	/**
	 * Generate uuid.
	 * 
	 * @return the string
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * Capitalize the first letter.
	 * 
	 * @param s
	 * @return the capitalized string
	 */
	public static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		if (s.length() > 1 && Character.isUpperCase(s.charAt(0))) {
			return s;
		}
		char chars[] = s.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	// #################### Splitter/Joiner ###############
	/**
	 * Split string into list, separate with comma ",".
	 * 
	 * @param str
	 *            the str
	 * @return the list
	 */
	public static List<String> strToList(String str) {
		return StrUtil.strToList(str, ",");
	}

	/**
	 * Convert string into List with specified token.
	 * 
	 * @param str
	 *            the str
	 * @param token
	 *            the token
	 * @return the list
	 */
	public static List<String> strToList(String str, String token) {
		List<String> list = Arrays.asList(str.split(token));
		return list;
	}

	public static String joinArray(Object[] array, String conjunction) {
		return org.apache.commons.lang.StringUtils.join(array, conjunction);
	}

	/**
	 * 
	 * @param s
	 * @param expr
	 * @return
	 */
	public static String[] splitToEnd(String s, String expr) {
		if (s.endsWith(expr)) {// s.matches(".*" + expr)
			String charm = new String(new byte[] { (byte) ~expr.getBytes()[0] });
			String[] result = (s + charm).split(expr);
			result[result.length - 1] = "";
			return result;
		} else {
			return s.split(expr);
		}
	}

	// #################### Encoder/Formatter/Wrapper ###############

	/**
	 * Change String into UTF8 encoded bytes.
	 * 
	 * @param string
	 *            the string
	 * @return the byte[]
	 */
//	public static byte[] stringToBytes(String string) {
//		return StringUtils.getBytesUtf8(string);
//	}
//
//	/**
//	 * Convert byte into UTF8 String.
//	 * 
//	 * @param bytes
//	 *            the bytes
//	 * @return the string
//	 */
//	public static String bytesToString(byte[] bytes) {
//		return StringUtils.newStringUtf8(bytes);
//	}

	/**
	 * Returns a String with special characters encoded with their HTML equivalents.
	 * 
	 * @param str
	 *            The String to encode.
	 * @return The HTML encoded string. If s is null, an empty string ("") is returned.
	 */
	public static String encodeHtml(String str) {
		if (str == null) {
			return "";
		}

		int len = str.length();
		StringBuffer strBuf = new StringBuffer(len + 50);

		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);

			switch (c) {
			case '&':
				strBuf.append("&amp;");
				break;
			case '<':
				strBuf.append("&lt;");
				break;
			case '>':
				strBuf.append("&gt;");
				break;
			case '"':
				strBuf.append("&quot;");
				break;
			// case '\n' :
			// strBuf.append( "<BR>" );
			// break;
			default:
				strBuf.append(c);
			}
		}

		return strBuf.toString();
	}

	/**
	 * URL Encoding from specified string.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String encodeURL(String str) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * Returns a string for Javascript.
	 * 
	 * @param str
	 *            The String to encode.
	 * @return The encoded string. If s is null, an empty string ("") is returned.
	 */
	public static String encodeJavascript(String str) {
		if (str == null) {
			return "";
		}
		int len = str.length();
		StringBuffer strBuf = new StringBuffer(len + 50);
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\\':
				strBuf.append("\\\\");
				break;
			case '\"':
				strBuf.append("\\\"");
				break;
			case '\'':
				strBuf.append("\\\'");
				break;
			case '%':
				strBuf.append("\\x" + Integer.toHexString('%'));
				break;
			case '\r':
				strBuf.append("\\r");
				break;
			case '\n':
				strBuf.append("\\n");
				break;
			default:
				strBuf.append(c);
			}
		}
		return strBuf.toString();
	}

	/**
	 * Format date.
	 * 
	 * @param dateFormat
	 *            the date format
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatDate(String dateFormat, Date date) {
		DateFormat dateformat = new SimpleDateFormat(dateFormat);
		return dateformat.format(date);
	}

	/**
	 * Format number.
	 * 
	 * @param numberFormat
	 *            the number format
	 * @param numberObject
	 *            the number object
	 * @return the string
	 */
	public static String formatNumber(String numberFormat, Object numberObject) {
		DecimalFormat decimalFormat = new DecimalFormat(numberFormat);
		return decimalFormat.format(numberObject);
	}

	/**
	 * Resolve ${...} place-holders in response output text, replacing them with corresponding i18n values.
	 * 
	 * @param text
	 *            the String to resolve
	 * @return the resolved String
	 * @see #PLACEHOLDER_PREFIX
	 * @see #PLACEHOLDER_SUFFIX
	 */
	public static String getI18N(String text) {
		StringBuffer buf = new StringBuffer(text);
		int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
		while (startIndex != -1) {
			int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX_LENGTH);
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX_LENGTH, endIndex);
				int nextIndex = endIndex + PLACEHOLDER_SUFFIX_LENGTH;
				try {
					String propVal = "";//SpringHelper.getMessage(placeholder);
					log.debug("SpringHelper.getMessage [{}] with: {}", placeholder, propVal);
					if (propVal != null) {
						buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX_LENGTH, propVal);
						nextIndex = startIndex + propVal.length();
					} else {
						log.error("Could not resolve placeholder '{}' as i18n resources.", placeholder);
					}
				} catch (Throwable ex) {
					log.error("Could not resolve placeholder '{}' as i18n resource: {}", placeholder, ex);
				}
				startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
			} else {
				startIndex = -1;
			}
		}
		return buf.toString();
	}

	// ######################## JSON ##############################
	/**
	 * Serialize json data into the specified type's Object.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param json
	 *            the json
	 * @param classOfT
	 *            the class of t
	 * @return the t
	 * @throws UtilityException
	 *             the UtilityException
	 */
	public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Fail to get Object[type:{}] from JSON: {}", clazz.getCanonicalName(), json);
			e.printStackTrace();
			throw new Exception("Fail to get Object as " + clazz.getSimpleName() + " from String: " + json, e);
		}
	}

	/**
	 * Deserialize a object into json data without type specification.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toJson(Object src) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(src);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Deserialize a object into json data with specified type.
	 * 
	 * @param src
	 *            the src
	 * @param typeOfSrc
	 *            the type of src
	 * @return the string
	 */
//	public static String toJson(Object src, Type typeOfSrc) {
//		Gson gson = new Gson();
//		return gson.toJson(src, typeOfSrc);
//	}

	/**
	 * toJsonWithGson
	 * 
	 * @param src
	 *            the src
	 * @return String
	 */
//	public static String toJsonWithGson(Object src) {
//		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//		return gson.toJson(src);
//	}

	// ##################### Serialize/Deserialize Methods #############

	/**
	 * Serialize object.
	 * 
	 * @param o
	 *            Object to be serialized
	 * @return Hex string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String serializeObject(Serializable o) throws IOException {

		if (o == null) {
			return null;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(o);
		out.close();

		// Get the bytes of the serialized object
		byte[] buf = bos.toByteArray();

		return encodeHex(buf);
	}

	/**
	 * Encode bytes into hex formatted string.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return the string
	 */
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16) {
				buf.append("0");
			}
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

	/**
	 * Deserialize object.
	 * 
	 * @param hexStr
	 *            the hex str
	 * @return object deserialized
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static Serializable deserializeObject(String hexStr) throws IOException, ClassNotFoundException {
		byte[] bytes = decodeHex(hexStr);

		// Deserialize from a byte array
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
		Serializable obj = (Serializable) in.readObject();
		in.close();

		return obj;
	}

	/**
	 * Decode Hex String.
	 * 
	 * @param hex
	 *            the hex
	 * @return the byte[]
	 */
	public static final byte[] decodeHex(String hex) {

		char[] chars = hex.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}

		return bytes;
	}

	/**
	 * Change hex into dec format.
	 * 1-9,a-f -> 1-15
	 * 
	 * @param ch
	 *            the ch
	 * @return the byte
	 */
	private static final byte hexCharToByte(char ch) {

		byte result = 0;
		if (ch >= 49 && ch <= 57) {
			result = (byte) (ch - 48);
		} else if (ch >= 97 && ch <= 102) {
			result = (byte) (ch - 87);
		}
		return result;
	}

	// ##################### Deprecated Methods #############
	/**
	 * Composite splitted string by specified encoding.
	 * 
	 * @param splitString
	 *            the split string
	 * @param encoding
	 *            the encoding
	 * @return encoding string.
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	@Deprecated
	public static String compositeSplitStringToEncodingString(String splitString, String encoding)
			throws UnsupportedEncodingException {
		if (splitString != null && splitString.length() > 0) {
			char[] resChars = splitString.toCharArray();
			byte[] resBytes = new byte[resChars.length];
			for (int i = 0; i < resChars.length; i++) {
				resBytes[i] = (byte) resChars[i];
			}
			return new String(resBytes, encoding);
		}
		return null;
	}

	/**
	 * Convert encoding of inputString into another one.
	 * 
	 * @param inputString
	 *            the input string
	 * @param fromEncoding
	 *            the from encoding
	 * @param toEncoding
	 *            the to encoding
	 * @return the string
	 */
	@Deprecated
	public static String encodingTransfer(String inputString, String fromEncoding, String toEncoding) {

		if (inputString == null || inputString.length() == 0) {
			return inputString;
		}
		try {
			return new String(inputString.getBytes(fromEncoding), toEncoding);
		} catch (Exception e) {
			return inputString;
		}
	}

	@Deprecated
	public static String Iso2Utf(String original) {
		if (isEmpty(original)) {
			return original;
		}
		String output = null;
		try {
			output = new String(original.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Fail to transform String from ISO8859 to UTF-8: {}", original);
		}
		return output;
	}

	/**
	 * Replace a token with the input string.
	 * 
	 * @param str
	 *            the original string
	 * @param token
	 *            the substring needed to be replaced
	 * @param sub
	 *            the new string which replaces token
	 * @return the string
	 */
	@Deprecated
	public static String replace(String str, String token, String sub) {
		// if any of the paramter values is null. return the original string
		if ((str == null) || (token == null) || (sub == null) || (str.length() == 0) || (token.length() == 0)
				|| (sub.length() == 0)) {
			return str;
		}

		if (str.indexOf(token) == -1) {
			return str;
		}

		int len = token.length();

		// temporary buffer to keep the returned string
		StringBuffer buf = new StringBuffer();

		int lastPos = 0;
		int curPos;
		while ((curPos = str.indexOf(token, lastPos)) != -1) {
			buf.append(str.substring(lastPos, curPos));
			buf.append(sub);
			lastPos = curPos + len;
		}

		if (lastPos < str.length()) {
			buf.append(str.substring(lastPos));
		}

		return buf.toString();
	}

}
