package net.onebean.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期工具类
 * 
 * 日期类型的工具类，该类均为静态方法，直接调用
 * 
 * 
 */
public class DateUtils {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdfSimple = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat sdfFileName = new SimpleDateFormat(
			"yyyy-MM-dd HH_mm_ss");
	private static final SimpleDateFormat sdfDetail = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	private static final SimpleDateFormat sdfSimpleymd = new SimpleDateFormat(
			"yyyy年MM月dd日");
	private static final SimpleDateFormat sdf_yyMMdd = new SimpleDateFormat(
			"yyMMdd");
	private static final SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat(
			"yyyyMMdd");
	
	private static final SimpleDateFormat sdf_yyyyMMddHHmm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	/**
	 * 日期格式化模式（日期类型数据）
	 * 
	 * 日期格式化模式，使用此模式将日期格式化为“2012-10-08”，一般用于日期类型数据格式化
	 * 
	 */
	public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

	public static final String PATTERNS_YYYY_MM_DD = "yyyy年MM月dd日";

	/**
	 * 日期格式化模式（时间类型数据）
	 * 
	 * 日期格式化模式，使用此模式将日期格式化为“2012-10-08 10:10:08”，一般用于时间类型数据格式化
	 * 
	 */
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String PATTERNS_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	public static final String PATTERNS_HH_MM = "HH:mm";
	
	public static final String PATTERNS_MM_DD = "MM-dd";
	/**
	 * 根据特定模式，将字符串型日期对象解析成Date对象
	 * 
	 * @param source
	 *            要解析的字符串
	 * @param pattern
	 *            解析模式，默认为{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}
	 * @return 解析结果
	 * @throws ParseException
	 *             如果要解析的字符串格式不匹配，则抛出此异常
	 */
	public static Date parse(String source, String pattern)
			throws ParseException {
		// 检查value是否为空
		if (source == null) {
			return null;
		}
		// 如果pattern为空
		if (pattern == null) {
			// 设置pattern为PATTERN_YYY_MM_DD_HH_MM_SS
			pattern = PATTERN_YYYY_MM_DD_HH_MM_SS;
		}
		// 初始化一个format类
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		// 开始解析
		return format.parse(source);
	}

	/**
	 * 将日期对象根据特定格式格式化成字符串
	 * 
	 * @param source
	 *            要格式化的日期对象
	 * @param pattern
	 *            格式化模式，默认为{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}
	 * @return 格式化后的字符串
	 */
	public static String format(Date source, String pattern) {
		// 检查value是否为空
		if (source == null) {
			return null;
		}
		// 如果pattern为空
		if (pattern == null) {
			// 设置pattern为PATTERN_YYYY_MM_DD_HH_MM_SS
			pattern = PATTERN_YYYY_MM_DD_HH_MM_SS;
		}
		// 初始化一个format类
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(source);
	}

	/**
	 * 按 {@value #PATTERN_YYYY_MM_DD}格式格式化日期对象
	 * 
	 * @param source
	 *            日期对象
	 * @return 格式化值
	 */
	public static String format(Date source) {
		return format(source, PATTERN_YYYY_MM_DD);
	}

	/**
	 * 格式化年月日
	 * 
	 * @param source
	 * @return
	 */
	public static String formats(Date source) {
		return format(source, PATTERNS_YYYY_MM_DD);
	}

	/**
	 * 将日期对象格式化成特定格式字符串
	 * 
	 * @param source
	 *            日期对象
	 * @param isFullPattern
	 *            格式化类型，true{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}类型，false
	 *            {@value #PATTERN_YYYY_MM_DD}类型
	 * @return 格式化后的值
	 */
	public static String format(Date source, boolean isFullPattern) {
		// PATTERN_YYYY_MM_DD_HH_MM_SS 格式
		if (isFullPattern) {
			return format(source, PATTERN_YYYY_MM_DD_HH_MM_SS);
		}
		// PATTERN_YYYY_MM_DD 格式
		else {
			return format(source);
		}
	}

