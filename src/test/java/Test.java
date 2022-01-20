import core.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

public class Test {
    private static Map<String, ObjectFactory> map = new HashMap<>();

    public static void addSimpleObject(String name, Object e){
        map.put(name, ()->{
            return e;
        });
    }

    public static Object getObject(String name){
        if(map.containsKey(name)){
            return map.get(name).getObject();
        }
        return null;
    }

    public static void main(String[] args) {
        Fly fly = new Fly();
        System.out.println(fly.hashCode());
        addSimpleObject("fly", fly);
        fly = (Fly)getObject("fly");
        fly.fly();
    }

}
