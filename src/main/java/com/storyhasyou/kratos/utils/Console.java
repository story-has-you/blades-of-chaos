package com.storyhasyou.kratos.utils;


import com.storyhasyou.kratos.toolkit.StrFormatter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * 命令行（控制台）工具方法类<br>
 * 此类主要针对{@link System#out} 和 {@link System#err} 做封装。
 *
 * @author fangxi
 */
public class Console {

    // --------------------------------------------------------------------------------- Log

    /**
     * 同 System.out.println()方法，打印控制台日志
     */
    public static void log() {
        out.println();
    }

    /**
     * 同 System.out.println()方法，打印控制台日志<br>
     * 如果传入打印对象为{@link Throwable}对象，那么同时打印堆栈
     *
     * @param obj 要打印的对象
     */
    public static void log(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            log(e, e.getMessage());
        } else {
            log("{}", obj);
        }
    }

    /**
     * 同 System.out.print()方法，打印控制台日志
     *
     * @param obj 要打印的对象
     * @since 3.3.1
     */
    public static void print(Object obj) {
        print("{}", obj);
    }

    /**
     * 打印进度条
     *
     * @param showChar 进度条提示字符，例如“#”
     * @param len      打印长度
     * @since 4.5.6
     */
    public static void printProgress(char showChar, int len) {
        print("{}{}", '\r', repeat(showChar, len));
    }

    /**
     * 打印进度条
     *
     * @param showChar 进度条提示字符，例如“#”
     * @param totalLen 总长度
     * @param rate     总长度所占比取值0~1
     * @since 4.5.6
     */
    public static void printProgress(char showChar, int totalLen, double rate) {
        Assert.isTrue(rate >= 0 && rate <= 1, "Rate must between 0 and 1 (both include)");
        printProgress(showChar, (int) (totalLen * rate));
    }

    /**
     * 同 System.out.println()方法，打印控制台日志
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void log(String template, Object... values) {
        log(null, template, values);
    }

    /**
     * 同 System.out.print()方法，打印控制台日志
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     * @since 3.3.1
     */
    public static void print(String template, Object... values) {
        out.print(format(template, values));
    }

    /**
     * 同 System.out.println()方法，打印控制台日志
     *
     * @param t        异常对象
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void log(Throwable t, String template, Object... values) {
        out.println(format(template, values));
        if (null != t) {
            t.printStackTrace();
            out.flush();
        }
    }

    // --------------------------------------------------------------------------------- Error

    /**
     * 同 System.err.println()方法，打印控制台日志
     */
    public static void error() {
        err.println();
    }

    /**
     * 同 System.err.println()方法，打印控制台日志
     *
     * @param obj 要打印的对象
     */
    public static void error(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            error(e, e.getMessage());
        } else {
            error("{}", obj);
        }
    }

    /**
     * 同 System.err.println()方法，打印控制台日志
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void error(String template, Object... values) {
        error(null, template, values);
    }

    /**
     * 同 System.err.println()方法，打印控制台日志
     *
     * @param t        异常对象
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    public static void error(Throwable t, String template, Object... values) {
        err.println(format(template, values));
        if (null != t) {
            t.printStackTrace(err);
            err.flush();
        }
    }

    // --------------------------------------------------------------------------------- in

    /**
     * 创建从控制台读取内容的{@link Scanner}
     *
     * @return {@link Scanner}
     * @since 3.3.1
     */
    public static Scanner scanner() {
        return new Scanner(System.in);
    }

    /**
     * 读取用户输入的内容（在控制台敲回车前的内容）
     *
     * @return 用户输入的内容
     * @since 3.3.1
     */
    public static String input() {
        return scanner().next();
    }

    // --------------------------------------------------------------------------------- console lineNumber

    /**
     * 返回当前位置+行号 (不支持Lambda、内部类、递归内使用)
     *
     * @return 返回当前行号
     * @author dahuoyzs
     * @since 5.2.5
     */
    public static String where() {
        final StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        final String className = stackTraceElement.getClassName();
        final String methodName = stackTraceElement.getMethodName();
        final String fileName = stackTraceElement.getFileName();
        final Integer lineNumber = stackTraceElement.getLineNumber();
        return String.format("%s.%s(%s:%s)", className, methodName, fileName, lineNumber);
    }

    /**
     * 返回当前行号 (不支持Lambda、内部类、递归内使用)
     *
     * @return 返回当前行号
     * @since 5.2.5
     */
    public static Integer lineNumber() {
        return new Throwable().getStackTrace()[1].getLineNumber();
    }

    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (ArrayUtils.isEmpty(params) || StringUtils.isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 重复某个字符
     *
     * @param c     被重复的字符
     * @param count 重复的数目，如果小于等于0则返回""
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        if (count <= 0) {
            return "";
        }

        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }
}
