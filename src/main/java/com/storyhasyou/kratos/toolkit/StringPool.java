package com.storyhasyou.kratos.toolkit;

/**
 * Copy to jodd.util
 * <p>
 * Pool of <code>String</code> constants to prevent repeating of
 * hard-coded <code>String</code> literals in the code.
 * Due to fact that these are <code>public static final</code>
 * they will be inlined by java compiler and
 * reference to this class will be dropped.
 * There is <b>no</b> performance gain of using this pool.
 * Read: https://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10.5
 * <ul>
 * <li>Literal strings within the same class in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in the same package represent references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in different packages likewise represent references to the same <code>String</code> object.</li>
 * <li>Strings computed by constant expressions are computed at compile time and then treated as if they were literals.</li>
 * <li>Strings computed by concatenation at run time are newly created and therefore distinct.</li>
 * </ul>
 */
public interface StringPool {

    /**
     * The constant AMPERSAND.
     */
    String AMPERSAND = "&";
    /**
     * The constant AND.
     */
    String AND = "and";
    /**
     * The constant AT.
     */
    String AT = "@";
    /**
     * The constant ASTERISK.
     */
    String ASTERISK = "*";
    /**
     * The constant STAR.
     */
    String STAR = ASTERISK;
    /**
     * The constant BACK_SLASH.
     */
    String BACK_SLASH = "\\";
    /**
     * The constant COLON.
     */
    String COLON = ":";
    /**
     * The constant COMMA.
     */
    String COMMA = ",";
    /**
     * The constant DASH.
     */
    String DASH = "-";
    /**
     * The constant DOLLAR.
     */
    String DOLLAR = "$";
    /**
     * The constant DOT.
     */
    String DOT = ".";
    /**
     * The constant DOTDOT.
     */
    String DOTDOT = "..";
    /**
     * The constant DOT_CLASS.
     */
    String DOT_CLASS = ".class";
    /**
     * The constant DOT_JAVA.
     */
    String DOT_JAVA = ".java";
    /**
     * The constant DOT_XML.
     */
    String DOT_XML = ".xml";
    /**
     * The constant EMPTY.
     */
    String EMPTY = "";
    /**
     * The constant EQUALS.
     */
    String EQUALS = "=";
    /**
     * The constant FALSE.
     */
    String FALSE = "false";
    /**
     * The constant SLASH.
     */
    String SLASH = "/";
    /**
     * The constant HASH.
     */
    String HASH = "#";
    /**
     * The constant HAT.
     */
    String HAT = "^";
    /**
     * The constant LEFT_BRACE.
     */
    String LEFT_BRACE = "{";
    /**
     * The constant LEFT_BRACKET.
     */
    String LEFT_BRACKET = "(";
    /**
     * The constant LEFT_CHEV.
     */
    String LEFT_CHEV = "<";
    /**
     * The constant DOT_NEWLINE.
     */
    String DOT_NEWLINE = ",\n";
    /**
     * The constant NEWLINE.
     */
    String NEWLINE = "\n";
    /**
     * The constant N.
     */
    String N = "n";
    /**
     * The constant NO.
     */
    String NO = "no";
    /**
     * The constant NULL.
     */
    String NULL = "null";
    /**
     * The constant OFF.
     */
    String OFF = "off";
    /**
     * The constant ON.
     */
    String ON = "on";
    /**
     * The constant PERCENT.
     */
    String PERCENT = "%";
    /**
     * The constant PIPE.
     */
    String PIPE = "|";
    /**
     * The constant PLUS.
     */
    String PLUS = "+";
    /**
     * The constant QUESTION_MARK.
     */
    String QUESTION_MARK = "?";
    /**
     * The constant EXCLAMATION_MARK.
     */
    String EXCLAMATION_MARK = "!";
    /**
     * The constant QUOTE.
     */
    String QUOTE = "\"";
    /**
     * The constant RETURN.
     */
    String RETURN = "\r";
    /**
     * The constant TAB.
     */
    String TAB = "\t";
    /**
     * The constant RIGHT_BRACE.
     */
    String RIGHT_BRACE = "}";
    /**
     * The constant RIGHT_BRACKET.
     */
    String RIGHT_BRACKET = ")";
    /**
     * The constant RIGHT_CHEV.
     */
    String RIGHT_CHEV = ">";
    /**
     * The constant SEMICOLON.
     */
    String SEMICOLON = ";";
    /**
     * The constant SINGLE_QUOTE.
     */
    String SINGLE_QUOTE = "'";
    /**
     * The constant BACKTICK.
     */
    String BACKTICK = "`";
    /**
     * The constant SPACE.
     */
    String SPACE = " ";
    /**
     * The constant TILDA.
     */
    String TILDA = "~";
    /**
     * The constant LEFT_SQ_BRACKET.
     */
    String LEFT_SQ_BRACKET = "[";
    /**
     * The constant RIGHT_SQ_BRACKET.
     */
    String RIGHT_SQ_BRACKET = "]";
    /**
     * The constant TRUE.
     */
    String TRUE = "true";
    /**
     * The constant UNDERSCORE.
     */
    String UNDERSCORE = "_";
    /**
     * The constant UTF_8.
     */
    String UTF_8 = "UTF-8";
    /**
     * The constant US_ASCII.
     */
    String US_ASCII = "US-ASCII";
    /**
     * The constant ISO_8859_1.
     */
    String ISO_8859_1 = "ISO-8859-1";
    /**
     * The constant Y.
     */
    String Y = "y";
    /**
     * The constant YES.
     */
    String YES = "yes";
    /**
     * The constant ONE.
     */
    String ONE = "1";
    /**
     * The constant ZERO.
     */
    String ZERO = "0";
    /**
     * The constant DOLLAR_LEFT_BRACE.
     */
    String DOLLAR_LEFT_BRACE = "${";
    /**
     * The constant HASH_LEFT_BRACE.
     */
    String HASH_LEFT_BRACE = "#{";
    /**
     * The constant CRLF.
     */
    String CRLF = "\r\n";

    /**
     * The constant HTML_NBSP.
     */
    String HTML_NBSP = "&nbsp;";
    /**
     * The constant HTML_AMP.
     */
    String HTML_AMP = "&amp";
    /**
     * The constant HTML_QUOTE.
     */
    String HTML_QUOTE = "&quot;";
    /**
     * The constant HTML_LT.
     */
    String HTML_LT = "&lt;";
    /**
     * The constant HTML_GT.
     */
    String HTML_GT = "&gt;";

    // ---------------------------------------------------------------- array

    /**
     * The constant EMPTY_ARRAY.
     */
    String[] EMPTY_ARRAY = new String[0];

    /**
     * The Bytes new line.
     */
    byte[] BYTES_NEW_LINE = StringPool.NEWLINE.getBytes();
}
