package core.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james
 * 使用cglib方式进行动态代理，基本模仿jdkProxy的思路
 */
public class AopCglibPolicyProxy implements MethodInterceptor {
    private List<SimpleMethodWrapper> decoratedMethods = new ArrayList<>();

    private Object subject;

    public AopCglibPolicyProxy(Object subject) {
        this.subject = subject;
    }

    private SimpleMethodWrapper isMethodDecorated(String method){
        synchronized (this.decoratedMethods) {
            for (int i = 0; i < decoratedMethods.size(); i++) {
                if(method.equals(decoratedMethods.get(i).getMethodName())){
                    return decoratedMethods.get(i);
                }
            }
        }
        return null;
    }

    public void addDecoratedMethod(SimpleMethodWrapper methodWrapper){
        decoratedMethods.add(methodWrapper);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        SimpleMethodWrapper wrapper = isMethodDecorated(method.getName());
        if(wrapper!=null){
            Object res;
            try{
                res = wrapper.invokeAround();
            }catch (Exception e){
                res = null;
            }
            return res;
        }
        return methodProxy.invokeSuper(objects, objects);
    }
}
