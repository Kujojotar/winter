package utils;

/**
 * @author james
 * @date 2022-1-14
 * 一个类型转换的util，写得有点煞笔
 */
public class TypeConverter {

    /**
     * 主要是配合@WinterConstructor注解的方法，将注解中的values值转换为对应的属性值
     * 因为注解不支持Object类型，暂时没想到什么好的解决方案，所以用String[]代替
     * @param type 参数的类型
     * @param val 对应在values中的值
     * @return 转换类型后的值
     */
    public static Object typeConvert(Class type, String val){
        if(type.equals(int.class)||type.equals(Integer.class)){
            return new Integer(val);
        }else if(type.equals(boolean.class)||type.equals(Boolean.class)){
            return new Boolean(val);
        }else if(type.equals(float.class)||type.equals(Float.class)){
            return new Float(val);
        }else if(type.equals(double.class)||type.equals(Double.class)){
            return new Double(val);
        }
        return val;
    }

    public static Object convertWithInitialVal(Class type){
        if(type.equals(int.class)||type.equals(Integer.class)){
            return new Integer(0);
        }else if(type.equals(boolean.class)||type.equals(Boolean.class)){
            return new Boolean(false);
        }else if(type.equals(float.class)||type.equals(Float.class)){
            return new Float(0);
        }else if(type.equals(double.class)||type.equals(Double.class)){
            return new Double(0);
        }else if(type.equals(String.class)){
            return "";
        }
        return null;
    }
}
