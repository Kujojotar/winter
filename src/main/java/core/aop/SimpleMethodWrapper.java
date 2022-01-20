package core.aop;

import java.lang.reflect.Method;

public class SimpleMethodWrapper {
    private Object entity;

    private String method;

    private Method beforeMethod;

    private Method afterMethod;

    private Method afterReturningMethod;

    private Method aroundMethod;

    private SimpleJoinPoint joinPoint;

    /**
     * 暂时就写这三个试试..
     */

    public void invokeBefore(JoinPoint joinPoint)throws Throwable{
        invokeMethod(beforeMethod, joinPoint);
    }

    public void invokeAfter(JoinPoint joinPoint)throws Throwable{
        invokeMethod(afterMethod, joinPoint);
    }

    public Object invokeAround(){
        Object res = null;
        if(aroundMethod==null){
            try{
                invokeBefore(joinPoint);
                res = ((ProceedingJoinPoint)joinPoint).proceed();
                invokeAfter(joinPoint);
            }catch (Exception e){
                invokeAfter(joinPoint);
            }finally {
                return res;
            }
        }else{
            try{
                invokeBefore(joinPoint);
                res = aroundMethod.invoke(entity, joinPoint);
                invokeAfter(joinPoint);
            }catch (Exception e){
                invokeAfter(joinPoint);
            }finally {
                return res;
            }
        }
    }




    private void invokeMethod(Method method, JoinPoint joinPoint)throws Throwable{
        if(method != null){
            if(method.getParameters().length<1){
                method.invoke(entity);
            }else{
                method.invoke(entity, joinPoint);
            }
        }
    }

    public String getMethodName(){
        if(method!=null){
            return method;
        }
        return "";
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public void setAfterReturningMethod(Method afterReturningMethod) {
        this.afterReturningMethod = afterReturningMethod;
    }

    public void setAroundMethod(Method aroundMethod) {
        this.aroundMethod = aroundMethod;
    }

    public void setJoinPoint(SimpleJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public void setJoinPointCustomer(Object customer){
        this.joinPoint.setTarget(customer);
    }
}
