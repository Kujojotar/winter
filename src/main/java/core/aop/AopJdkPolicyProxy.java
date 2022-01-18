package core.aop;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AopJdkPolicyProxy implements InvocationHandler {
    private List<SimpleMethodWrapper> decoratedMethods = new ArrayList<>();

    private Object subject;

    public AopJdkPolicyProxy(Object subject) {
        this.subject = subject;
    }

    private SimpleMethodWrapper isMethodDecorated(Method method){
        synchronized (this.decoratedMethods) {
            for (int i = 0; i < decoratedMethods.size(); i++) {
                if(method.getName().equals(decoratedMethods.get(i).getMethodName())){
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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SimpleMethodWrapper wrapper = isMethodDecorated(method);
        if(wrapper!=null){
            Object res;
            try{
                res = wrapper.invokeAround();
            }catch (Exception e){
                res = null;
            }
            return res;
        }
        return method.invoke(subject, args);
    }
}
