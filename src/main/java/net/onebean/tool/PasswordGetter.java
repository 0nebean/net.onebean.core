package net.onebean.tool;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 0neBean
 * 密码生成器
 */
public class PasswordGetter {

    public static void main(String[] args) {
        new PasswordGetter();
    }

    public static void runIt() {
        StringBuilder password = new StringBuilder();
        int isContainSpecialCharacters = 0;
        int length = 30;
        int min =33;
        int max =126;
        char start = 33;
        String test = "[A-Za-z0-9\\-]*";
        Matcher m;


        Scanner sc = new Scanner(System.in);

        System.out.println("是否包含特殊符号,(复杂度更高,0:否,1是)");

        try {
            isContainSpecialCharacters = sc.nextInt();
        } catch (Exception e) {
            System.out.println("您的输入有误");
        }



        System.out.println("请输入输入所需密码的长度.");

        try {
            length = sc.nextInt();
        } catch (Exception e) {
            System.out.println("您的输入有误");
        }

        while (password.length() != length){
            start = (char) getRandom(max, min);
            m = Pattern.compile(test).matcher(start + "");
            if (m.find() && (start != 34 && start != 92)) {
                password.append(start);
            }
        }


        String result = password.toString();
        if (isContainSpecialCharacters == 0){
            result = getStringRandom(length);
        }


        System.out.println("密码长度 "+result.length());
        System.out.println(result);
    }

    private static int getRandom(int max,int min) {
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }


    //生成随机数字和字母,
    private static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
