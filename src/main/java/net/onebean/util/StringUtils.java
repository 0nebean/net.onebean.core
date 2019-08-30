package net.onebean.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.onebean.core.Json.OverrNullJsonMapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * 该类封装了字符串类型数据的常用方法，该类中的方法均为静态方法。
 * 
 */
public class StringUtils {
	public static final String EMPTY = "";
	/**
	 * 获得mapping str
	 * @param str
	 * @return
	 */
	public static String getMappingStr(String str){
		return str.toLowerCase().replace("_","");
	}

	/**
	 * 首字母转小写
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s){
		if(Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}


	/**
	 * 首字母转大写
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s){
		if(Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 将带有下划线的字符串转换成驼峰写法
	 * @param str
	 * @return
	 */
	public static String replaceUnderLineAndUpperCase(String str){
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		int count = sb.indexOf("_");
		while(count!=0){
			int num = sb.indexOf("_",count);
			count = num + 1;
			if(num != -1){
				char ss = sb.charAt(count);
				char ia = (char) (ss - 32);
				sb.replace(count , count + 1,ia + "");
			}
		}
		String result = sb.toString().replaceAll("_","");
		return org.apache.commons.lang.StringUtils.capitalize(result);
	}

	/***
	 * 驼峰命名转为下划线命名
	 * @param para 驼峰命名的字符串
	 */
	public static String humpToUnderline(String para){
		StringBuilder sb=new StringBuilder(para);
		int temp=0;//定位
		for(int i=0;i<para.length();i++){
			if(Character.isUpperCase(para.charAt(i))){
				sb.insert(i+temp, "_");
				temp+=1;
			}
		}
		return sb.toString().toLowerCase();
	}


	/**
	 * 字符串连接时的分隔符
	 * 
	 * 该分隔符用于{@link #toString(Collection)} 和
	 * {@link #toString(Collection, String)}方法。
	 * 
	 */
	public static final String DEFAULT_SEPARATOR = ",";

	/**
	 * 检查当前字符串是否为空
	 * 
	 * 如果字符串为null，或者长度为0，都被归为空。
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 返回结果，true表示不为空，false表示为空
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.equals("null"))
			return true;
		if (str.trim().equals(""))
			return true;
		return false;
	}

	/**
	 * 检查当前字符串是否为空
	 * 
	 * 如果字符串为null，或者长度为0，都被归为空。
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 返回结果，true表示为空，false表示不为空
	 */
	public static boolean isEmpty(Object str) {
		if (str == null)
			return true;
		if (str.toString().trim().equals(""))
			return true;
		return false;
	}

	/**
	 * 检查当前字符串是否不为空
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 返回结果，true表示为空，false表示不为空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 检查当前字符串是否不为空
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 返回结果，true表示为空，false表示不为空
	 */
	public static boolean isNotEmpty(Object str) {
		return !isEmpty(str);
	}

	/**
	 * 检查当前字符串是否为数字
	 * 
	 * @param s
	 *            要检查的字符串
	 * @return
	 */
	public static boolean isNumberic(String s) {
		if (StringUtils.isEmpty(s))
			return false;
		boolean rtn = validByRegex("^[-+]{0,1}\\d*\\.{0,1}\\d+$", s);
		if (rtn)
			return true;

		return validByRegex("^0[x|X][\\da-eA-E]+$", s);
	}

