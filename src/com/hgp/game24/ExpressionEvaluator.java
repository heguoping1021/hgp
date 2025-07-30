package com.hgp.game24;

import java.util.*;

/**
 * 表达式计算器
 * 支持基本的数学运算：+, -, *, /, () 
 * 使用逆波兰表达式（后缀表达式）来计算结果
 */
public class ExpressionEvaluator {
    
    /**
     * 计算数学表达式的值
     * @param expression 要计算的表达式
     * @return 计算结果
     * @throws IllegalArgumentException 如果表达式无效
     */
    public double evaluate(String expression) throws IllegalArgumentException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("表达式不能为空");
        }
        
        // 移除空格
        expression = expression.replaceAll("\\s+", "");
        
        try {
            // 将中缀表达式转换为后缀表达式
            List<String> postfix = infixToPostfix(expression);
            
            // 计算后缀表达式
            return evaluatePostfix(postfix);
        } catch (Exception e) {
            throw new IllegalArgumentException("表达式格式错误: " + e.getMessage());
        }
    }
    
    /**
     * 将中缀表达式转换为后缀表达式（逆波兰表达式）
     */
    private List<String> infixToPostfix(String expression) {
        List<String> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c)) {
                // 处理多位数
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                i--; // 回退一位
                output.add(number.toString());
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                // 弹出操作符直到遇到左括号
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(String.valueOf(operators.pop()));
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // 弹出左括号
                }
            } else if (isOperator(c)) {
                // 处理操作符
                while (!operators.isEmpty() && 
                       operators.peek() != '(' && 
                       getPrecedence(operators.peek()) >= getPrecedence(c)) {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.push(c);
            }
        }
        
        // 弹出剩余的操作符
        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(' || op == ')') {
                throw new IllegalArgumentException("括号不匹配");
            }
            output.add(String.valueOf(op));
        }
        
        return output;
    }
    
    /**
     * 计算后缀表达式的值
     */
    private double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("表达式格式错误");
                }
                
                double b = stack.pop();
                double a = stack.pop();
                double result = performOperation(a, b, token.charAt(0));
                stack.push(result);
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("表达式格式错误");
        }
        
        return stack.pop();
    }
    
    /**
     * 执行基本运算
     */
    private double performOperation(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (Math.abs(b) < 1e-10) {
                    throw new IllegalArgumentException("除数不能为零");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("不支持的操作符: " + operator);
        }
    }
    
    /**
     * 获取操作符的优先级
     */
    private int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
    
    /**
     * 判断字符是否为操作符
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    /**
     * 判断字符串是否为数字
     */
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}