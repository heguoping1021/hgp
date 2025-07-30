package com.hgp.game24;

import java.util.*;

/**
 * 24点游戏主类
 * 玩家需要使用四个数字和基本运算符(+, -, *, /)来组成等于24的表达式
 */
public class Game24 {
    private Scanner scanner;
    private Random random;
    private ExpressionEvaluator evaluator;
    private int[] gameNumbers;
    
    public Game24() {
        scanner = new Scanner(System.in);
        random = new Random();
        evaluator = new ExpressionEvaluator();
        gameNumbers = new int[4];
    }
    
    /**
     * 开始游戏
     */
    public void startGame() {
        printWelcomeMessage();
        
        while (true) {
            generateNumbers();
            playRound();
            
            if (!askPlayAgain()) {
                break;
            }
        }
        
        System.out.println("谢谢游戏！再见！");
        scanner.close();
    }
    
    /**
     * 打印欢迎信息
     */
    private void printWelcomeMessage() {
        System.out.println("===============================");
        System.out.println("      欢迎来到24点游戏！       ");
        System.out.println("===============================");
        System.out.println("游戏规则：");
        System.out.println("1. 系统会给出4个数字");
        System.out.println("2. 使用 +、-、*、/ 四种运算符");
        System.out.println("3. 可以使用括号改变运算顺序");
        System.out.println("4. 每个数字必须且只能使用一次");
        System.out.println("5. 计算结果等于24即为获胜");
        System.out.println("6. 输入'hint'可获得提示");
        System.out.println("7. 输入'skip'跳过当前题目");
        System.out.println("===============================\n");
    }
    
    /**
     * 生成四个随机数字
     */
    private void generateNumbers() {
        for (int i = 0; i < 4; i++) {
            gameNumbers[i] = random.nextInt(13) + 1; // 1-13的数字
        }
        System.out.println("本轮数字是：" + Arrays.toString(gameNumbers));
    }
    
    /**
     * 进行一轮游戏
     */
    private void playRound() {
        boolean solved = false;
        
        while (!solved) {
            System.out.print("请输入表达式（使用数字 " + Arrays.toString(gameNumbers) + "）：");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("hint")) {
                showHint();
                continue;
            }
            
            if (input.equalsIgnoreCase("skip")) {
                System.out.println("跳过当前题目...\n");
                return;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            // 验证输入
            if (!isValidExpression(input)) {
                System.out.println("表达式无效！请检查：");
                System.out.println("- 是否只使用了给定的四个数字");
                System.out.println("- 每个数字是否只使用了一次");
                System.out.println("- 是否只使用了 +、-、*、/、() 运算符");
                continue;
            }
            
            try {
                double result = evaluator.evaluate(input);
                if (Math.abs(result - 24.0) < 0.0001) {
                    System.out.println("恭喜！计算结果是 " + result + "，你赢了！🎉\n");
                    solved = true;
                } else {
                    System.out.println("计算结果是 " + result + "，不等于24，请再试试！");
                }
            } catch (Exception e) {
                System.out.println("表达式计算错误：" + e.getMessage());
            }
        }
    }
    
    /**
     * 验证表达式是否有效
     */
    private boolean isValidExpression(String expression) {
        // 移除空格
        expression = expression.replaceAll("\\s+", "");
        
        // 检查是否只包含允许的字符
        if (!expression.matches("[0-9+\\-*/(). ]+")) {
            return false;
        }
        
        // 提取表达式中的数字
        List<Integer> numbersInExpression = new ArrayList<>();
        StringBuilder currentNumber = new StringBuilder();
        
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else {
                if (currentNumber.length() > 0) {
                    numbersInExpression.add(Integer.parseInt(currentNumber.toString()));
                    currentNumber.setLength(0);
                }
            }
        }
        
        // 处理最后一个数字
        if (currentNumber.length() > 0) {
            numbersInExpression.add(Integer.parseInt(currentNumber.toString()));
        }
        
        // 检查数字是否匹配
        if (numbersInExpression.size() != 4) {
            return false;
        }
        
        // 排序后比较
        List<Integer> gameNumbersList = new ArrayList<>();
        for (int num : gameNumbers) {
            gameNumbersList.add(num);
        }
        Collections.sort(gameNumbersList);
        Collections.sort(numbersInExpression);
        
        return gameNumbersList.equals(numbersInExpression);
    }
    
    /**
     * 显示提示
     */
    private void showHint() {
        System.out.println("提示：");
        System.out.println("- 试试不同的运算符组合");
        System.out.println("- 使用括号改变运算顺序");
        System.out.println("- 常见的24点组合：(a+b)*(c+d), a*b+c*d, a*(b+c+d) 等");
        System.out.println("- 如果有较大的数，试试除法");
        System.out.println("- 如果有1，可以用作乘法或除法的中性元素");
    }
    
    /**
     * 询问是否继续游戏
     */
    private boolean askPlayAgain() {
        while (true) {
            System.out.print("是否继续游戏？(y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes") || input.equals("是")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("否")) {
                return false;
            } else {
                System.out.println("请输入 y 或 n");
            }
        }
    }
}