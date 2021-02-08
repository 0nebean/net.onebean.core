package net.onebean.util;

import net.onebean.core.base.YesterdayEnum;
import net.onebean.core.form.Parse;

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
 * <p>
 * 日期类型的工具类，该类均为静态方法，直接调用
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
     * <p>
     * 日期格式化模式，使用此模式将日期格式化为“2012-10-08”，一般用于日期类型数据格式化
     */
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String PATTERNS_YYYY_MM_DD = "yyyy年MM月dd日";

    /**
     * 日期格式化模式（时间类型数据）
     * <p>
     * 日期格式化模式，使用此模式将日期格式化为“2012-10-08 10:10:08”，一般用于时间类型数据格式化
     */
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERNS_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String PATTERNS_HH_MM = "HH:mm";

    public static final String PATTERNS_MM_DD = "MM-dd";

    /**
     * 根据特定模式，将字符串型日期对象解析成Date对象
     *
     * @param source  要解析的字符串
     * @param pattern 解析模式，默认为{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}
     * @return 解析结果
     * @throws ParseException 如果要解析的字符串格式不匹配，则抛出此异常
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
     * @param source  要格式化的日期对象
     * @param pattern 格式化模式，默认为{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}
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
     * @param source 日期对象
     * @return 格式化值
     */
    public static String format(Date source) {
        return format(source, PATTERN_YYYY_MM_DD);
    }

    /**
     * 格式化年月日
     *
     * @param source 模板
     * @return 字符串时间
     */
    public static String formats(Date source) {
        return format(source, PATTERNS_YYYY_MM_DD);
    }

    /**
     * 将日期对象格式化成特定格式字符串
     *
     * @param source        日期对象
     * @param isFullPattern 格式化类型，true{@value #PATTERN_YYYY_MM_DD_HH_MM_SS}类型，false
     *                      {@value #PATTERN_YYYY_MM_DD}类型
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
     * @return 时间
     */
    public static Date getServerDate() {
        return new Date();
    }

    /**
     * 时间转字符串
     *
     * @param date 时间
     * @return 字符串
     */
    public static synchronized String dateToString(Date date) {
        return (date == null || "".equals(date.toString())) ? "" : sdf
                .format(date);
    }

    /**
     * 日期转字符串
     *
     * @param date 时间
     * @return 字符串
     */
    public static synchronized String dateToStringSimple(Date date) {

        return (date == null || "".equals(date.toString())) ? "" : sdfSimple
                .format(date);

    }

    /**
     * String转Timestamp
     *
     * @param str 时间字符串
     * @return 时间戳
     * @throws ParseException 转换异常
     */
    public static Timestamp strToTimeStamp(String str) throws ParseException {
        Date date11 = sdfSimple.parse(str);

        String time = sdfSimple.format(date11);

        Timestamp ts = Timestamp.valueOf(time);

        return ts;
    }

    /**
     * 日历转时间戳
     *
     * @param calendar 日历
     * @return 时间戳
     */
    public static Timestamp calendar2Timestamp(Calendar calendar) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(calendar.getTime());
            return Timestamp.valueOf(time);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String转Timestamp
     *
     * @param str 字符串
     * @return 时间戳
     */
    public static Timestamp stringToTimeStamp(String str) {
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
     * 获得当前时间
     * @return 当前时间
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获得当前时间
     * @return 当前时间
     */
    public static String getCurrentDateStr() {
        return sdf.format(new Date());
    }

    /**
     * 获得当前时间
     *
     * @return 当前时间
     */
    public static Date getCurrentDateTime() {
        return new Date();
    }

    /**
     * 获得当前时间
     *
     * @return 当前时间
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }


    /**
     * 获得当前日期
     *
     * @return 当前时间字符串
     */
    public static String getCurrentDateToDay() {

        return sdfSimpleymd.format(new Date());

    }

    /**
     * 获得当前时间(包含毫秒)
     *
     * @return 当前时间字符串
     */
    public static String getDetailTime() {

        return sdfDetail.format(new Date());

    }

    /**
     * 自然日推移后的实践
     *
     * @param date  初试时间
     * @param value 偏移的日子
     * @return 偏移后的时间
     */
    public static synchronized Date dayOffSet(Date date, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

    /**
     * 字符串转时间
     *
     * @param str 字符串
     * @return 时间
     * @throws ParseException 转换异常
     */
    public static synchronized Date strToDate(String str) throws ParseException {
        return sdfSimple.parse(str);
    }

    /**
     * 字符串转时间
     *
     * @param str 字符串
     * @return 时间
     * @throws ParseException 转换异常
     */
    public static synchronized Date stringToDate(String str) throws ParseException {
        return sdf.parse(str);
    }


    /**
     * 按 {@value #PATTERN_YYYY_MM_DD}格式格式化日期对象
     *
     * @param obj 日期对象
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
     * @param millis 毫秒数
     * @return 时间戳字符串
     */
    public static String getDateTimeByMillisecond(Long millis) {
        Date date = new Date(millis);
        SimpleDateFormat format = new SimpleDateFormat(
                PATTERN_YYYY_MM_DD_HH_MM_SS);
        return format.format(date);
    }

    /**
     * 把毫秒转换为年+月+日+小时+分+秒
     *
     * @param millis 毫秒数
     * @return 时间戳字符串
     */
    public static String convertMillis(Long millis) {
        /*
         * String y = "年"; String m = "月"; String d = "天"; String h = "小时";
         * String mi = "分"; String ms = "秒";
         */
        if (millis <= 0L)
            return "";
        Long d = 24 * 60 * 60 * 1000L;
        long h = 60 * 60 * 1000L;
        long mi = 60 * 1000L;
        long ms = 1000L;
        long day = millis / d;
        long hour = millis % d / h;
        long min = millis % d % h / mi;
        long sec = millis % d % h % mi / ms;
        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }

    /**
     * 计算两个日期相差的天数 日期类型需为：yyyy-MM-dd
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 差值
     * @throws ParseException 转换异常
     */
    public static int divideDay(String startTime, String endTime)
            throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = ft.parse(endTime);
        Date date2 = ft.parse(startTime);
        long quot = date1.getTime() - date2.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return (int) quot;
    }

    /**
     * 计算两个日期相差的天数 日期类型需为：yyyy-MM-dd
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 差值
     */
    public static int divideDay(Date startTime, Date endTime) {
        long quot = endTime.getTime() - startTime.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return (int) quot;
    }

    /**
     * 得到下一个日期
     *
     * @param date 时间
     * @param day  天数
     * @return 时间
     */
    public static Date nextDays(Date date, long day) {

        long time = date.getTime();
        time += 1000 * 24 * 60 * 60 * day;
        return new Date(time);
    }

    /**
     * 获取当前星期几
     *
     * @param pTime 时间戳
     * @return 星期几
     * @throws ParseException 转换异常
     */
    public static String dayForWeek(String pTime) throws ParseException {
        String[] weeks = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return weeks[dayForWeek - 1];
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException 转换异常
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
     * 计算从20200701到现在的天数
     *
     * @return 相差天数
     */
    public static int daysBetween20200701() {
        Calendar date20200701 = Calendar.getInstance();
        date20200701.set(Calendar.YEAR, 2020);
        date20200701.set(Calendar.MONTH, 6);// 月份从0 1 2 3 计算
        date20200701.set(Calendar.DAY_OF_MONTH, 1);
        long time1 = date20200701.getTimeInMillis();
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
     *
     * @param date 时间戳
     * @return 天数
     */
    public static int daysBetweenToDay(Timestamp date) {
        Calendar date20200701 = Calendar.getInstance();
        int press_date = daysBetween20200701();
        date20200701.setTimeInMillis(date.getTime());
        int reDate = DateUtils.dayInDate20200701(date20200701.get(Calendar.YEAR), date20200701.get(Calendar.MONTH), date20200701.get(Calendar.DATE));
        return press_date - reDate;
    }

    /**
     * 传入一个pressdate 获取到对应的日期
     *
     * @param pressDate 天数数字
     * @return 时间对象
     */
    public static Date dayIn20200701(Integer pressDate) {
        Calendar date20200701 = Calendar.getInstance();
        date20200701.set(Calendar.YEAR, 2020);
        date20200701.set(Calendar.MONTH, 6);
        date20200701.set(Calendar.DAY_OF_MONTH, 1);
        date20200701.add(Calendar.DATE, pressDate);
        return date20200701.getTime();
    }


    /**
     * 给定时间计算从20200701到今天的整数值
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 差值
     */
    public static int dayInDate20200701(int year, int month, int day) {
        Calendar date20200701 = Calendar.getInstance();
        date20200701.set(Calendar.YEAR, 2020);
        date20200701.set(Calendar.MONTH, 6);
        date20200701.set(Calendar.DAY_OF_MONTH, 1);
        long time1 = date20200701.getTimeInMillis();
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.YEAR, year);
        cd.set(Calendar.MONTH, month - 1);
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

    public static String getNowYyyyMMdd() {
        return sdf_yyyyMMdd.format(Calendar.getInstance().getTime());
    }

    public static String getNowYyyy_MM_dd() {
        return sdfSimple.format(Calendar.getInstance().getTime());
    }

    public static String getDateStrByTimestamp(Timestamp timestamp) {
        return sdf.format(getDateByTimestamp(timestamp));
    }

    public static Date getDateByTimestamp(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    public static String getDateStrByTimestampNextDay(Timestamp timestamp) {
        return sdf.format(getDateByTimestampNextDay(timestamp));
    }


    public static Date getDateByTimestampNextDay(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar.getTime();
    }

    public static String getUserSetYyyyMMdd(int press_date) {
        return sdf_yyyyMMdd.format(dayIn20200701(press_date));
    }

    public static String getMonth_ch(String month) {
        String[] month_ch = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月",
                "九月", "十月", "十一月", "十二月"};
        return month_ch[Integer.parseInt(month) - 1];
    }

    /**
     * 获取指定天数以前的字符串表示
     *
     * @param before 天数
     * @return 字符串
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
     *
     * @param date 时间
     * @return 字符串
     */
    public static String formatDate(Date date) {
        if (sdfSimple.format(date).equalsIgnoreCase(sdfSimple.format(new Date()))) {
            return format(date, PATTERNS_HH_MM);
        }
        return format(date, PATTERNS_MM_DD);
    }

    /**
     * 格式化时间
     *
     * @param date 时间
     * @return 字符串
     */
    public static String parse_yyyy_MM_dd_HH_mm(Date date) {
        return sdf_yyyyMMddHHmm.format(date);
    }

    public static String getYestoday(String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyyMMdd";
        }
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    /**
     * 将文章发表时间转换成几分钟前、几秒前、几小时前等等提示语
     *
     * @param create_time 创建时间
     * @return String
     */
    public static String setDateDiff(Timestamp create_time) {
        // 获取当前时间并转换为毫秒
        Date now = new Date();
        String dateDiff = null;

        // 如果创建时间不为空才转换
        if (StringUtils.isNotEmpty(create_time)) {
            int isYeaterDay = 0;
            // 判断是否是昨天
            isYeaterDay = isYesterday(create_time, now);

            // 获取文章创建时间
            long createTime = create_time.getTime();
            String hours = getHour(create_time);
            int pmHours = Integer.valueOf(getHour(create_time));
            String mins = getMin(create_time);
            String month = getMonth(create_time);

            // 今天显示几分钟，几小时，几秒等等
            if (isYeaterDay == -1) {

                long l = now.getTime() - createTime;
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
                long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                if (hour > 0) {
                    if (hour > 1) {
                        dateDiff = hour + " hrs ";
                    } else {
                        dateDiff = hour + " hr ";
                    }

                } else if (min > 0) {
                    if (min > 1) {
                        dateDiff = min + " mins ";
                    } else {
                        dateDiff = min + " min ";
                    }
                } else if (s > 0) {
                    if (s > 1) {
                        dateDiff = s + " secs ";
                    } else {
                        dateDiff = s + " sec ";
                    }
                }
            } else if (isYeaterDay == 0) {
                // 昨天，显示Yesterday at 11:50 am  Yesterday at 2:50 pm
                dateDiff = "Yesterday at ";

                // 如果超过11点，说明是下午了
                if (pmHours > 11) {
                    // 如果超过12点，减去12来表示
                    if (pmHours > 12) {
                        pmHours = pmHours - 12;
                    }
                    dateDiff = dateDiff + pmHours + ":" + mins + " pm";
                } else {
                    dateDiff = dateDiff + hours + ":" + mins + " am";
                }
            } else if (isYeaterDay == 1) {
                // 更早显示日期：December 25 at 11:50 am
                dateDiff = getMonths().get(Integer.valueOf(month)) + " " + getDay(create_time) + " at ";

                // 如果超过11点，说明是下午了
                if (pmHours > 11) {
                    // 如果超过12点，减去12来表示
                    if (pmHours > 12) {
                        pmHours = pmHours - 12;
                    }
                    dateDiff = dateDiff + pmHours + ":" + mins + " pm";
                } else {
                    dateDiff = dateDiff + hours + ":" + mins + " am";
                }
            }
        }
        return dateDiff;
    }



    /**
     * 相差的小时
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的小时
     */
    public static int aHoursB(Date target, Date now) {
        long l = now.getTime() - target.getTime();
        return Parse.toInt(l / (60 * 60 * 1000));
    }

    /**
     * 相差的小时
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的小时
     */
    public static int aHoursB(Timestamp target, Timestamp now) {
        Date oldTimeDate = getDateByTimestamp(target);
        Date newTimeDate = getDateByTimestamp(now);
        return aHoursB(oldTimeDate, newTimeDate);
    }

    /**
     * 相差的分钟
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的分钟
     */
    public static long aMinutesB(Date target, Date now) {
        long l = now.getTime() - target.getTime();
        return l / (60 * 1000);
    }

    /**
     * 相差的分钟
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的分钟
     */
    public static long aMinutesB(Timestamp target, Timestamp now) {
        Date oldTimeDate = getDateByTimestamp(target);
        Date newTimeDate = getDateByTimestamp(now);
        return aMinutesB(oldTimeDate, newTimeDate);
    }

    /**
     * 相差的秒数
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的秒数
     */
    public static long aSecondsB(Date target, Date now) {
        long l = now.getTime() - target.getTime();
        return l / (1000);
    }

    /**
     * 相差的秒数
     *
     * @param target 目标时间
     * @param now    当下
     * @return 相差的秒数
     */
    public static long aSecondsB(Timestamp target, Timestamp now) {
        Date oldTimeDate = getDateByTimestamp(target);
        Date newTimeDate = getDateByTimestamp(now);
        return aSecondsB(oldTimeDate, newTimeDate);
    }

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
     * @throws ParseException 转换异常
     */
    public static int isYesterday(Timestamp oldTime, Timestamp newTime) {
        Date oldTimeDate = getDateByTimestamp(oldTime);
        Date newTimeDate = getDateByTimestamp(newTime);
        return isYesterday(oldTimeDate, newTimeDate);
    }

    /**
     * @param oldTime 较小的时间
     * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比)
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
     * @throws ParseException 转换异常
     */
    public static int isYesterday(Date oldTime, Date newTime) {
        if (newTime == null) {
            newTime = new Date();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = null;
        try {
            today = format.parse(todayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 判断到底是哪天
        if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
            // 昨天
            return YesterdayEnum.YESTERDAY.getKey();
        } else if ((today.getTime() - oldTime.getTime()) <= 0) {
            // 至少是今天
            return YesterdayEnum.SAME_DAY.getKey();
        } else {
            // 至少是前天
            return YesterdayEnum.LEAST_THE_DAY_BEFORE_YESTERDAY.getKey();
        }

    }

    /**
     * 将月份转换成英文月份
     *
     * @return map
     */
    public static Map<Integer, String> getMonths() {
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
     *
     * @param date 日期
     * @return 月份
     */
    public static String getMonth(Date date) {
        DateFormat f_month = new SimpleDateFormat("MM");
        return f_month.format(date).toString();
    }

    /**
     * 获取日期中天
     *
     * @param date 日期
     * @return 天
     */
    public static String getDay(Date date) {
        DateFormat f_day = new SimpleDateFormat("dd");
        return f_day.format(date).toString();
    }

    /**
     * 获取日期中时
     *
     * @param date 日期
     * @return 时
     */
    public static String getHour(Date date) {
        DateFormat f_day = new SimpleDateFormat("HH");
        return f_day.format(date).toString();
    }

    /**
     * 获取日期中分
     *
     * @param date 日期
     * @return 分
     */
    public static String getMin(Date date) {
        DateFormat f_day = new SimpleDateFormat("mm");
        return f_day.format(date);
    }


    /**
     * 根据日期添加分钟
     * @param date  时间
     * @param minutes 分钟
     * @return Timestamp
     */
    public static Timestamp addMinutes(Date date, int minutes) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MINUTE, minutes);
        return dateToTimeStamp(ca.getTime());
    }

    /**
     * 根据日期添加小时
     * @param date  时间
     * @param hours 小时
     * @return Timestamp
     */
    public static Timestamp addHours(Date date, int hours) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, hours);
        return dateToTimeStamp(ca.getTime());
    }

    /**
     * 获取延时的时间
     *
     * @param hours 小时
     * @return 字符串
     */
    public static Timestamp getDelayTime(int hours) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(getCurrentDateTime());
        ca.add(Calendar.HOUR_OF_DAY, hours);
        return dateToTimeStamp(ca.getTime());
    }

    /**
     * 获取一天后的时间
     *
     * @return 字符串
     */
    public static Timestamp getDelay0neDayTime() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(getCurrentDateTime());
        ca.add(Calendar.HOUR_OF_DAY, 24);
        return dateToTimeStamp(ca.getTime());
    }

    /**
     * date转timestamp(父类不能直接转成子类，通过string来转换)
     *
     * @param date 时间
     * @return 时间戳
     */
    public static Timestamp dateToTimeStamp(Date date) {
        String time = sdf.format(date);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
    }

    public static String sdfSimpleFormat(Date data) {
        return sdfSimple.format(data);
    }

    /**
     * 秒值转换成 时:分:秒格式
     *
     * @param time 秒值
     * @return 字符串
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

    public static final String getTimeStamp() {
        Date now = new Date();
        return String.valueOf(now.getTime());
    }


}
