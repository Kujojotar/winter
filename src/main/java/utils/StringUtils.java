package utils;

public class StringUtils {
    public static String getSimplifiedName(String fullName){
        return fullName.substring(fullName.lastIndexOf(".")==-1?0:fullName.lastIndexOf("."));
    }

    /**
     * 返回name的首字母小写，与默认beanName强相关
     * @param name 名称字符串
     * @return 首字母小写的名称字符串
     */
    public static String getDefaultBeanName(String name){
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(name.charAt(0))).append(sb.substring(1));
        return sb.toString();
    }

    public static String getBeanNameFromAspectExpression(String expression){
        return expression.split("//")[1];
    }

    public static String getMethodNameFromAspectExpression(String expression){
        return expression.split(".")[2];
    }

}
