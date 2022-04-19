package com.zjz.utils;

import com.mysql.jdbc.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Handle {

	@Autowired
    private ReloadableResourceBundleMessageSource messageSource;
	
	public static MessageSource ms;
	
	@PostConstruct
    public void init(){
        ms = this.messageSource;
	}
	
	public static String getLanguageName(String key,String lankey,String defaultKey) {
		String message = "";
		
		try {
			if(key == null) {
				return defaultKey;
			}
			Locale locale;
			if(lankey == null || lankey.split("_").length < 2) {
				locale = LocaleContextHolder.getLocale();
			}else {
				String[] lan = lankey.split("_");
				locale = new Locale(lan[0],lan[1]);
			}
			message = ms.getMessage(key, null, locale);
		} catch (Exception e) {
			return defaultKey;
		}
		return (message == null || message.equals("")) ? defaultKey : message;
	}
	
	public static String getLanguageName(String key,String lankey) {
		String message = "";
		try {
			if(key == null) {
				return "";
			}
			Locale locale;
			if(lankey == null || lankey.split("_").length < 2) {
				locale = LocaleContextHolder.getLocale();
			}else {
				String[] lan = lankey.split("_");
				locale = new Locale(lan[0],lan[1]);
			}
			message = ms.getMessage(key, null, locale);
		} catch (NoSuchMessageException e) {
			return key;
		}
		return  (message == null || message.equals("")) ? key : message;
	}
	
	// 鍒嗗壊瀛楃涓�
	public static String splitString(String source, String split, String lankey) {
		String languageString = "";
		if (source != null && !source.equals("")) {
			if (source.indexOf("*") != -1) {
				String[] str = source.split(split);
				for (String string : str) {
					if (languageString.equals("")) {
						languageString = getLanguageName(string, lankey);
					} else {
						languageString += "*" + getLanguageName(string, lankey);
					}
				}
				return languageString;
			} else {
				return languageString = getLanguageName(source, lankey);
			}
		}
		return null;
	}

	public static String il8nStringGet(String il8nStr, String lankey) {
		if (il8nStr != null) {
			List<String> il8nLs = new ArrayList<String>();
			Pattern pattern = Pattern.compile("(?<=\\{)(.*?)(?=\\})");
			Matcher matcher = pattern.matcher(il8nStr);
			while (matcher.find()) {
				il8nLs.add(matcher.group());
			}

			if (il8nLs != null && il8nLs.size() > 0) {
				for (String l : il8nLs) {
					if (!l.trim().equals("")) {
						il8nStr = il8nStr.replace("{" + l + "}", getLanguageName(l, lankey,null) == null ? "" : getLanguageName(l, lankey,null));
					}
				}
			}
		}
		return il8nStr;
	}
	
	public static String getFileSuffix(String fileName) {
		if (fileName != null) {
			int index = fileName.lastIndexOf(".");
			if (index != -1) {
				return fileName.substring(index, fileName.length());
			}
		}

		return "";
	}

	public static Boolean isUUID(String uuid) {
		if (isNull(uuid)) {
			return false;
		}
		String reg = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
		String reg2 = "^[0-9]{16}$";
		return uuid.matches(reg) || uuid.matches(reg2);
	}

	public static Boolean isKeyID(String id) {
		if (isNull(id)) {
			return false;
		}
		// 14067 93422 37112 5
		String reg = "^[0-9]{16}$";
		return id.matches(reg);
	}

	/**
	 * @鏂规硶瑾槑:鍒犻櫎List涓噸澶嶅厓绱�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/12/4 涓嬪崍2:50:08
	 * @copyright e-print
	 * @param list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	/**
	 * @鏂规硶瑾槑: 鍒犻櫎List涓噸澶嶅厓绱狅紝淇濇寔椤哄簭
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/12/4 涓嬪崍2:51:13
	 * @copyright e-print
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		list.clear();
		list.addAll(newList);
		return list;
	}

	/**
	 * @鏂规硶瑾槑: 鑾峰彇鏈�澶ф帴杩戞暟锛屽鏋滈泦鍚堜腑娌℃湁鍊煎ぇ浜巒um锛屽垯杩斿洖闆嗗悎涓渶澶х殑鍊�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/11/30 涓嬪崍3:22:51
	 * @copyright e-print
	 * @param list
	 * @param num
	 * @return
	 */
	public static Integer maxNearList(List<Integer> list, int num) {
		List<Integer> _list = new ArrayList<Integer>();
		for (Integer integer : list) {
			if (integer != null && integer >= num) {
				_list.add(integer);
			}
		}
		if (_list.size() > 0) {
			return Collections.min(_list);
		}
		return Collections.max(list);
	}

	/**
	 * @鏂规硶瑾槑: 鑾峰彇鏈�灏忔帴杩戞暟锛屽鏋滈泦鍚堜腑娌℃湁鍊煎皬浜巒um锛屽垯杩斿洖闆嗗悎涓渶灏忕殑鍊�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/11/30 涓嬪崍3:23:49
	 * @copyright e-print
	 * @param list
	 * @param num
	 * @return
	 */
	public static Integer minNearList(List<Integer> list, int num) {
		List<Integer> _list = new ArrayList<Integer>();
		for (Integer integer : list) {
			if (integer != null && integer <= num) {
				_list.add(integer);
			}
		}
		if (_list.size() > 0) {
			return Collections.max(_list);
		}
		return Collections.min(list);
	}

	/**
	 * @鏂规硶瑾槑: 鑾峰彇鏈�澶ф帴杩戞暟锛屽鏋滈泦鍚堜腑娌℃湁鍊煎ぇ浜巒um锛屽垯杩斿洖闆嗗悎涓渶澶х殑鍊�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/11/30 涓嬪崍3:22:51
	 * @copyright e-print
	 * @param list
	 * @param num
	 * @return
	 */
	public static Float maxNearList2(List<Float> list, float num) {
		List<Float> _list = new ArrayList<Float>();
		for (Float integer : list) {
			if (integer != null && integer >= num) {
				_list.add(integer);
			}
		}
		if (_list.size() > 0) {
			return Collections.min(_list);
		}
		return Collections.max(list);
	}

	/**
	 * @鏂规硶瑾槑: 鑾峰彇鏈�灏忔帴杩戞暟锛屽鏋滈泦鍚堜腑娌℃湁鍊煎皬浜巒um锛屽垯杩斿洖闆嗗悎涓渶灏忕殑鍊�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/11/30 涓嬪崍3:23:49
	 * @copyright e-print
	 * @param list
	 * @param num
	 * @return
	 */
	public static Float minNearList2(List<Float> list, float num) {
		List<Float> _list = new ArrayList<Float>();
		for (Float integer : list) {
			if (integer != null && integer <= num) {
				_list.add(integer);
			}
		}
		if (_list.size() > 0) {
			return Collections.max(_list);
		}
		return Collections.min(list);
	}

	/**
	 * 绲变竴path璺緫"\"
	 * 
	 * @param path
	 * @return
	 */
	public static String passpath2(String path) {
		boolean flag = true;
		path = passpath(path);
		while (flag) {
			path = path.replace("/", "\\");
			if (path.indexOf("/") == -1 && path.indexOf("//") == -1) {
				flag = false;
			}
		}
		return path;
	}

	/**
	 * 绲变竴path璺緫"/"
	 * 
	 * @param path
	 * @return
	 */
	public static String passpath(String path) {
		boolean flag = true;
		while (flag) {
			path = path.replace("\t", "/t");
			path = path.replace("\\", "/");
			path = path.replace("//", "/");
			if (path.indexOf("\\") == -1 && path.indexOf("//") == -1) {
				flag = false;
			}
		}
		return path;
	}

	/**
	 * 绲变竴鏂囦欢璺緫锛屽彧鍙敤鏂兼枃浠惰矾寰戯紝鍏朵粬璺緫涓嶉仼鐢�
	 * 
	 * @param path
	 * @return
	 */
	public static String formatPath(String path) {
		// 灏囨墍鏈�"\"鏇挎彌鐐�"/"
		path = path.replaceAll("\\\\", "/");
		// 灏囨墍鏈夐噸瑜�"/"鏇挎彌鐐哄柈"/"
		path = path.replaceAll("(\\/{2,})+", "/");
		// 鍘绘帀璺緫鏈�寰岀殑"/"
		if (path.endsWith("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
		}

		return path;
	}


	public static long getLong(String str) {
		str = trim(str);
		try {
			return Long.valueOf(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long getLong2(Integer num) {
		try {
			return Long.valueOf(num);
		} catch (Exception e) {
			return 0;
		}
	}


	/** 鐐虹┖鏅傝繑鍥瀟rue */
	public static boolean isNull(String arg) {
		if (arg == null)
			return true;
		if ("".equals(arg))
			return true;
		if ("".equals(arg.trim()))
			return true;
		if ("null".equals(arg.trim().toLowerCase()))
			return true;
		return false;
	}

	/** 鍘荤┖鏍�. */
	public static String trim(String arg) {
		return isNull(arg) ? null : arg.trim();
	}

	public static String getString2(Object obj) {
		if (obj != null && !obj.equals("") && !obj.equals("null")) {
			return obj.toString().trim();
		}
		return "";
	}

	public static String getString(String str) {
		if (str != null && !str.equals("") && !str.equals("null")) {
			return str.trim();
		}
		return "";
	}

	public static String getString3(String str) {
		if (str != null && !str.equals("") && !str.equals("null")) {
			return str.trim();
		}
		return "0";
	}

	public static String getString1(String str) {
		if (str != null && !str.equals("") && !str.equals("null")) {
			return str.trim();
		}
		return null;
	}

	public static String getInteger1(int id) {
		try {
			if (id == 0) {
				return "";
			} else {
				return id + "";
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static String getFloat1(float value) {
		try {
			if (value == 0f) {
				return "";
			} else {
				return value + "";
			}
		} catch (Exception e) {
		}
		return "";
	}

	public static String getUploadFileName(String name) {
		try {
			Date date = new Date();
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyyMMddHHmmss");
			String str = (Math.random() * 10000) + "";
			str = str.substring(0, 4);
			name = name.substring(name.lastIndexOf("."), name.length());
			return simpleDF.format(date).toString() + str + name;
		} catch (Exception e) {
		}

		return name;
	}


	public static List getElementvalueids(String strs) {
		if (strs != null && !strs.trim().equals("")) {
			List list = null;
			try {
				list = new ArrayList();
				String str[] = Handle.getArrString(strs, "_");
				for (int i = 0; i < str.length; i++) {
					if (str[i] != null && !str[i].trim().equals("")) {
						list.add(str[i]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		return null;
	}

	public static String filterstring(String source, String split) {
		String[] strs = source.split(split);
		Set<String> set = new HashSet<String>();
		if (strs != null) {
			for (int i = 0; i < strs.length; i++) {
				if (!"".equals(strs[i])) {
					set.add(strs[i]);
				}
			}
		}
		StringBuffer dest = new StringBuffer();
		for (String str : set) {
			dest.append(str).append(",");
		}
		return dest.toString();
	}


	public static String Replace(String s, String s1, String s2) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = s2.indexOf(s);
		if (i == -1) {
			return s2;
		}
		stringbuffer.append(s2.substring(0, i) + s1);
		if (i + s.length() < s2.length()) {
			stringbuffer.append(Replace(s, s1, s2.substring(i + s.length(), s2.length())));
		}
		return stringbuffer.toString();
	}

	public static String RetrunTX(String s) {
		return Replace("\"", "\\\"", Replace("'", "\\'", Replace("\\", "\\\\", s)));
	}

	public static String replaceLign(String str, String key) {
		return Replace(key.toLowerCase(), "<font color=red>" + key + "</font>", str.toLowerCase());
	}

	public static boolean checkObjectIsNull(Object object) {
		if (object != null && !object.toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	// 妫�鏌ュ瓧绗︿覆鏄惁鏄暣褰�
	public static boolean checkInt(String str) {
		try {
			Integer.parseInt(str.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 妫�鏌ュ瓧绗︿覆鏄惁鏄暱鏁村舰
	public static boolean checkLong(String str) {
		try {
			Long.parseLong(str.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Float getFloat(String str, Float defaultValue) {
		try {
			return Float.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float getfloat(String str) {
		try {

			DecimalFormat myformat = new DecimalFormat("######0.00");
			Number strss = myformat.parse(str.trim());
			// System.out.println(strss.floatValue());

			return strss.floatValue();
			// return Float.valueOf();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0f;
	}

	public static Float getfloat2(float str) {
		try {

			DecimalFormat myformat = new DecimalFormat("######0.00");
			Number strss = myformat.parse(str + "");
			// System.out.println(strss.floatValue());

			return strss.floatValue();
			// return Float.valueOf();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1f;
	}

	public static double getfloat3(double d) {
		try {
			BigDecimal a = new BigDecimal(d);
			BigDecimal b = a.setScale(2, 2);
			return b.doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1d;
	}

	public static float getfloat4(double d) {
		try {
			// BigDecimal a = new BigDecimal(d);
			// BigDecimal b = a.setScale(2, 2);
			// return b.floatValue();
			return new BigDecimal(d).floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1f;
	}

	public static float getfloat5(float d) {
		try {
			BigDecimal a = new BigDecimal(d);
			BigDecimal b = a.setScale(2, 2);
			return b.floatValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1f;
	}

	public static String getfloatstr(float num) {
		double d = div(num, 1, 2);
		return d + "";
	}

	/**
	 * 鎻愪緵锛堢浉瀵癸級绮剧‘鐨勯櫎娉曡繍绠椼�傚綋鍙戠敓闄や笉灏界殑鎯呭喌鏃讹紝鐢眘cale鍙傛暟鎸� 瀹氱簿搴︼紝浠ュ悗鐨勬暟瀛楀洓鑸嶄簲鍏ャ��
	 * 
	 * @param v1
	 *            琚櫎鏁�
	 * @param v2
	 *            闄ゆ暟
	 * @param scale
	 *            琛ㄧず琛ㄧず闇�瑕佺簿纭埌灏忔暟鐐逛互鍚庡嚑浣嶃��
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 鎻愪緵锛堢浉瀵癸級绮剧‘鐨勯櫎娉曡繍绠椼�傚綋鍙戠敓闄や笉灏界殑鎯呭喌鏃讹紝鐢眘cale鍙傛暟鎸� 瀹氱簿搴︼紝浠ュ悗鐨勬暟瀛楀洓鑸嶄簲鍏ャ��
	 * 
	 * @param v1
	 *            琚櫎鏁�
	 * @param v2
	 *            闄ゆ暟
	 * @param scale
	 *            琛ㄧず琛ㄧず闇�瑕佺簿纭埌灏忔暟鐐逛互鍚庡嚑浣嶃��
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */

	public static Float div(float v1, float v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠椼��
	 * 
	 * @param v1
	 *            琚姞鏁�
	 * @param v2
	 *            鍔犳暟
	 * @return 涓や釜鍙傛暟鐨勫拰
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠椼��
	 * 
	 * @param v1
	 *            琚姞鏁�
	 * @param v2
	 *            鍔犳暟
	 * @return 涓や釜鍙傛暟鐨勫拰
	 */
	public static float add(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫姞娉曡繍绠椼��
	 * 
	 * @param v1
	 *            琚姞鏁�
	 * @param v2
	 *            鍔犳暟
	 * @return 涓や釜鍙傛暟鐨勫拰
	 */
	public static double bigadd(double v1, BigDecimal v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = v2;
		return b1.add(b2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠椼�� V1- V2
	 * 
	 * @param v1
	 *            鍑忔暟
	 * @param v2
	 *            琚噺鏁�
	 * @return 涓や釜鍙傛暟鐨勫樊 V1- V2
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫噺娉曡繍绠椼�� V1- V2
	 * 
	 * @param v1
	 *            鍑忔暟
	 * @param v2
	 *            琚噺鏁�
	 * @return 涓や釜鍙傛暟鐨勫樊 V1- V2
	 */
	public static float sub(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).floatValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠椼��
	 * 
	 * @param v1
	 *            涔樻暟
	 * @param v2
	 *            琚箻鏁�
	 * @return 涓や釜鍙傛暟鐨勭Н
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠椼��
	 * 
	 * @param v1
	 *            琚箻鏁�
	 * @param v2
	 *            涔樻暟
	 * @return 涓や釜鍙傛暟鐨勭Н
	 */
	public static int mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).intValue();
	}

	/**
	 * 鎻愪緵绮剧‘鐨勪箻娉曡繍绠椼��
	 * 
	 * @param v1
	 *            琚箻鏁�
	 * @param v2
	 *            涔樻暟
	 * @return 涓や釜鍙傛暟鐨勭Н
	 */
	public static float mul(float v1, float v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).floatValue();
	}

	/**
	 * 鎻愪緵锛堢浉瀵癸級绮剧‘鐨勯櫎娉曡繍绠楋紝褰撳彂鐢熼櫎涓嶅敖鐨勬儏鍐垫椂锛岀簿纭埌 灏忔暟鐐逛互鍚�10浣嶏紝浠ュ悗鐨勬暟瀛楀洓鑸嶄簲鍏ャ��
	 * 
	 * @param v1
	 *            琚櫎鏁�
	 * @param v2
	 *            闄ゆ暟
	 * @return 涓や釜鍙傛暟鐨勫晢
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, 10);
	}

	/**
	 * 鎻愪緵绮剧‘鐨勫皬鏁颁綅鍥涜垗浜斿叆澶勭悊銆�
	 * 
	 * @param v
	 *            闇�瑕佸洓鑸嶄簲鍏ョ殑鏁板瓧
	 * @param scale
	 *            灏忔暟鐐瑰悗淇濈暀鍑犱綅
	 * @return 鍥涜垗浜斿叆鍚庣殑缁撴灉
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 瑷堢畻鍦ㄦ寚瀹氱殑涓�鍊嬫檪闁撳収瀹屾垚涓�鍊嬫寚瀹氱殑鏁搁噺瑕佽姳璨荤殑鏅傞枔/瀵﹂殯瑕佸嵃鍒风殑鏁搁噺
	 * 
	 * @param costTime
	 *            鎸囧畾鐨勬檪闁�
	 * @param number
	 *            鎸囧畾鐨勬暩閲�
	 * @param printnumber
	 *            瀵﹂殯瑕佸嵃鍒风殑鏁搁噺
	 * @return
	 */

	// 灏囧垎閻樿綁鍖栫偤灏忔檪
	public static String minuteTOHour(int minute) {
		String hour = null;
		if (minute > 0) {
			if (minute > 60) {
				hour = (minute / 60) + "灏忔檪" + (minute % 60) + "鍒�";
				if ((minute / 60) > 24) {
					hour = (minute / 60) / 24 + "澶�" + (minute / 60) % 24 + "灏忔檪" + (minute % 60) + "鍒�";
				}
			} else {
				hour = minute + "鍒�";
			}
		} else {
			hour = "0鍒�";
		}

		return hour;
	}

	// 浠庡皬鍒板ぇ鎺掑簭 int
	public static int[] sort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					int x = arr[i];
					arr[i] = arr[j];
					arr[j] = x;
				}
			}
		}
		return arr;
	}

	// 鏅傞枔鐨勫姞娓� +2 琛ㄧず鍔�2澶╋紝-2琛ㄧず娓�2澶�
	public static String add_minus(Date date, int how) {
		if (date != null)
			return DateToStr(new Date(date.getTime() + how * 24 * 60 * 60 * 1000));
		return null;
	}

	// 鏅傞枔鐨勫姞娓� +2 琛ㄧず鍔�2澶╋紝-2琛ㄧず娓�2澶�
	public static Date add_Date(Date date, int how) {
		if (date != null)
			return new Date(date.getTime() + how * 24 * 60 * 60 * 1000);
		return null;
	}

	/**
	 * 鍒ゆ柇鏄惁鏄妭鍋囨棩
	 * 
	 * @param deliveryDate
	 * @return
	 */
	// public static Date compareDate(Date deliveryDate) {
	// List<BaseHolidayDetail> lbhdlist =
	// FMX.BASEHOLIDAYDETAIL_MAP.get("baseHolidayKey");
	// if (lbhdlist != null && deliveryDate != null) {
	// for (BaseHolidayDetail baseHolidayDetail : lbhdlist) {
	// String holiday = DateToStr(baseHolidayDetail.getHolidaydate());
	// String ddate = DateToStr(deliveryDate);
	// if (holiday.equals(ddate)) {
	// long dvalue = baseHolidayDetail.getHolidaydate().getTime() -
	// deliveryDate.getTime();
	// if (dvalue > 0) {
	// return getDate(new Date(baseHolidayDetail.getHolidaydate().getTime() +
	// dvalue));
	// } else {
	// return getDate(new Date(baseHolidayDetail.getHolidaydate().getTime() -
	// dvalue));
	// }
	// }
	// // if(baseHolidayDetail.getHolidaydate().getTime()-deliveryDate.getTime()
	// // >= 0 &&
	// // baseHolidayDetail.getHolidaydate().getTime()-deliveryDate.getTime()
	// // <= (24 * 60 * 60 * 1000)){
	// // return getDate(new
	// //
	// Date(baseHolidayDetail.getHolidaydate().getTime()+(baseHolidayDetail.getHolidaydate().getTime()-deliveryDate.getTime())));
	// // }
	// }
	// }
	// return null;
	// }

	// 浠庡皬鍒板ぇ鎺掑簭 long
	public static long[] sort(long[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					long x = arr[i];
					arr[i] = arr[j];
					arr[j] = x;
				}
			}
		}
		return arr;
	}

	/*
	 * costTime鏄及瑷堟檪闁撳収 number鏄及瑷堟檪闁撳収瀹屾垚鐨勬暩閲� machinnumber 瀵﹂殯鍗板埛鐨勬暩閲�
	 */

	public static int costTime(int costTime, int number, int machinnumber) {
		int ct = 0;
		double b = Handle.div(costTime, number, 10);
		double c = b * machinnumber;
		double h = Handle.div(c, 1, 0); // 灏囬渶瑕佺敤鐨勬檪闁撳洓鑸嶄簲鍏�
		ct = (int) h;
		return ct;
	}

	public static Integer[] getIntegers(String[] str) {
		if (str != null) {
			Integer tt[] = new Integer[str.length];
			for (int i = 0; i < str.length; i++) {
				tt[i] = getInteger(str[i]);
			}
			return tt;
		}
		return null;
	}

	public static List getIntegersList(String[] str) {
		if (str != null) {
			List list = new ArrayList();
			for (int i = 0; i < str.length; i++) {
				list.add(getInteger(str[i]));
			}
			return list;
		}
		return null;
	}

	public static Integer getInteger(String str) {
		try {
			return new Integer(str.trim());
		} catch (Exception e) {
		}
		return null;
	}

	public static Integer getInteger1(String str) {
		try {
			return new Integer(str.trim());
		} catch (Exception e) {
		}
		return -1;
	}

	public static Integer getInteger2(Object obj) {
		if (obj != null && !obj.equals("") && !obj.equals("null")) {
			return new Integer(obj.toString().trim());
		}
		return null;
	}

	public static int getInteger3(Object obj) {
		if (obj != null && !obj.equals("") && !obj.equals("null")) {
			return new Integer(obj.toString().trim()).intValue();
		}
		return 0;
	}

	public static Integer getInteger(String str, Integer def) {
		Integer rint = getInteger(str);
		if (rint != null)
			return rint;
		return def;
	}

	public static Integer[] getArrInteger(String str) {
		StringTokenizer sto = new StringTokenizer(str, ",");
		Integer rr[] = new Integer[sto.countTokens()];
		int i = 0;
		while (sto.hasMoreTokens()) {
			rr[i] = getInteger(sto.nextToken());
			i++;
		}
		return rr;
	}

	public static String strDistinct(String str) {
		String[] strArr = str.split(",");
		String strAim = ",";
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals(""))
				continue;
			if (strAim.indexOf("," + strArr[i] + ",") == -1) {
				strAim = strAim + strArr[i] + ",";
			}
		}
		if (!strAim.equals(","))
			strAim = strAim.substring(1, strAim.length() - 1);
		else
			strAim = "";
		return strAim;
	}

	public static String replace(String arg) {
		if (arg == null) {
			return "";
		} else {
			byte[] bs = arg.getBytes();
			int j = 0;
			byte[] temp = new byte[bs.length];
			for (int i = 0; i < bs.length; i++) {
				if (0 < bs[i] && bs[i] <= 32) {
					// bs[i]=0;
				} else if (bs[i] == 127) {
					// bs[i]=0;
				} else if (bs[i] >= 128) {
					// bs[i]=0;
				} else {
					temp[j] = bs[i];
					// System.out.println(bs[i]);
					j++;
				}
			}
			if (j > 0) {
				return new String(temp, 0, j);
			} else {
				return "";
			}
		}
	}

	public static String[] getArrString(String str, String split) {
		StringTokenizer sto = new StringTokenizer(str, split);
		String rr[] = new String[sto.countTokens()];
		int i = 0;
		while (sto.hasMoreTokens()) {
			rr[i] = sto.nextToken();
			i++;
		}
		List list = new ArrayList();
		for (int j = 0; j < rr.length; j++) {
			list.add(rr[j]);
		}
		for (int j = 0; j < list.size(); j++) {
			for (int k = 0; k < j; k++) {
				if (list.get(j).toString().equalsIgnoreCase(list.get(k).toString())) {
					list.remove(j);
					j--;
					break;
				}
			}
		}
		String kkr[] = new String[list.size()];
		list.toArray(kkr);
		return kkr;
	}

	public static List<String> getArrList(String str) {
		List<String> list = new ArrayList();
		if (str != null) {
			if (str.indexOf(",") != -1) {
				String rr[] = str.split(",");
				for (int i = 0; i < rr.length; i++) {
					list.add(trim(rr[i]));
				}
			} else {
				list.add(str);
			}
		}
		return list;
	}

	public static String[] getArrString(String str) {
		if (trim(str) == null) {
			String[] strnull = { null };
			return strnull;
		}
		return getArrString(str, ",");
	}

	public static boolean[] getArrboolean(String str) {
		StringTokenizer sto = new StringTokenizer(str, ",");
		boolean rr[] = new boolean[sto.countTokens()];
		int i = 0;

		while (sto.hasMoreTokens()) {
			rr[i] = StrToBoolean(sto.nextToken()).booleanValue();
			i++;
		}
		return rr;
	}

	public static Short getShort2(String str) {
		try {
			return new Short(str);
		} catch (Exception e) {
		}
		return new Short("0");
	}

	public static short getShort(String str) {
		return getShort2(str).shortValue();
	}

	public static Byte getByte3(String str) {
		try {
			return new Byte(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static Byte getByte2(String str) {
		try {
			return new Byte(str);
		} catch (Exception e) {

		}
		return new Byte("0");
	}

	public static byte getByte(String str) {
		try {
			return new Byte(str).byteValue();
		} catch (Exception e) {
		}
		return new Byte("0").byteValue();
	}

	public static boolean isDate(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");

			Date d = (Date) simpleDF.parse(str);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Date getDate4(String str) {
		try {
			if (str.length() > 19) {
				str = str.substring(0, 19);
			}
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return getDate2(str);
		}
	}

	public static Date getDate3(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return getDate2(str);
		}
	}

	/**
	 * 鏍煎紡鍖杁ate绫诲瀷鏃ユ湡
	 * 
	 * @author oyxl
	 * @param date
	 * @return date
	 */
	public static Date getDate(Date date) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetime = simpleDF.format(date);
			return simpleDF.parse(datetime);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * @鏂规硶璇存槑:
	 * @author oyxl
	 * @createtime 2015骞�10鏈�7鏃� 涓婂崍10:09:27
	 * @param date
	 * @return
	 */
	public static Date getDate2(Date date) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
			String datetime = simpleDF.format(date);
			return simpleDF.parse(datetime);
		} catch (Exception e) {
		}
		return null;
	}

	// 2008/12/4 涓婂崍 12:00:00
	public static Date getDate31_(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return getDate2(str);
		}
	}

	public static Date getDate31(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return getDate2(str);
		}
	}

	public static Date getDate2(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static Date getDate2_(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy/MM/dd");

			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
		}
		return null;
	}

	public static Date getDate(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");

			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Date(System.currentTimeMillis());
	}

	public static String getYMD(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy骞碝M鏈坉d鏃�");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String getYMD1(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String getYMDHHMM(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String DateToStr3(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDF.format(d).toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static String getMMDDHHMM(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm");
			return simpleDF.format(d).toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static String getHHMM(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm");
			return simpleDF.format(d).toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static String getHHMMSS(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
			return simpleDF.format(d).toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static String DateToStr(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String DateToStr4(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyyMMddHHmmss");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String DateToStr41(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("HH-mm-ss");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String DateToStr7(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String DateToStr8(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	//
	// public static String DateToStr5(Date d) {
	// try {
	// SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// return simpleDF.format(d).toString();
	// } catch (Exception e) {
	// }
	//
	// return "";
	// }

	public static String IntToStr(Integer i) {
		try {
			return i.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String ShortToStr(short s) {
		try {
			Short ss = new Short(s);
			return ss.toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String ByteToStr(byte b) {
		try {
			Byte bb = new Byte(b);
			return bb.toString();
		} catch (Exception e) {
		}
		return "";
	}


	public static Boolean StrToBoolean(String str) {
		try {
			if (str.equalsIgnoreCase("1")) {
				return new Boolean(true);
			} else if (str.equalsIgnoreCase("0")) {
				return new Boolean(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// asp涓綁鏃ユ湡鏍煎紡
	public static Date getAspDate(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy/MM/dd");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {
			return getAspDate2(str);
		}
	}

	public static Date getAspDate2(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {

		}
		return null;
	}

	public static Date getAspDate3(String str) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyyMMddHH");
			return (Date) simpleDF.parse(str);
		} catch (Exception e) {

		}
		return null;
	}

	// 寰楀埌鏃ユ湡鏈�澶у��
	public static int getmaxDay(int year, int month) {
		boolean value = Leep(year);
		int result = 0;
		if (month % 2 != 0) {
			return 31;
		} else {
			if (value) {
				if (month == 2) {
					result = 29;
				} else {
					result = 30;
				}
			} else {
				if (month == 2) {
					result = 28;
				} else {
					result = 30;
				}
			}
		}
		return result;
	}

	public static boolean Leep(int year) {
		boolean value;
		if ((year % 4) != 0)
			value = false;
		else {
			if ((year % 100) != 0)
				value = true;
			else {
				if ((year % 400) != 0)
					value = false;
				else
					value = true;
			}
		}
		return value;
	}

	public static String DateToStr(Date d, Integer mm, Integer dd) {
		try {
			int y = getInteger(getYYYY(d));
			int m = getInteger(getMM(d));
			int di = getInteger(getDD(d));
			if (mm != null) {
				m = m + mm;
			}
			if (dd != null) {
				di = di + dd;
			}
			Date tempdate = getDate2(y + "-" + m + "-" + di);
			return DateToStr(tempdate);
		} catch (Exception e) {
		}

		return "";
	}

	public static String getHH(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("HH");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String getDD(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("dd");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String getMM(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("MM");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}

		return "";
	}

	public static String getYYYY(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static String getMD(Date d) {
		try {
			SimpleDateFormat simpleDF = new SimpleDateFormat("yyyyMMdd");
			return simpleDF.format(d).toString();
		} catch (Exception e) {
		}
		return "";
	}

	public static void errorlog(Log log, Class clazz, Exception e) {
		if (log.isErrorEnabled()) {
			// e.printStackTrace();
			log.error(clazz + " : " + e.getMessage());
		}
	}

	public static String getIP(String path) {
		if (path != null && !path.trim().equals("")) {
			int c = path.lastIndexOf("192");

			if (c != -1) {
				String temp = path.substring(c, path.length());

				int indexOf = 10000;
				if (temp != null) {
					if (temp.indexOf("\\") != -1) {
						if (temp.indexOf("\\") < indexOf)
							indexOf = temp.indexOf("\\");
					}
					if (temp.indexOf("/") != -1) {
						if (temp.indexOf("/") < indexOf)
							indexOf = temp.indexOf("/");
					}

				}
				if (temp != null && indexOf != -1 && indexOf != 10000) {
					temp = temp.substring(0, indexOf);
				}
				return temp;
			}

		}

		return "";
	}

	/**
	 * jiang
	 * 
	 * @param temp
	 * @return
	 */
	public static String getSize(double fileSize) {
		if (fileSize > 1000) {
			fileSize = div(fileSize, 1024.00, 2);
			if (fileSize >= 1000) {
				fileSize = div(fileSize, 1024.00, 2);
				if (fileSize >= 1000) {
					fileSize = div(fileSize, 1024.00, 2);
					return fileSize + " GB";
				} else {
					return fileSize + " MB";
				}
			} else {
				return fileSize + " KB";
			}
		} else {
			return fileSize + " B";
		}
	}

	public static boolean isGOT(String publishno) {

		if (publishno.toLowerCase().lastIndexOf("gto") != -1) {
			return true;
		}
		return false;
	}


	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︽槸鏁板瓧
	 * 
	 * @author ycw
	 * @param str
	 * @return
	 */
	public static Boolean isNumberic(String str) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("[0-9]*");
		if (null != str) {
			Matcher isNum = pattern.matcher(str.trim());
			if (isNum.matches())
				flag = true;
		}

		return flag;
	}

	/**
	 * 灏囨棩鏈�/鏅傞枔鍊兼牸寮忓寲鐐哄瓧绗︿覆
	 * 
	 * @param date
	 *            寰呮牸寮忓寲鐨勬棩鏈�/鏅傞枔鍊�
	 * @param pattern
	 *            杓稿嚭鏍煎紡
	 * @return 鏍煎紡鍖栫殑鏃堕棿瀛楃涓层��
	 */
	public static String formatDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 灏囨棩鏈�/鏅傞枔鍊兼牸寮忓寲鐐哄瓧绗︿覆,榛樿獚杓稿嚭鏍煎紡鐐簓yyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            寰呮牸寮忓寲鐨勬棩鏈�/鏅傞枔鍊�
	 * @return 鏍煎紡鍖栫殑鏃堕棿瀛楃涓层��
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 璁＄畻瀛楃涓睲D5鍊�
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getMD5String(String str) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		StringBuffer sb = new StringBuffer(32);

		md.update(str.getBytes("UTF-8"));

		byte[] result = md.digest();

		for (byte b : result) {
			int i = b & 0xff;
			if (i < 0xf) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();
	}

	/**
	 * 鐛插緱瑕佹坊鍔犵殑Id
	 * 
	 * @param ids
	 *            鏁版嵁搴撶殑id
	 * @param checkids
	 *            閫変腑鐨刬d
	 * @return
	 */
	public static List add_checkid(List ids, List checkids) {
		List<Object> lst = new ArrayList<Object>();
		if (ids == null) {
			return checkids;
		} else {
			Map<Object, Object> map = new HashMap<Object, Object>();
			for (int i = 0; i < ids.size(); i++) {
				map.put(ids.get(i), ids.get(i));
			}
			for (int i = 0; i < checkids.size(); i++) {
				if (map != null && map.get(checkids.get(i)) == null) {
					lst.add(checkids.get(i));
				}
			}
		}
		return lst;
	}

	/**
	 * 鐛插緱瑕佸垹闄ょ殑Id
	 * 
	 * @param ids
	 *            鏁版嵁搴撶殑id
	 * @param checkids
	 *            閫変腑鐨刬d
	 * @return
	 */
	public static List del_checkid(List ids, List checkids) {
		List<Object> lst = new ArrayList<Object>();
		if (ids != null && checkids == null || checkids.size() == 0) {
			return ids;
		}
		if (ids == null) {
			return null;
		} else {
			Map<Object, Object> map = new HashMap<Object, Object>();
			for (int i = 0; i < ids.size(); i++) {
				map.put(ids.get(i), ids.get(i));
			}
			for (int i = 0; i < checkids.size(); i++) {
				if (map != null && map.get(checkids.get(i)) != null) {
					map.remove(checkids.get(i));
				}
			}
			if (map != null) {
				for (Iterator it = map.values().iterator(); it.hasNext();) {
					Integer id = (Integer) it.next();
					lst.add(id);
				}
			} else {
				return checkids;
			}
		}
		return lst;
	}

	/**
	 * 鐛插緱瑕佹坊鍔犵殑Id
	 * 
	 * @param ids
	 *            鏁版嵁搴揑D
	 * @param checkids
	 *            閫変腑ID
	 * @return
	 */
	public static String getAddCheck(String ids, String[] checkids) {
		String include = "";
		ids = "," + ids;
		if (checkids != null) {
			for (int i = 0; i < checkids.length; i++) {
				if (ids.indexOf(("," + checkids[i] + ",")) < 0) {
					include = include + checkids[i] + ",";
				}
			}
		}
		if (include == null && include.equals(""))
			include = null;
		return include;
	}

	/**
	 * 鐛插緱瑕佸埅闄ょ殑Id
	 * 
	 * @param ids
	 *            鏁版嵁搴揑D
	 * @param checkids
	 *            閫変腑ID
	 * @return
	 */
	public static String getDelCheck(String ids, String[] checkids) {
		if (ids == null || ids.equals("")) {
			return null;
		}
		String[] checkidssplit = ids.split(",");
		String outclude = "";
		for (int i = 0; i < checkidssplit.length; i++) {
			String same = "0";
			if (!checkidssplit[i].equals("")) {
				for (int j = 0; j < checkids.length; j++) {
					if (checkids[j].equals(checkidssplit[i])) {
						same = "1";
						break;

					}
				}
			}
			if (!checkidssplit[i].equals("")) {
				if (same.equals("0")) {
					outclude = outclude + checkidssplit[i] + ",";
				}
			}
		}
		if (outclude != null && !outclude.equals(""))
			outclude = "," + outclude;
		return outclude;
	}

	// ----------------------------------------------------------------------------------------------------

	/**
	 * 淇濈暀n浣嶅皬鏁�
	 * 
	 * @param val
	 *            瑕佹搷浣滅殑Double鍊�
	 * @param retainNum
	 *            淇濈暀鐨勪綅鏁�
	 * @return
	 */
	public static String retainDecimal(Double val, int retainNum) {
		if (val == null)
			return null;
		StringBuilder builder = new StringBuilder();
		builder.append("#.");
		for (int i = 0; i < retainNum; i++) {
			builder.append("0");
		}
		DecimalFormat decimalFormat = new DecimalFormat(builder.toString());
		return decimalFormat.format(val);
	}

	/**
	 * @author oyxl 鐢熸垚9浣嶉毃姗熷瘑纰硷紝鍓�4浣嶇偤澶у皬瀵瓧姣嶏紝鍚�4浣嶇偤鏁稿瓧锛屾渶寰屼竴浣嶇偤鐗规畩绗﹁櫉
	 * @return
	 */
	public static String getRandomPassword() {
		StringBuffer password = new StringBuffer();
		Random ran = new Random();
		String enChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < 4; i++) {
			password.append(enChar.charAt(ran.nextInt(enChar.length() - 1)));
		}
		String numberChar = "0123456789";
		for (int j = 0; j < 4; j++) {
			password.append(numberChar.charAt(ran.nextInt(numberChar.length() - 1)));
		}
		String otherChar = "*?><!@#$%^&";
		password.append(otherChar.charAt(ran.nextInt(otherChar.length() - 1)));

		return password.toString();
	}

	/**
	 * @author oyxl 鐢熸垚N浣嶅彲鑳藉寘鍚ぇ灏忓瓧姣嶆暩瀛楃殑闅ㄦ瀵嗙⒓
	 * @param Integer
	 *            n
	 */

	public static String getRandomPassword(Integer n) {
		StringBuffer randNum = new StringBuffer();
		Random ran = new Random();
		String enChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < n; i++) {
			randNum.append(enChar.charAt(ran.nextInt(enChar.length() - 1)));
		}
		return randNum.toString();
	}

	/**
	 * 
	 * @鏂规硶瑾槑 鐢熸垚涓�涓洓浣嶆暟瀛楃殑楠岃瘉鐮侊紝鍚夊埄涓�鐐�.....
	 * @author yrf
	 * @createTime 2016/3/30 涓婂崍11:16:31
	 * @copyright kimleysoftServer
	 */
	public static String getVeriCode(Integer n) {
		String base = "01234567896886668868682367896886686";
		StringBuffer result = new StringBuffer();
		int index = 0;
		for (int i = 0; i < n; i++) {
			index = (int) (Math.random() * base.length());
			result.append(base.charAt(index));
		}
		return result.toString();
	}

	/**
	 * @author oyxl 澶嶅埗鐗堟湰锛岄噸鏂扮敓鎴愮増鏈彿
	 * @param versionno
	 */
	public static String getNewVersionno(String versionno) {
		String newVersionno = "";
		if (versionno.indexOf("_CP") != -1) {
			String verStr = versionno.substring(0, versionno.indexOf("_") + 3);
			Integer num = Integer.parseInt(versionno.substring(versionno.indexOf("_") + 3)) + 1;
			String numStr = String.valueOf(num);
			StringBuffer sbZero = new StringBuffer();
			for (int i = numStr.length(); i < 3; i++) {
				sbZero.append("0");
			}
			newVersionno = verStr + sbZero.toString() + numStr;
		} else {
			newVersionno = versionno + "_CP001";
		}
		return newVersionno;
	}

	public static Double getDouble(String str, Double defaultValue) {
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Double getDouble2(Float f) {
		try {
			return Double.valueOf(f);
		} catch (Exception e) {
			return 0d;
		}
	}

	/**
	 * @鏂规硶瑾槑: 鑾峰彇涓�涓瓧绗︿腑鐨勬暟瀛�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013/11/26 涓嬪崍4:37:16
	 * @copyright e-print
	 * @param str
	 * @return
	 */
	public static List<Double> getStringOfNumber(String str) {
		List<Double> list = new ArrayList<Double>();
		if (isNull(str)) {
			return list;
		}
		String regex = "\\d*[.]\\d*|\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			if (!"".equals(m.group())) {
				// System.out.println("come here:" + m.group());
				list.add(getDouble(m.group(), 0d));
			}
		}
		return list;
	}

	public static Float getFristFloatOfNumber(String str) {
		String regex = "\\d*[.]\\d*|\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			if (!"".equals(m.group())) {
				return getFloat(m.group(), -1f);
			}
		}
		return -1f;
	}

	public static String getLikeString(String likeString) {
		StringBuffer sb = new StringBuffer();
		String regex = "[\\u2E80-\\u9FFF]|\\w|\\d*[.]\\d*|\\d*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(likeString);
		while (m.find()) {
			if (!"".equals(m.group())) {
				sb.append(m.group());
			}
		}
		return sb.toString();
	}

	/**
	 * @鏂规硶瑾槑: 鍘绘帀灏嶈薄涓瓧绗︿覆鐨勫墠寰岀┖鏍�
	 * @author chenJian E-mail: test_t@163.COM
	 * @createTime 鍓靛缓鏅傞枔锛�2013-12-23 涓嬪崍5:44:49
	 * @copyright e-print
	 * @param obj
	 * @return
	 */
	public static Object objTirm(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String type = field.getGenericType().toString();
				if (type.equals("class java.lang.String")) {
					Method getM = obj.getClass().getMethod("get" + name);
					String value = (String) getM.invoke(obj);
					Class[] parameterTypes = new Class[1];
					parameterTypes[0] = String.class;
					if (!StringUtils.isNullOrEmpty(value)) {
						Method setM = obj.getClass().getMethod("set" + name, parameterTypes);
						setM.invoke(obj, new String[] { value.trim() });
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @鏂规硶璇存槑:鎵�鏈夐鍨嬭綁鎴愬瓧绗︿覆椤炲瀷
	 * @author oyxl
	 * @creatime 2014骞�8鏈�11鏃ヤ笂鍗�10:09:10
	 * @param value
	 * @return
	 */
	public static String objToString(Object value) {
		if (value != null) {
			if (value instanceof Short) {
				Short aa = (Short) value;
				return aa == 0 ? "false" : "true";
			}
			return value.toString();
		}
		return "";
	}

	public static void sortMap(Map<String, String> map) {
		List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		// 閫氳繃姣旇緝鍣ㄥ疄鐜版瘮杈冩帓搴�
		Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
				return mapping1.getKey().compareTo(mapping2.getKey());
			}
		});
	}

	public static String encoderBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	/**
	 * 鑾峰彇褰撳墠鏃ユ湡鐨勫墠鍚庡嚑澶�,杩斿洖鏍煎紡鍖栧悗鐨勬棩鏈�
	 *
	 * @author yrf
	 * @createTime 2016/4/29涓婂崍9:13:01
	 * @copyright kimleysoftserver
	 * @param day
	 * @return
	 */
	public static String beforeDateStr(int day) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		// rightNow.add(java.util.Calendar.DAY_OF_MONTH, -day);
		rightNow.add(Calendar.DAY_OF_MONTH, day);
		// 杩涜鏃堕棿杞崲
		String date = sim.format(rightNow.getTime()) + " 00:00:00";
		return date;
	}

	/**
	 * 鑾峰彇褰撳墠鏃堕棿鐨勫墠鍚庡嚑澶�
	 *
	 * @author yrf
	 * @createTime 2016/5/5涓嬪崍4:15:35
	 * @copyright kimleysoftserver
	 * @param day
	 * @return
	 */
	public static Date beforeDate(int day) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.DAY_OF_MONTH, day);
		return rightNow.getTime();
	}

	/**
	 * 鑾峰彇褰撳墠鏃堕棿鐨勫墠鍚庡嚑涓湀
	 *
	 * @author yrf
	 * @createTime 2016/5/28涓嬪崍3:52:07
	 * @copyright kimleysoftserver
	 * @param month
	 * @return
	 */
	public static Date beforeMonth(int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.MONTH, month);
		return rightNow.getTime();
	}

	/**
	 * 鑾峰彇鎸囧畾鏃ユ湡鐨勫墠鍚庡嚑澶�
	 *
	 * @author yrf
	 * @createTime 2016/7/29涓嬪崍4:25:45
	 * @copyright kimleysoftserver
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date beforeDateSel(Date d, Integer day) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(d);
		rightNow.add(Calendar.DAY_OF_MONTH, day);
		return rightNow.getTime();
	}

	/**
	 * lyw 杩斿洖骞冲勾锛岄棸骞�//鑳借4鎴�100鎴�400鏁撮櫎鐨勬槸闂板勾
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	/**
	 * lyw 杩斿洖鐣跺墠鏈堢殑鏈�寰屼竴澶� 2鏈堜唤澶╂暩锛岄棸骞�2鏈�29澶╋紝骞冲勾2鏈�28澶�
	 */
	public static String getNowMonLastDay() throws ParseException {
		String strY = null;
		String strZ = null;
		boolean leap = false;
		int x;
		int y;
		Calendar localTime = Calendar.getInstance();
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 1;
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-" + strZ;
	}

	/**
	 * lyw 杩斿洖浠绘剰鏈堢殑鏈�寰屼竴澶� 2鏈堜唤澶╂暩锛岄棸骞�2鏈�29澶╋紝骞冲勾2鏈�28澶�
	 */
	public static String getAnyLastDay(Calendar a, int month) throws ParseException {
		String strY = null;
		String strZ = null;
		boolean leap = false;
		int x;
		int y;
		// Calendar localTime = Calendar.getInstance();
		// x = localTime.get(Calendar.YEAR);
		x = a.get(Calendar.YEAR);
		// y = localTime.get(Calendar.MONTH) + 1;
		y = month;
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10 || y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-" + strZ + " 23:59:59";
	}

	/**
	 * 寰楀埌涓嬪�嬫湀鐨勮帿涓�澶� lyw
	 */
	public static String getNextMonday(Integer day) throws ParseException {
		int x;
		int y;
		String time = "";
		Calendar localTime = Calendar.getInstance();
		String strY = null;
		String strD = null;
		x = localTime.get(Calendar.YEAR);
		y = localTime.get(Calendar.MONTH) + 2;
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		strD = day >= 10 ? String.valueOf(day) : ("0" + day);
		return x + "-" + strY + "-" + strD;
	}

	/**
	 * 
	 * @author lyw
	 * @date 2017骞�11鏈�8鏃�
	 * @version 1.0.0
	 * @param date1
	 *            鏈�鍚庝粯娆炬椂闂�
	 * @param date2
	 *            褰撳墠鏃堕棿锛堝綋鍓嶆椂闂村繀椤诲ぇ浜庢渶鍚庝粯娆炬椂闂达級
	 * @return
	 */
	public static int getDateDifference(Date date1, Date date2) {

		Calendar now = Calendar.getInstance();
		now.setTime(date1);

		Calendar overdue = Calendar.getInstance();
		overdue.setTime(date2);

		boolean isafter = now.before(overdue);
		if (!isafter) {
			return -1;
		}

		int day1 = overdue.get(Calendar.DAY_OF_YEAR);
		int day2 = now.get(Calendar.DAY_OF_YEAR);

		int year1 = overdue.get(Calendar.YEAR);
		int year2 = now.get(Calendar.YEAR);

		if (year1 != year2) {// 涓嶅悓骞翠唤鐨�
			int timediscount = 0;

			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 == 0 || i % 400 == 0) {
					timediscount += 366;
				} else {
					timediscount += 365;
				}
			}

			return timediscount + (day1 - day2);
		} else {// 鐩稿悓骞翠唤鐨�
			return day1 - day2;
		}

	}

	/**
	 * 鑾峰彇绫讳腑鎵�鏈夌殑灞炴�у悕
	 * 
	 * @author yrf
	 * @createTime 2016/5/19涓婂崍9:45:04
	 * @copyright kimleysoftserver
	 * @param o
	 * @return
	 */
	public static List<String> FiledNameGet(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		List<String> filedlist = new ArrayList();
		for (int i = 0; i < fields.length; i++) {
			filedlist.add(fields[i].getName());
		}
		return filedlist;
	}

	public static String replaceParamter(String templateContent, String paramters, Object paramterValues) {
		if (templateContent.indexOf(paramters) != -1) {
			templateContent = templateContent.replace(paramters, paramterValues.toString());
		}
		return templateContent;
	}

	/**
	 * 缁濆鍊艰繘浣嶏紙姝ｅ�煎彉澶�,鍓�煎彉灏�)
	 * 
	 * @param value
	 *            蹇呴』涓烘暟鍊煎瀷
	 * @param scale
	 * @return
	 */
	public static <T> T roundUp(T value, int scale) {
		Double val = Double.parseDouble(value.toString());
		BigDecimal bdval = new BigDecimal(val);
		return (T) bdval.setScale(scale, BigDecimal.ROUND_UP);
	}

	/**
	 * 缁濆鍊艰垗浣�(姝ｅ�煎彉灏�,鍓�煎彉澶�)
	 * 
	 * @param value
	 *            蹇呴』涓烘暟鍊煎瀷
	 * @param scale
	 * @return
	 */
	public static <T> T roundDown(T value, int scale) {
		Double val = Double.parseDouble(value.toString());
		BigDecimal bdval = new BigDecimal(val);
		return (T) bdval.setScale(scale, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 鍥涜垗浜斿叆
	 * 
	 * @param value
	 *            蹇呴』涓烘暟鍊煎瀷
	 * @param scale
	 * @return
	 */
	public static <T> T roundHalfUp(T value, int scale) {
		Double val = Double.parseDouble(value.toString());
		BigDecimal bdval = new BigDecimal(val);
		return (T) bdval.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 鍚戜笂杩涗綅锛堝�煎彉澶э級
	 * 
	 * @param value
	 *            蹇呴』涓烘暟鍊煎瀷
	 * @param scale
	 * @return
	 */
	public static <T> T roundCeiling(T value, int scale) {
		Double val = Double.parseDouble(value.toString());
		BigDecimal bdval = new BigDecimal(val);
		return (T) bdval.setScale(scale, BigDecimal.ROUND_CEILING);
	}

	/**
	 * 鍚戜笅鑸嶄綅(鍊煎彉灏�)
	 * 
	 * @param value
	 *            蹇呴』涓烘暟鍊煎瀷
	 * @param scale
	 * @return
	 */
	public static <T> T roundFloor(T value, int scale) {
		Double val = Double.parseDouble(value.toString());
		BigDecimal bdval = new BigDecimal(val);
		return (T) bdval.setScale(scale, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * 绠�浣撹浆涓虹箒浣�
	 * 
	 * @return
	 */
	public static String toHkChinaese(String text) {
		String tradiStr = "";
		if (null != text) {
			tradiStr = ZHConverter.convert(text, ZHConverter.TRADITIONAL);// SIMPLIFIED绻佽浆绠� TRADITIONAL
		} else {
			return "";
		}
		return tradiStr;
	}
	

	public static float parse(float f) {
		float result = (int) Math.ceil(f);
		float singler = result % 10;
		if (singler >= 1) {
			result = result + 10 - singler;
		}
		return result;
	}


	/**
	 * 閫楀彿鍒嗛殧鐨勫瓧绗︿覆鍖归厤
	 * 
	 * @param sourceString
	 *            婧愬瓧绗︿覆
	 * @param matchString
	 *            鍖归厤鐨勫瓧绗︿覆
	 * @return Boolean
	 */
	public static Boolean matchSameString(String sourceString, String matchString) {
		if (sourceString != null && matchString != null) {
			String[] sourceStrings = sourceString.split(",");
			for (String string : sourceStrings) {
				if (string.equals(matchString)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 2涓棩鏈熼枔闅旀槸鍚︾浉宸�1澶�
	 * 
	 * @param d1
	 * @param d2
	 * @return d1 - d2 = ? 澶�
	 * @throws Exception
	 */

	/**
	 * 瑙ｅ瘑
	 * 
	 * @param content
	 *            寰呰В瀵嗗唴瀹�
	 * @return
	 */
	public static String decrypt(String contentString) {
		try {
			if (contentString != null) {
				KeyGenerator kgen = KeyGenerator.getInstance("AES");// 瀹炰緥鍖朅ES瀵嗛挜鐢熸垚鍣�
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed("kimleysoft".getBytes("UTF-8"));
				kgen.init(128, secureRandom);// 鏍规嵁瀵嗛挜鏄庢枃鍒濆鍖�128浣嶅瘑閽ョ敓鎴愬櫒
				SecretKey secretKey = kgen.generateKey();// 鐢熸垚瀵嗛挜
				byte[] enCodeFormat = secretKey.getEncoded();// 鑾峰彇瀵嗛挜瀛楄妭淇℃伅RAW
				SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 鍒涘缓瀵嗛挜绌洪棿
				Cipher cipher = Cipher.getInstance("AES");// 鏍规嵁AES鍔犲瘑绫诲瀷瀹炰緥鍖栧姞瀵嗙畻娉曞璞�
				cipher.init(Cipher.DECRYPT_MODE, key);// 鏍规嵁绫诲瀷锛堝姞瀵嗘垨瑙ｅ瘑锛変笌瀵嗛挜鍒濆鍖栧姞瀵嗙畻娉曞璞�
				byte[] content = parseHexStr2Byte(contentString);// 灏�16杩涘埗瀵嗘枃瀛楃涓茶浆鎹负瀛楄妭鏁扮粍
				byte[] result = cipher.doFinal(content);// 瑙ｅ瘑
				return new String(result, "UTF-8");// 杞崲鏄庢枃瀛楃涓�
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 灏嗕簩杩涘埗杞崲鎴�16杩涘埗
	 * 
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 灏�16杩涘埗杞崲涓轰簩杩涘埗
	 * 
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 鑾峰彇鏌愭湀鐨勬渶鍚庝竴澶�
	 * 
	 * @Title:getLastDayOfMonth @Description: @param:@param year @param:@param
	 * month @param:@return @return:String @throws
	 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 璁剧疆骞翠唤
		cal.set(Calendar.YEAR, year);
		// 璁剧疆鏈堜唤
		cal.set(Calendar.MONTH, month - 1);
		// 鑾峰彇鏌愭湀鏈�澶уぉ鏁�
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 璁剧疆鏃ュ巻涓湀浠界殑鏈�澶уぉ鏁�
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	
	
	/**
	 * 璁剧疆寮�濮嬫椂闂磞yyy-mm-dd HH:mm:ss
	 * 渚嬪瓙
	 * month 褰撳墠鏈堜唤鍔犱笂澶氬皯鏈� 鈥�+3鈥� 锛�   褰撳墠鏈堝噺鍘诲灏戞湀  鈥�-3鈥�
	 * day 褰撳墠鏃ュ姞涓� 澶氬皯澶┾��+3鈥� 锛�   褰撳墠鏃ュ噺鍘诲灏戝ぉ  鈥�-3鈥�
	 * 涓嶄紶濉玭ull
	 * 鏈�缁堣繑鍥�  YYYY-mm-dd 00 00 00
	 */
	public static Date setBeginDate(Integer month,Integer day) throws Exception {
//		鏌ヨ鐨勫紑濮嬫椂闂�(鍓�3涓湀璐墿杞�)
		Calendar start = Calendar.getInstance();
		if(month != null){
			start.add(Calendar.MONTH, month);
		}
		if(day != null){
			start.add(Calendar.DATE, day);
		}
		start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DATE), 00, 00, 00);
		
		return start.getTime();
	}
	
	public static Short changeShort(Byte obj) {
		Short data=null;
		
		if(null!=obj) {
			data=obj.shortValue();
		}
		
		return data;
	}
}