	/**
	 * 检查当前字符串是否符合正则表达式
	 * 
	 * @param regex
	 *            正则表达式
	 * @param input
	 *            要检查的字符串
	 * @return
	 */
	public static boolean validByRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex, 2);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}

	/**
	 * 检查当前字符串是否为空
	 * 
	 * 如果字符串为null，或者调用{@link #java.lang.String.trim()}后长度为0，都被归为空。
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 检查结果，true 为空，false不为空
	 */
	public static boolean isTrimEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串数组转化为 字符串
	 *
	 * @param array
	 * @return
	 */
	public static String arrayToString(Object[] array,String split) {
		if (array == null)
			return "";
		StringBuffer result = new StringBuffer();
		for (Object item : array) {
			result.append(item).append(split);
		}
		if (result.length() > 0) {
			return result.substring(0, result.length() - 1);
		}
		return "";
	}

	/**
	 * 字符串数组转化为 字符串
	 * 
	 * @param array
	 * @return
	 */
	public static String arrayToString(Object[] array) {
		if (array == null)
			return "";
		StringBuffer result = new StringBuffer();
		for (Object item : array) {
			result.append(item).append(",");
		}
		if (result.length() > 0) {
			return result.substring(0, result.length() - 1);
		}
		return "";
	}

	/**
	 * 将对象解析成字符串
	 * 
	 * @param value
	 *            要解析的对象
	 * @return 解析的字符串
	 */
	public static String toJSON(Object value) {
		// 检查value是否为空
		if (value == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		SimpleDateFormat format = new SimpleDateFormat(
				DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
		// 设置日期格式
		mapper.writer(format);
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	/**
	 * 判断当前字符串是否是由数字组成
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 结果
	 */
	public static boolean isDigit(String str) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return false;
		}
		return Pattern.matches("^\\d+$", str);
	}

	/**
	 * 判断当前字符串是否表示数字区间
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 结果
	 */
	public static boolean isDigitRange(String str) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return false;
		}
		return Pattern.matches("^\\d+-\\d+$", str);
	}

	/**
	 * 替换字符串中的字符,该方法用于velocity层，只替换第一次匹配
	 * 
	 * @param str
	 *            被替换的原始字符串
	 * @param regex
	 *            替换的字符
	 * @param value
	 *            替换的值
	 * @return 替换结果
	 */
	public static String replace(String str, String regex, String value) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return str;
		}
		return str.replace(regex, value);
	}

	/**
	 * 替换字符串中的字符,该方法用于velocity层，替换所有匹配
	 * 
	 * @param str
	 *            被替换的原始字符串
	 * @param regex
	 *            替换的字符
	 * @param value
	 *            替换的值
	 * @return 替换结果
	 */
	public static String replaceAll(String str, String regex, String value) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return str;
		}
		return str.replaceAll(regex, value);
	}

	/**
	 * 提换html的部分特殊字符
	 * 
	 * 只替换了&、<和>符号
	 * 
	 * @param str
	 *            要替换的字符串
	 * @return 替换结果
	 */
	public static String formatHtml(String str) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return str;
		}
		// 替换特殊字符串
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		return str;
	}

	/**
	 * 替换HTML的全部特殊字符
	 * 
	 * 替换了&、<、>、"和空格
	 * 
	 * @param str
	 *            要替换的字符串
	 * @return 替换的结果
	 */
	public static String formatAllHtml(String str) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return str;
		}
		// 替换特殊字符串
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll(" ", "&nbsp;");
		return str;
	}

	/**
	 * 将过长字符串进行截取，并在末尾追加描述符，如...
	 * 
	 * @param str
	 *            要截取的字符串
	 * @param maxLength
	 *            最大长度
	 * @param replace
	 *            追加的字符串，如果是null，则默认为...
	 * @return 截取结果
	 */
	public static String cut(String str, int maxLength, String replace) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return str;
		}
		// 检查replace是否存在
		if (replace == null) {
			replace = "...";
		}
		// 检查长度
		if (str.length() + replace.length() <= maxLength || maxLength < 1
				|| replace.length() > maxLength) {
			return str;
		}
		// 开始截取
		return str.substring(0, maxLength - replace.length()) + replace;
	}

	public static String trimSufffix(String toTrim, String trimStr) {
		if (toTrim == null) {
			return "";
		}
		while (toTrim.endsWith(trimStr)) {
			toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
		}
		return toTrim;
	}

	/**
	 * 合并两个路径
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combinePath(String path1, String path2) {
		if (path1 == null || path1.isEmpty()) {
			return path2;
		}
		if (path2 == null || path2.isEmpty()) {
			return path1;
		}

		return trimSufffix(path1, "/") + "/" + trimSufffix(path2, "/");

	}

	/**
	 * 将string 集合拼接成字符串，使用{@value #DEFAULT_SEPARATOR}分隔
	 * 
	 * @param list
	 *            要处理的集合
	 * @return 处理结果
	 */
	public static String toString(Collection<String> list) {
		// 检查list是否存在
		if (list == null) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		Iterator<String> it = list.iterator();
		String next = null;
		while (it.hasNext()) {
			next = it.next();
			if (next == null) {
				continue;
			}
			rs.append(next);
			// 如果有下一个值，则添加分隔符
			if (it.hasNext()) {
				rs.append(DEFAULT_SEPARATOR);
			}
		}
		return rs.toString();
	}

	/**
	 * 将string 集合拼接成字符串，使用特定字符分隔
	 * 
	 * @param list
	 *            要处理的集合
	 * @param separator
	 *            分隔符，如果为null，则默认使用{@value #DEFAULT_SEPARATOR}
	 * @return 处理结果
	 */
	public static String toString(Collection<String> list, String separator) {
		if (separator == null) {
			separator = DEFAULT_SEPARATOR;
		}
		// 检查list是否存在
		if (list == null) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		Iterator<String> it = list.iterator();
		String next = null;
		while (it.hasNext()) {
			next = it.next();
			if (next == null) {
				continue;
			}
			// 如果有下一个值，则添加分隔符
			if (it.hasNext()) {
				rs.append(separator);
			}
		}
		return rs.toString();
	}

	/**
	 * 检查输入的字符串是否为查询条件 有[ 标识
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isQueryCondition(String str) {
		// 检查是否为空
		if (StringUtils.isTrimEmpty(str)) {
			return false;
		}
		// 检查是否为查询条件
		if (str.indexOf("[") != -1) {
			return true;
		}

		return false;
	}

	/**
	 * @Title strToInt
	 * @Description 将字符串数字转换成数字
	 * @param ojb
	 * @return Integer
	 * @throws
	 */
	public static Integer strToInt(Object ojb) {
		if (isEmpty(ojb.toString()))
			return 0;
		try {
			return Integer.valueOf(ojb.toString());
		} catch (Exception e) {
			return 0;
		}
	}

	public static String formatParamMsg(String message, Object[] args) {
		for (int i = 0; i < args.length; i++) {
			message = message.replace(new StringBuilder().append("{").append(i)
					.append("}").toString(), args[i].toString());
		}
		return message;
	}

	@SuppressWarnings("rawtypes")
	public static String formatParamMsg(String message, Map params) {
		if (params == null)
			return message;
		Iterator keyIts = params.keySet().iterator();
		while (keyIts.hasNext()) {
			String key = (String) keyIts.next();
			Object val = params.get(key);
			if (val != null) {
				message = message.replace(new StringBuilder().append("${")
						.append(key).append("}").toString(), val.toString());
			}
		}
		return message;
	}

	public static StringBuilder formatMsg(CharSequence msgWithFormat,
			boolean autoQuote, Object[] args) {
		int argsLen = args.length;
		boolean markFound = false;

		StringBuilder sb = new StringBuilder(msgWithFormat);

		if (argsLen > 0) {
			for (int i = 0; i < argsLen; i++) {
				String flag = new StringBuilder().append("%").append(i + 1)
						.toString();
				int idx = sb.indexOf(flag);

				while (idx >= 0) {
					markFound = true;
					sb.replace(idx, idx + 2, toString(args[i], autoQuote));
					idx = sb.indexOf(flag);
				}
			}

			if ((args[(argsLen - 1)] instanceof Throwable)) {
				StringWriter sw = new StringWriter();
				((Throwable) args[(argsLen - 1)])
						.printStackTrace(new PrintWriter(sw));
				sb.append("\n").append(sw.toString());
			} else if ((argsLen == 1) && (!markFound)) {
				sb.append(args[(argsLen - 1)].toString());
			}
		}
		return sb;
	}

	public static StringBuilder formatMsg(String msgWithFormat, Object[] args) {
		return formatMsg(new StringBuilder(msgWithFormat), true, args);
	}

	public static String toString(Object obj, boolean autoQuote) {
		StringBuilder sb = new StringBuilder();
		if (obj == null) {
			sb.append("NULL");
		} else if ((obj instanceof Object[])) {
			for (int i = 0; i < ((Object[]) obj).length; i++) {
				sb.append(((Object[]) (Object[]) obj)[i]).append(", ");
			}
			if (sb.length() > 0)
				sb.delete(sb.length() - 2, sb.length());
		} else {
			sb.append(obj.toString());
		}

		if ((autoQuote)
				&& (sb.length() > 0)
				&& ((sb.charAt(0) != '[') || (sb.charAt(sb.length() - 1) != ']'))
				&& ((sb.charAt(0) != '{') || (sb.charAt(sb.length() - 1) != '}'))) {
			sb.insert(0, "[").append("]");
		}
		return sb.toString();
	}

	/**
	 * 判段对象是否为null，为空返回""
	 * 
	 * @param Object
	 *            s
	 * @return String 为空返回"",不为空返回转化为String的值
	 */
	public static String null2String(Object s) {
		return s == null ? "" : s.toString();
	}

	/**
	 * 判段String是否为null，为空返回""
	 * 
	 * @param String
	 *            s
	 * @return String 为空返回"",不为空直接返回
	 */
	public static String null2String(String s) {
		return s == null ? "" : s.toString();
	}

	/**
	 * 过滤非法字符
	 * 
	 * @param source
	 */
	public static String filterChar(String source) {
		String regEx = "[`~!@#$^&*()+=|{}':;',\\[\\].<>/?~@#……&*]";
		// String source =
		// "？？*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中文哐哐哐哐哐哐正在做}34{45[]12.fd'*&999，下面是中文的字符￥？";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(source);
		String title = m.replaceAll("").trim();
		char ch = title.charAt(0);
		do {
			ch = title.charAt(0);
			title = title.replace(ch + "", "");
		} while (isChinese(ch)); // 保证第一个字符为中文字符
		// System.out.println("原始title:" + source);
		// System.out.println("过滤后的title：" + title);
		return title;
	}

	public static String filterSpecial(String source) {
		String regEx = "[`~!@#$^&*()+=|{}':;',\\[\\].<>/?~@#%……&*]";
		// String source =
		// "？？*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中文哐哐哐哐哐哐正在做}34{45[]12.fd'*&999，下面是中文的字符￥？";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(source);
		String title = m.replaceAll("").trim();
		return title;
	}

	/**
	 * 判断是否为中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		boolean result = false;
		if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
			result = true;
		}
		return result;
	}

	/**
	 * 判断是否英文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(char ch) {
		if (!Character.isDigit(ch)) {
			return false;
		}
		return true;
	}

	/**
	 * 是否为有效的UTF8字符
	 * 
	 * @param rawtext
	 * @return
	 */
	public static boolean isUTF8(byte[] rawtext) {
		int score = 0;
		int i, rawtextlen = 0;
		int goodbytes = 0, asciibytes = 0;
		// Maybe also use UTF8 Byte Order Mark: EF BB BF
		// Check to see if characters fit into acceptable ranges
		rawtextlen = rawtext.length;
		for (i = 0; i < rawtextlen; i++) {
			if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) {
				// 最高位是0的ASCII字符
				asciibytes++;
				// Ignore ASCII, can throw off count
			} else if (-64 <= rawtext[i]
					&& rawtext[i] <= -33
					// -0x40~-0x21
					&& // Two bytes
					i + 1 < rawtextlen && -128 <= rawtext[i + 1]
					&& rawtext[i + 1] <= -65) {
				goodbytes += 2;
				i++;
			} else if (-32 <= rawtext[i]
					&& rawtext[i] <= -17
					&& // Three bytes
					i + 2 < rawtextlen && -128 <= rawtext[i + 1]
					&& rawtext[i + 1] <= -65 && -128 <= rawtext[i + 2]
					&& rawtext[i + 2] <= -65) {
				goodbytes += 3;
				i += 2;
			}
		}
		if (asciibytes == rawtextlen) {
			return false;
		}
		score = 100 * goodbytes / (rawtextlen - asciibytes);
		// If not above 98, reduce to zero to prevent coincidental matches
		// Allows for some (few) bad formed sequences
		if (score > 98) {
			return true;
		} else if (score > 95 && goodbytes > 30) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean iszm(String str) {
		boolean isWord = str.matches("[a-zA-Z0-9\\s]+");
		return isWord;
	}

	public static boolean getByteEncode(byte[] b) {
		if (b != null && b.length > 3) {
			byte utf8[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
			if ((b[0] == utf8[0]) && (b[1] == utf8[1]) && (b[2] == utf8[2]))
				return true;
		}
		return false;
	}

	public static String encodeUtf8(String str) {
		try {
			return URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "_abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getTimeRandomString() {
		return DateUtils.getDetailTime()
				+ String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否为手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		if (StringUtils.isNotEmpty(mobiles)) {
			Pattern p = Pattern
					.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		}
		return false;
	}

	/**
	 * 将对象忽略Null值解析成字符串
	 * 
	 * @param value
	 *            要解析的对象
	 * @return 解析的字符串
	 */
	public static String overrNulltoJSON(Object value) {
		// 检查value是否为空
		if (value == null) {
			return null;
		}
		ObjectMapper mapper = new OverrNullJsonMapper();
		SimpleDateFormat format = new SimpleDateFormat(
				DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
		// 设置日期格式
		mapper.writer(format);
		// mapper.setSerializationInclusion(Include.NON_EMPTY);
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return null;
		}
	}



	/**
	 * 截断显示在首页文章内容的数据
	 * 
	 * @param content
	 * @return
	 */
	public static String setSummary(String content, Integer length) {
		// 由于直接拿前70个字符，如果前70个字符中包含图片，造成前台页面显示不完整
		// 现对前70字符有图片的做特殊处理
		// 先判断该文章内容中是否包含图片
		if (StringUtils.isBlank(content)) {
			return "";
		}
		content = content.replaceAll("<[^>]+>", "");
		if (StringUtils.isNotBlank(content) && content.length() > length) {
			content = content.substring(0, length) + "...";
		}
		return content;
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not
	 *         whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * emoji表情替换
	 *
	 * @param source
	 *            原字符串
	 * @param slipStr
	 *            emoji表情替换成的字符串
	 * @return 过滤后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String filterWrongChars(String text)
			throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

	public static String toStrTrim(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}


	public static boolean isJson(String content) {
		try {
			JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}