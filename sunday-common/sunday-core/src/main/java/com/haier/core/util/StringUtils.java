package com.haier.core.util;

import com.haier.core.constant.CommonConstants;
import com.haier.core.enums.PatternEnum;
import com.haier.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.AntPathMatcher;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 21:46
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';


    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }


    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link) {
        return StringUtils.startsWithAny(link, CommonConstants.HTTP, CommonConstants.HTTPS);
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    public static final String SPACE = " ";
    public static final String TAB = "	";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String DASHED = "-";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String BRACKET_START = "[";
    public static final String BRACKET_END = "]";
    public static final String COLON = ":";

    /**
     * 将var2参数替换成var1中出现的{}
     * <pre>
     * stringFormat("text{}", "a") = texta
     * stringFormat("text,{},{}", "a", "b") = text,a,b
     * stringFormat("text{}", Arrays.asList("1", "2", "3")) = text[1, 2, 3]
     * stringFormat("text\\{}", "a") = text{}
     * </pre>
     *
     * @param var1 字符串
     * @param var2 参数
     * @return
     */
    public static String stringFormat(String var1, Object... var2) {
        return MessageFormatter.arrayFormat(var1, var2).getMessage();
    }

    /**
     * EMOJI encode
     *
     * @param content 带有emoji表情的字符串
     * @return 将字符串中的emoji表情编码为UTF-8
     */
    public static String emojiEncode(String content) {
        try {
            return RegexUtils.replaceAll(content, PatternEnum.EMOJI.getPattern(), a -> "[[EMOJI:" + URLEncoder.encode(a, StandardCharsets.UTF_8) + "]]");
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BaseException("emoji filter error");
    }


    /**
     * EMOJI decode
     *
     * @param content 字符串
     * @return 将带有编码后emoji的字符串 解码为emoji
     */
    public static String emojiDecode(String content) {
        try {
            return RegexUtils.replaceAll(content, PatternEnum.EMOJI_DECODE.getPattern(), a -> URLDecoder.decode(a, StandardCharsets.UTF_8), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BaseException("emoji decode error");
    }


    public static class MessageFormatter {
        static final char DELIM_START = '{';
        static final char DELIM_STOP = '}';
        static final String DELIM_STR = "{}";
        private static final char ESCAPE_CHAR = '\\';

        static final Throwable getThrowableCandidate(Object[] argArray) {
            if (argArray == null || argArray.length == 0) {
                return null;
            }

            final Object lastEntry = argArray[argArray.length - 1];
            if (lastEntry instanceof Throwable) {
                return (Throwable) lastEntry;
            }
            return null;
        }

        final public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
            Throwable throwableCandidate = getThrowableCandidate(argArray);
            Object[] args = argArray;
            if (throwableCandidate != null) {
                args = trimmedCopy(argArray);
            }
            return arrayFormat(messagePattern, args, throwableCandidate);
        }

        private static Object[] trimmedCopy(Object[] argArray) {
            if (argArray == null || argArray.length == 0) {
                throw new IllegalStateException("non-sensical empty or null argument array");
            }
            final int trimemdLen = argArray.length - 1;
            Object[] trimmed = new Object[trimemdLen];
            System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
            return trimmed;
        }

        final public static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray, Throwable throwable) {

            if (messagePattern == null) {
                return new FormattingTuple(null, argArray, throwable);
            }

            if (argArray == null) {
                return new FormattingTuple(messagePattern);
            }

            int i = 0;
            int j;
            // use string builder for better multicore performance
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            int L;
            for (L = 0; L < argArray.length; L++) {

                j = messagePattern.indexOf(DELIM_STR, i);

                if (j == -1) {
                    // no more variables
                    if (i == 0) { // this is a simple string
                        return new FormattingTuple(messagePattern, argArray, throwable);
                    } else { // add the tail string which contains no variables and return
                        // the result.
                        sbuf.append(messagePattern, i, messagePattern.length());
                        return new FormattingTuple(sbuf.toString(), argArray, throwable);
                    }
                } else {
                    if (isEscapedDelimeter(messagePattern, j)) {
                        if (!isDoubleEscaped(messagePattern, j)) {
                            L--; // DELIM_START was escaped, thus should not be incremented
                            sbuf.append(messagePattern, i, j - 1);
                            sbuf.append(DELIM_START);
                            i = j + 1;
                        } else {
                            // The escape character preceding the delimiter start is
                            // itself escaped: "abc x:\\{}"
                            // we have to consume one backward slash
                            sbuf.append(messagePattern, i, j - 1);
                            deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                            i = j + 2;
                        }
                    } else {
                        // normal case
                        sbuf.append(messagePattern, i, j);
                        deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object[], Object>());
                        i = j + 2;
                    }
                }
            }
            // append the characters following the last {} pair.
            sbuf.append(messagePattern, i, messagePattern.length());
            return new FormattingTuple(sbuf.toString(), argArray, throwable);
        }

        final static boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {

            if (delimeterStartIndex == 0) {
                return false;
            }
            char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
            if (potentialEscape == ESCAPE_CHAR) {
                return true;
            } else {
                return false;
            }
        }

        final static boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
            if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR) {
                return true;
            } else {
                return false;
            }
        }

        // special treatment of array values was suggested by 'lizongbo'
        private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
            if (o == null) {
                sbuf.append("null");
                return;
            }
            if (!o.getClass().isArray()) {
                safeObjectAppend(sbuf, o);
            } else {
                // check for primitive array types because they
                // unfortunately cannot be cast to Object[]
                if (o instanceof boolean[]) {
                    booleanArrayAppend(sbuf, (boolean[]) o);
                } else if (o instanceof byte[]) {
                    byteArrayAppend(sbuf, (byte[]) o);
                } else if (o instanceof char[]) {
                    charArrayAppend(sbuf, (char[]) o);
                } else if (o instanceof short[]) {
                    shortArrayAppend(sbuf, (short[]) o);
                } else if (o instanceof int[]) {
                    intArrayAppend(sbuf, (int[]) o);
                } else if (o instanceof long[]) {
                    longArrayAppend(sbuf, (long[]) o);
                } else if (o instanceof float[]) {
                    floatArrayAppend(sbuf, (float[]) o);
                } else if (o instanceof double[]) {
                    doubleArrayAppend(sbuf, (double[]) o);
                } else {
                    objectArrayAppend(sbuf, (Object[]) o, seenMap);
                }
            }
        }

        private static void safeObjectAppend(StringBuilder sbuf, Object o) {
            try {
                String oAsString = o.toString();
                sbuf.append(oAsString);
            } catch (Throwable t) {
                System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
                System.err.println("Reported exception:");
                t.printStackTrace();
                sbuf.append("[FAILED toString()]");
            }

        }

        private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
            sbuf.append('[');
            if (!seenMap.containsKey(a)) {
                seenMap.put(a, null);
                final int len = a.length;
                for (int i = 0; i < len; i++) {
                    deeplyAppendParameter(sbuf, a[i], seenMap);
                    if (i != len - 1)
                        sbuf.append(", ");
                }
                // allow repeats in siblings
                seenMap.remove(a);
            } else {
                sbuf.append("...");
            }
            sbuf.append(']');
        }

        private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void charArrayAppend(StringBuilder sbuf, char[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void intArrayAppend(StringBuilder sbuf, int[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void longArrayAppend(StringBuilder sbuf, long[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }

        private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
            sbuf.append('[');
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                sbuf.append(a[i]);
                if (i != len - 1)
                    sbuf.append(", ");
            }
            sbuf.append(']');
        }
    }

    @Data
    @AllArgsConstructor
    public static class FormattingTuple {

        /**
         * 消息体
         */
        private String message;

        /**
         * 消息拼接的参数
         */
        private Object[] argArray;

        /**
         * 异常
         */
        private Throwable throwable;


        public FormattingTuple(String message) {
            this(message, null, null);
        }
    }

}
