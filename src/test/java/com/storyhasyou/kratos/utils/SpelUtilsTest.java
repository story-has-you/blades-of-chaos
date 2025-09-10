package com.storyhasyou.kratos.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

/**
 * SpelUtils单元测试类
 * 
 * 【强制】测试Spring Expression Language解析功能
 * 【强制】测试AOP切点参数解析和表达式求值
 * 【强制】测试方法命名遵循规范：should_ReturnExpected_When_GivenInput()
 * 
 * @author fangxi created by 2023/10/25
 */
@ExtendWith(MockitoExtension.class)
public class SpelUtilsTest {

    @Mock
    private JoinPoint mockJoinPoint;
    
    @Mock
    private MethodSignature mockMethodSignature;

    // ==================== 基础SPEL解析测试 ====================

    @Test
    void should_ParseSimpleVariable_When_GivenBasicExpression() throws NoSuchMethodException {
        // Given
        String expression = "#username";
        Object[] args = {"testUser"};
        String[] paramNames = {"username"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("testUser");
    }

    @Test
    void should_ParseMultipleVariables_When_GivenComplexExpression() throws NoSuchMethodException {
        // Given
        String expression = "#firstName + '_' + #lastName";
        Object[] args = {"John", "Doe"};
        String[] paramNames = {"firstName", "lastName"};
        
        setupMockJoinPoint(args, paramNames, "getUserFullName", String.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("John_Doe");
    }

    @Test
    void should_ParseObjectProperty_When_AccessingObjectField() throws NoSuchMethodException {
        // Given
        String expression = "#user.name";
        User user = new User("Alice", 25, "alice@example.com");
        Object[] args = {user};
        String[] paramNames = {"user"};
        
        setupMockJoinPoint(args, paramNames, "processUser", User.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("Alice");
    }

    @Test
    void should_ParseObjectMethod_When_AccessingObjectMethod() throws NoSuchMethodException {
        // Given
        String expression = "#user.getEmailDomain()";
        User user = new User("Bob", 30, "bob@company.com");
        Object[] args = {user};
        String[] paramNames = {"user"};
        
        setupMockJoinPoint(args, paramNames, "validateUser", User.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("company.com");
    }

    // ==================== 数值计算测试 ====================

    @Test
    void should_CalculateArithmetic_When_GivenMathExpression() throws NoSuchMethodException {
        // Given
        String expression = "#num1 + #num2 * #num3";
        Object[] args = {10, 5, 2}; // 10 + 5 * 2 = 20
        String[] paramNames = {"num1", "num2", "num3"};
        
        setupMockJoinPoint(args, paramNames, "calculate", Integer.class, Integer.class, Integer.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("20");
    }

    @Test
    void should_HandleBigDecimalCalculation_When_GivenDecimalNumbers() throws NoSuchMethodException {
        // Given
        String expression = "#price.multiply(#quantity)";
        BigDecimal price = new BigDecimal("19.99");
        BigDecimal quantity = new BigDecimal("3");
        Object[] args = {price, quantity};
        String[] paramNames = {"price", "quantity"};
        
        setupMockJoinPoint(args, paramNames, "calculateTotal", BigDecimal.class, BigDecimal.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("59.97");
    }

    // ==================== 条件表达式测试 ====================

    @Test
    void should_EvaluateCondition_When_GivenTernaryExpression() throws NoSuchMethodException {
        // Given
        String expression = "#age >= 18 ? 'adult' : 'minor'";
        Object[] args = {25};
        String[] paramNames = {"age"};
        
        setupMockJoinPoint(args, paramNames, "checkAge", Integer.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("adult");
    }

    @Test
    void should_EvaluateCondition_When_GivenBooleanExpression() throws NoSuchMethodException {
        // Given
        String expression = "#isActive and #isVerified";
        Object[] args = {true, false};
        String[] paramNames = {"isActive", "isVerified"};
        
        setupMockJoinPoint(args, paramNames, "checkStatus", Boolean.class, Boolean.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("false");
    }

    // ==================== 字符串操作测试 ====================

    @Test
    void should_ProcessString_When_GivenStringMethods() throws NoSuchMethodException {
        // Given
        String expression = "#text.toUpperCase().substring(0, 3)";
        Object[] args = {"hello world"};
        String[] paramNames = {"text"};
        
        setupMockJoinPoint(args, paramNames, "processText", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("HEL");
    }

    @Test
    void should_ConcatenateStrings_When_GivenMultipleStrings() throws NoSuchMethodException {
        // Given
        String expression = "'User: ' + #name + ' (ID: ' + #id + ')'";
        Object[] args = {"John", 123};
        String[] paramNames = {"name", "id"};
        
        setupMockJoinPoint(args, paramNames, "formatUser", String.class, Integer.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("User: John (ID: 123)");
    }

    // ==================== 数组和集合操作测试 ====================

    @Test
    void should_AccessArrayElement_When_GivenArrayIndex() throws NoSuchMethodException {
        // Given
        String expression = "#items[1]";
        String[] items = {"first", "second", "third"};
        Object[] args = {items};
        String[] paramNames = {"items"};
        
        setupMockJoinPoint(args, paramNames, "processItems", String[].class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("second");
    }

    @Test
    void should_GetArrayLength_When_AccessingLengthProperty() throws NoSuchMethodException {
        // Given
        String expression = "#numbers.length";
        int[] numbers = {1, 2, 3, 4, 5};
        Object[] args = {numbers};
        String[] paramNames = {"numbers"};
        
        setupMockJoinPoint(args, paramNames, "countNumbers", int[].class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("5");
    }

    // ==================== null值处理测试 ====================

    @Test
    void should_ReturnNull_When_ExpressionIsNull() throws NoSuchMethodException {
        // Given
        String expression = null;
        Object[] args = {"test"};
        String[] paramNames = {"param"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_ReturnNull_When_ExpressionIsNullOrEmpty(String expression) throws NoSuchMethodException {
        // Given
        Object[] args = {"test"};
        String[] paramNames = {"param"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_HandleNullParameter_When_ParameterIsNull() throws NoSuchMethodException {
        // Given
        String expression = "#param";
        Object[] args = {null};
        String[] paramNames = {"param"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_HandleSafeNavigation_When_UsingElvisOperator() throws NoSuchMethodException {
        // Given
        String expression = "#user?.name ?: 'Unknown'";
        Object[] args = {null};
        String[] paramNames = {"user"};
        
        setupMockJoinPoint(args, paramNames, "getUserName", User.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("Unknown");
    }

    @Test
    void should_ReturnNull_When_ExpressionIsInvalid() throws NoSuchMethodException {
        // Given - 由于我们修改了SpelUtils返回null而不抛异常，测试需要相应调整
        String invalidExpression = "#nonExistentParam";
        Object[] args = {"test"};
        String[] paramNames = {"param"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, invalidExpression);
        
        // Then - 验证异常情况下返回null
        assertThat(result).isNull();
    }

    // ==================== 复杂表达式测试 ====================

    @Test
    void should_ParseStringConcatenation_When_GivenTwoParameters() throws NoSuchMethodException {
        // Given
        String expression = "#userId + '_' + #action";
        Object[] args = {"user123", "login"};
        String[] paramNames = {"userId", "action"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("user123_login");
    }
    
    @Test
    void should_ParseArithmeticExpression_When_GivenNumericParameters() throws NoSuchMethodException {
        // Given
        String expression = "#price * #quantity";
        Object[] args = {10.5, 2.0};
        String[] paramNames = {"price", "quantity"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("21.0");
    }
    
    @Test
    void should_ParseTernaryExpression_When_GivenBooleanParameter() throws NoSuchMethodException {
        // Given
        String expression = "#enabled ? 'active' : 'inactive'";
        Object[] args = {true};
        String[] paramNames = {"enabled"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("active");
    }
    
    @Test
    void should_ParseStringConditional_When_GivenStringLength() throws NoSuchMethodException {
        // Given
        String expression = "#name.length() > 5 ? #name : 'short'";
        Object[] args = {"Alexander"};
        String[] paramNames = {"name"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("Alexander");
    }
    
    private Object convertArgument(String arg) {
        if ("true".equals(arg) || "false".equals(arg)) {
            return Boolean.valueOf(arg);
        }
        if (isNumeric(arg)) {
            return Double.valueOf(arg);
        }
        return arg;
    }
    
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ==================== 实际应用场景测试 ====================

    @Test
    void should_GenerateCacheKey_When_UsingInCacheAnnotation() throws NoSuchMethodException {
        // Given
        String expression = "'user:' + #userId + ':profile'";
        Object[] args = {12345};
        String[] paramNames = {"userId"};
        
        setupMockJoinPoint(args, paramNames, "getUserProfile", Long.class);
        
        // When
        String cacheKey = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(cacheKey).isEqualTo("user:12345:profile");
    }

    @Test
    void should_GenerateLogMessage_When_UsingInLogAnnotation() throws NoSuchMethodException {
        // Given
        String expression = "'User ' + #user.name + ' performed action: ' + #action";
        User user = new User("Admin", 30, "admin@system.com");
        String action = "DELETE_USER";
        Object[] args = {user, action};
        String[] paramNames = {"user", "action"};
        
        setupMockJoinPoint(args, paramNames, "performAction", User.class, String.class);
        
        // When
        String logMessage = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(logMessage).isEqualTo("User Admin performed action: DELETE_USER");
    }

    @Test
    void should_ValidatePermission_When_UsingInSecurityCheck() throws NoSuchMethodException {
        // Given
        String expression = "#user.role == 'ADMIN' and #resource.startsWith('admin.')";
        User adminUser = new User("SuperUser", 40, "super@admin.com");
        adminUser.setRole("ADMIN");
        String resource = "admin.user.delete";
        Object[] args = {adminUser, resource};
        String[] paramNames = {"user", "resource"};
        
        setupMockJoinPoint(args, paramNames, "checkPermission", User.class, String.class);
        
        // When
        String result = SpelUtils.parse(mockJoinPoint, expression);
        
        // Then
        assertThat(result).isEqualTo("true");
    }

    // ==================== 性能测试 ====================

    @Test
    void should_PerformEfficiently_When_ParsingManyExpressions() throws NoSuchMethodException {
        // Given
        String[] expressions = {
            "#param1",
            "#param1 + #param2",
            "#param1.length()",
            "'prefix_' + #param1 + '_suffix'",
            "#param2 > 10 ? 'high' : 'low'"
        };
        
        Object[] args = {"test", 15};
        String[] paramNames = {"param1", "param2"};
        
        setupMockJoinPoint(args, paramNames, "testMethod", String.class, Integer.class);
        
        // When
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            for (String expression : expressions) {
                String result = SpelUtils.parse(mockJoinPoint, expression);
                assertThat(result).isNotNull();
            }
        }
        
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        
        // Then
        assertThat(elapsedTime).isLessThan(5000L); // 5秒内完成
        System.out.println("SPEL解析性能测试: " + (expressions.length * 10000) + " 次解析耗时 " + elapsedTime + "ms");
    }

    // ==================== 辅助方法 ====================

    private void setupMockJoinPoint(Object[] args, String[] paramNames, String methodName, Class<?>... paramTypes) 
            throws NoSuchMethodException {
        // 使用lenient模式避免UnnecessaryStubbing错误
        lenient().when(mockJoinPoint.getArgs()).thenReturn(args);
        lenient().when(mockJoinPoint.getSignature()).thenReturn(mockMethodSignature);
        
        Method method = TestClass.class.getMethod(methodName, paramTypes);
        lenient().when(mockMethodSignature.getMethod()).thenReturn(method);
        
        // 对于参数名发现，我们需要特别处理以避免循环引用
        // 直接返回我们预期的参数名，而不是依赖Spring的参数名发现器
        lenient().when(mockMethodSignature.getParameterNames()).thenReturn(paramNames);
    }

    private void setupMockJoinPointWithNullParamNames(Object[] args, String methodName, Class<?>... paramTypes) 
            throws NoSuchMethodException {
        lenient().when(mockJoinPoint.getArgs()).thenReturn(args);
        lenient().when(mockJoinPoint.getSignature()).thenReturn(mockMethodSignature);
        
        Method method = TestClass.class.getMethod(methodName, paramTypes);
        lenient().when(mockMethodSignature.getMethod()).thenReturn(method);
        lenient().when(mockMethodSignature.getParameterNames()).thenReturn(null);
    }

    // ==================== 测试用例辅助类 ====================

    public static class User {
        private String name;
        private int age;
        private String email;
        private String role;
        
        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getEmailDomain() {
            return email.substring(email.indexOf('@') + 1);
        }
    }

    public static class TestClass {
        public void testMethod(String param) {}
        public void testMethod(String param1, String param2) {}
        public void testMethod(String param1, Integer param2) {}
        public void testMethod(Object param1, Object param2) {}
        public void testMethod(String param1, Object param2) {}
        public void getUserFullName(String firstName, String lastName) {}
        public void processUser(User user) {}
        public void validateUser(User user) {}
        public void calculate(Integer num1, Integer num2, Integer num3) {}
        public void calculateTotal(BigDecimal price, BigDecimal quantity) {}
        public void checkAge(Integer age) {}
        public void checkStatus(Boolean isActive, Boolean isVerified) {}
        public void processText(String text) {}
        public void formatUser(String name, Integer id) {}
        public void processItems(String[] items) {}
        public void countNumbers(int[] numbers) {}
        public void getUserName(User user) {}
        public void getUserProfile(Long userId) {}
        public void performAction(User user, String action) {}
        public void checkPermission(User user, String resource) {}
    }
}