	/**
	 * 返回服务器时间
	 * 
	 * @return
	 */
	public static Date getServerDate() {
		return new Date();
	}

	/**
	 * 
	 * 日期转字符串
	 */
	public static synchronized String dateToString(Date date) {

		return (date == null || "".equals(date.toString())) ? "" : sdf
				.format(date);

	}

	/**
	 * 
	 * 日期转字符串
	 */
	public static synchronized String dateToStringSimple(Date date) {

		return (date == null || "".equals(date.toString())) ? "" : sdfSimple
				.format(date);

	}
	
	/**
	 * String转Timestamp
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp strToTimeStamp(String str) throws ParseException
	{
		 Date date11 = sdfSimple.parse(str);

		 String time = sdfSimple.format(date11);

		 Timestamp ts = Timestamp.valueOf(time);

		 return ts;
	}
	
	/**
	 * String转Timestamp
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp stringToTimeStamp(String str)
	{
		 Date date11 = new Date();
		try {
			date11 = sdf.parse(str);
		} catch (ParseException e) {
			try {
				date11 = sdf_yyyyMMddHHmm.parse(str);
			} catch (ParseException e1) {
				return null;
			}
		}

		 String time = sdf.format(date11);

		 Timestamp ts = Timestamp.valueOf(time);

		 return ts;
	}

	/**
	 * 
	 * 获得当前时间
	 */
	public static String getCurrentDate() {

		return sdfFileName.format(new Date());

	}

	/**
	 * 
	 * 获得当前日期2013年7月26日
	 */
	public static String getCurrentDateToDay() {

		return sdfSimpleymd.format(new Date());

	}

	/**
	 * 
	 * 获得当前时间(包含毫秒)
	 */
	public static String getDetailTime() {

		return sdfDetail.format(new Date());

	}

