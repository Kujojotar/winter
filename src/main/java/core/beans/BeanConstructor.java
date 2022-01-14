package core.beans;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author james
 * @date 2022-1-14
 * 这个类主要用作BeanDefinition中选择将要调用的构造器
 */
public class BeanConstructor {
    private Constructor constructor;
    private List<Object> args;
    private boolean isAutowired = false;

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public boolean isAutowired() {
        return isAutowired;
    }

    public void setAutowired(boolean autowired) {
        isAutowired = autowired;
    }
}
