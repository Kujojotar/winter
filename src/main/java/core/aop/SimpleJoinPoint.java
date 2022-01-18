package core.aop;

import java.lang.reflect.Method;

public class SimpleJoinPoint implements ProceedingJoinPoint {
    private Method method;

    private Object target;

    public SimpleJoinPoint(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target);
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        return method.invoke(target, args);
    }
}