	/**
	 * 
	 * 查询日期
	 */
	public static synchronized Date getDate(Date date, int value) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, value);
		return cal.getTime();

	}

	/**
	 * 
	 * 字符串转时间
	 */
	public static synchronized Date strToDate(String str) throws Exception {

		return sdfSimple.parse(str);

	}
	
	/**
	 * 
	 * 字符串转时间
	 */
	public static synchronized Date stringToDate(String str)throws Exception {

		return sdf.parse(str);

	}
	

	/**
	 * 按 {@value #PATTERN_YYYY_MM_DD}格式格式化日期对象
	 * 
	 * @param source
	 *            日期对象
	 * @return 格式化值
	 */
	public static String format(Object obj) {
		SimpleDateFormat format = new SimpleDateFormat(
				PATTERN_YYYY_MM_DD_HH_MM_SS);
		if (obj == null)
			return "";
		return format.format(obj);
	}

	/**
	 * 把毫秒转换为日期字符串
	 * 
	 * @param millis
	 *            毫秒数
	 * @return
	 */
	public static String getDateTimeByMillisecond(Long millis) {
		Date date = new Date(millis);
		SimpleDateFormat format = new SimpleDateFormat(
				PATTERN_YYYY_MM_DD_HH_MM_SS);
		String time = format.format(date);
		return time;
	}

	/**
	 * 把毫秒转换为年+月+日+小时+分+秒
	 * 
	 * @param millis
	 * @return
	 */
	public static String convertMillis(Long millis) {
		/*
		 * String y = "年"; String m = "月"; String d = "天"; String h = "小时";
		 * String mi = "分"; String ms = "秒";
		 */
		if (millis <= 0l)
			return "";
		Long d = 24 * 60 * 60 * 1000l;
		Long h = 60 * 60 * 1000l;
		Long mi = 60 * 1000l;
		Long ms = 1000l;
		long day = millis / d;
		long hour = millis % d / h;
		long min = millis % d % h / mi;
		long sec = millis % d % h % mi / ms;
		return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
	}

	/**
	 * 计算两个日期相差的天数 日期类型需为：yyyy-MM-dd
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int divideDay(String startTime, String endTime)
			throws ParseException {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = ft.parse(endTime);
		Date date2 = ft.parse(startTime);
		long quot = date1.getTime() - date2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return (int)quot;
	}
	
	/**
	 * 计算两个日期相差的天数 日期类型需为：yyyy-MM-dd
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int divideDay(Date startTime, Date endTime){
		long quot = endTime.getTime() - startTime.getTime();
		quot = quot / 1000 / 60 / 60 / 24;
		return (int)quot;
	}

	/**
	 * 得到下一个日期
	 * 
	 * @param date
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static Date nextDays(Date date, long day) throws ParseException {

		long time = date.getTime();
		time += 1000 * 24 * 60 * 60 * day;
		Date date1 = new Date(time);
		return date1;
	}

	public static String dayForWeek(String pTime) throws Exception {
		String[] weeks = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return weeks[dayForWeek - 1];
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算从20140401到现在的天数
	 * 
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween20140401() {
		Calendar date20140401 = Calendar.getInstance();
		date20140401.set(Calendar.YEAR, 2014);
		date20140401.set(Calendar.MONTH, 3);// 月份从0 1 2 3 计算
		date20140401.set(Calendar.DAY_OF_MONTH, 1);
		long time1 = date20140401.getTimeInMillis();
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.HOUR_OF_DAY, 0);   
		cd.set(Calendar.MINUTE, 0);   
		cd.set(Calendar.SECOND, 0);   
		long time2 = cd.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days + 1));
	}
	
	/**
	 * 距离今日多少天
	 * @param date
	 * @return
	 */
	public static int daysBetweenToDay(Timestamp date){
		Integer press_date = daysBetween20140401();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		int reDate = DateUtils.dayInDate20140401(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		Integer time = press_date - reDate;
		
		return time;
	}

	/**
	 * 传入一个pressdate 获取到对应的日期
	 * 
	 * @param pressDate
	 * @return
	 */
	public static Date dayIn20140401(Integer pressDate) {
		Calendar date20140401 = Calendar.getInstance();
		date20140401.set(Calendar.YEAR, 2014);
		date20140401.set(Calendar.MONTH, 3);
		date20140401.set(Calendar.DAY_OF_MONTH, 1);
		date20140401.add(Calendar.DATE, pressDate);
		return date20140401.getTime();
	}

	/**
	 * 给定时间计算从20140401到今天的整数值
	 * 
	 * @param date
	 * @return
	 */
	public static int dayInDate20140401(int year, int month, int day) {
		Calendar date20140401 = Calendar.getInstance();
		date20140401.set(Calendar.YEAR, 2014);
		date20140401.set(Calendar.MONTH, 3);
		date20140401.set(Calendar.DAY_OF_MONTH, 1);
		long time1 = date20140401.getTimeInMillis();
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.YEAR, year);
		cd.set(Calendar.MONTH, month);
		cd.set(Calendar.DAY_OF_MONTH, day);
		cd.set(Calendar.HOUR_OF_DAY, 0);   
		cd.set(Calendar.MINUTE, 0);   
		cd.set(Calendar.SECOND, 0);   
		long time2 = cd.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days + 1));
	}

	/**
	 * 获取当前时间以yyyy-MM-dd HH:mm:ss格式显示的时间
	 * 
	 * @return String
	 */
	public static String getNowyyyy_MM_dd_HH_mm_ss() {
		return format(Calendar.getInstance().getTime(), true);
	}

	/**
	 * 获取当前以yyMMdd格式显示的日期
	 * 
	 * @return String
	 */
	public static String getNowyyMMdd() {
		return sdf_yyMMdd.format(Calendar.getInstance().getTime());
	}
	
	public static String getNowYyyyMMdd(){
		return sdf_yyyyMMdd.format(Calendar.getInstance().getTime());
	}
	
	public static String getUserSetYyyyMMdd(int press_date){
		return sdf_yyyyMMdd.format(dayIn20140401(press_date));
	}
	
	public static String getMonth_ch(String month) {
		String[] month_ch = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
				"九月", "十月", "十一月", "十二月" };
		return month_ch[Integer.parseInt(month) - 1];
	}

	/**
	 * 获取指定天数以前的字符串表示
	 * 
	 * @param before
	 * @return
	 */
	public static String getBeforeDayStr(int before) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// System.out.println(calendar.get(Calendar.DAY_OF_MONTH));// 今天的日期
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- before);// 让日期-1
		return DateUtils.format(calendar.getTime());
	}
	
	/**
	 * 格式化时间
	 * 当天:HH:mm,非当天:MM-dd
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		if(sdfSimple.format(date).equalsIgnoreCase(sdfSimple.format(new Date()))) {
			return format(date, PATTERNS_HH_MM);
		}
		return format(date, PATTERNS_MM_DD);
	}
	
	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String parse_yyyy_MM_dd_HH_mm(Date date) {
		return sdf_yyyyMMddHHmm.format(date);
	}
	
	public static String getYestoday(String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		if(StringUtils.isEmpty(pattern)) {
			pattern = "yyyyMMdd";
		}
		return new SimpleDateFormat(pattern).format(calendar.getTime());
	}
	
	/**
	 * 将文章发表时间转换成几分钟前、几秒前、几小时前等等提示语
	 * @param create_time
	 * @return String
	 */
	public static String setDateDiff(Timestamp create_time)
	{
		// 获取当前时间并转换为毫秒
		Date now = new Date();
		String dateDiff = null;
		
		// 如果创建时间不为空才转换
		if (StringUtils.isNotEmpty(create_time))
		{
			int isYeaterDay = 0;
		    try {
		    	// 判断是否是昨天
				isYeaterDay = isYeaterday(create_time, now);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
		    // 获取文章创建时间
		    long createTime = create_time.getTime();
		    String hours = getHour(create_time);
		    int pmHours = Integer.valueOf(getHour(create_time));
		    String mins = getMin(create_time);
		    String month = getMonth(create_time);
		    
		    // 今天显示几分钟，几小时，几秒等等
		    if (isYeaterDay == -1)
		    {
			    
				long l = now.getTime() - createTime;
			    long day=l/(24*60*60*1000);
			    long hour=(l/(60*60*1000)-day*24);
			    long min=((l/(60*1000))-day*24*60-hour*60);
			    long s=(l/1000-day*24*60*60-hour*60*60-min*60);
			    if(hour > 0)
			    {
			    	if (hour > 1)
			    	{
			    		dateDiff = hour + " hrs ";
			    	}
			    	else
			    	{
			    		dateDiff = hour + " hr ";
			    	}
			    	
			    }   
			    else if(min > 0)
			    {
			    	if (min > 1)
			    	{
			    		dateDiff = min + " mins ";
			    	}
			    	else
			    	{
			    		dateDiff = min + " min ";
			    	}
			    }
			    else if (s > 0)
			    {
			    	if (s > 1)
			    	{
			    		dateDiff = s + " secs ";
			    	}
			    	else
			    	{
			    		dateDiff = s + " sec ";
			    	}
			    }
		    }
		    else if (isYeaterDay == 0)
		    {
		    	// 昨天，显示Yesterday at 11:50 am  Yesterday at 2:50 pm
		    	dateDiff = "Yesterday at ";
		    	
		    	// 如果超过11点，说明是下午了
		    	if (pmHours > 11)
		    	{
		    		// 如果超过12点，减去12来表示
		    		if (pmHours > 12)
		    		{
		    			pmHours = pmHours - 12;
		    		}
		    		dateDiff = dateDiff + pmHours  + ":" + mins + " pm";
		    	}
		    	else
		    	{
		    		dateDiff = dateDiff + hours  + ":" + mins + " am";
		    	}
		    }
		    else if (isYeaterDay == 1)
		    {
		    	// 更早显示日期：December 25 at 11:50 am
		    	dateDiff = getMonths().get(Integer.valueOf(month)) + " " + getDay(create_time) + " at ";
		    	
		    	// 如果超过11点，说明是下午了
		    	if (pmHours > 11)
		    	{
		    		// 如果超过12点，减去12来表示
		    		if (pmHours > 12)
		    		{
		    			pmHours = pmHours - 12;
		    		}
		    		dateDiff = dateDiff + pmHours  + ":" + mins + " pm";
		    	}
		    	else
		    	{
		    		dateDiff = dateDiff + hours  + ":" + mins + " am";
		    	}
		    }
		}
		return dateDiff;
	}
	
    /** 
     * @param oldTime 较小的时间 
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比) 
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天. 
     * @throws ParseException 转换异常 
     */  
	public static int isYeaterday(Date oldTime,Date newTime) throws ParseException{  
        if(newTime==null)
        {  
            newTime=new Date();  
        }  
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String todayStr = format.format(newTime);  
        Date today = format.parse(todayStr);  
        
        // 判断到底是哪天
        if((today.getTime()-oldTime.getTime())>0 && (today.getTime()-oldTime.getTime())<=86400000) 
        {  
            // 昨天
            return 0;  
        }  
        else if((today.getTime()-oldTime.getTime())<=0)
        { 
        	// 至少是今天  
            return -1;  
        }  
        else
        { 
        	// 至少是前天  
            return 1;  
        }  
          
    }  
    
    /**
     * 将月份转换成英文月份
     * @return
     */
    public static Map<Integer, String> getMonths()
    {
    	Map<Integer, String> mothsMap = new HashMap<Integer, String>();
    	mothsMap.put(1, "January");
    	mothsMap.put(2, "February");
    	mothsMap.put(3, "March");
    	mothsMap.put(4, "April");
    	mothsMap.put(5, "May");
    	mothsMap.put(6, "June");
    	mothsMap.put(7, "July");
    	mothsMap.put(8, "August");
    	mothsMap.put(9, "September");
    	mothsMap.put(10, "October");
    	mothsMap.put(11, "November");
    	mothsMap.put(12, "December");
    	return mothsMap;
    }
    
    /**
     * 获取日期中的月
     * @param date 日期
     * @return 月份
     */
    public static String getMonth(Date date){
        DateFormat f_month=new SimpleDateFormat("MM");
        return f_month.format(date).toString();
    }

    /**
     * 获取日期中天
     * @param date 日期
     * @return 天
     */
    public static String getDay(Date date){
        DateFormat f_day=new SimpleDateFormat("dd");
        return f_day.format(date).toString();
    } 
    
    /**
     * 获取日期中时
     * @param date 日期
     * @return 时
     */
    public static String getHour(Date date){
        DateFormat f_day=new SimpleDateFormat("HH");
        return f_day.format(date).toString();
    }
    
    /**
     * 获取日期中分
     * @param date 日期
     * @return 分
     */
    public static String getMin(Date date){
        DateFormat f_day=new SimpleDateFormat("mm");
        return f_day.format(date).toString();
    }
    
    /**
     * 根据日期添加小时
     * @param dateStr
     * @param hours
     * @return
     */
    public static String addHours(Date date, int hours)
    {
////    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	
//    	sdf.format(date)
//    	Date date=null;
//    	try {
//    	date = sdf.parse(dateStr);
//    	} catch (ParseException e) {
//    	// TODO 自动生成 catch 块
//    	e.printStackTrace();
//    	}
    	Calendar ca=Calendar.getInstance();
    	ca.setTime(date);
    	ca.add(Calendar.HOUR_OF_DAY, hours);
    	
    	return sdf.format(ca.getTime());
    }
    
    /**
     * date转timestamp(父类不能直接转成子类，通过string来转换)
     * @param date
     * @return
     */
    public static Timestamp dateToTimeStamp(Date date)
    {
    	String time = sdf.format(date); 
    	Timestamp ts = new Timestamp(System.currentTimeMillis());   
    	try 
    	{   
    		ts = Timestamp.valueOf(time);   
        } 
    	catch (Exception e) 
    	{   
            e.printStackTrace();   
        } 
    	return ts;
    }
    
    public static String sdfSimpleFormat(Date data){
		return sdfSimple.format(data);
	}
    
    /**
     * 秒值转换成 时:分:秒格式
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    
    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
