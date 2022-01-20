package core.aop;

import annotation.aspect.*;
import core.beans.factory.JamBeanFactory;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AopProxyManager {
    private Map<String, List<SimpleMethodWrapper>> methodWrappers;
    private JamBeanFactory jamBeanFactory;

    public AopProxyManager(JamBeanFactory jamBeanFactory){
        this.methodWrappers = new HashMap<>(16);
        this.jamBeanFactory = jamBeanFactory;
    }

    public boolean isBeanWrapped(String beanName){
        return this.methodWrappers.containsKey(beanName);
    }

    public void parseAspect(String aspectName, Class aspect){
        if(aspect.isAnnotationPresent(Aspect.class)){
            SimpleMethodWrapper simpleMethodWrapper = new SimpleMethodWrapper();
            String beanName="";
            //这边用责任链模式可能会更优雅些，不过感觉小项目了必要性不大
            for(Method method:aspect.getDeclaredMethods()){
                if(method.isAnnotationPresent(Pointcut.class)){
                    String aopExpression;
                    try{
                        aopExpression = ((Pointcut)method.getAnnotation(Pointcut.class)).value();
                    }catch (NoSuchFieldError e){
                        return ;
                    }
                    beanName = ((Pointcut)method.getAnnotation(Pointcut.class)).bean();
                    String methodName = ((Pointcut)method.getAnnotation(Pointcut.class)).method();
                    simpleMethodWrapper.setMethod(methodName);
                    Object aspectInstance;
                    aspectInstance = jamBeanFactory.getBean(aspectName);
                    simpleMethodWrapper.setEntity(aspectInstance);
                    //这样的话要求分析Aspect时增强类已经被注册在BeanFactory容器之中，存在不太合理的一些地方
                    SimpleJoinPoint joinPoint = tryGetProceedingJoinPoint(beanName, methodName);
                    if(joinPoint==null){
                        return;
                    }
                    simpleMethodWrapper.setJoinPoint(joinPoint);
                }else if(method.isAnnotationPresent(Before.class)){
                    simpleMethodWrapper.setBeforeMethod(method);
                }else if(method.isAnnotationPresent(After.class)){
                    simpleMethodWrapper.setAfterMethod(method);
                }else if(method.isAnnotationPresent(AfterReturning.class)){
                    simpleMethodWrapper.setAfterReturningMethod(method);
                }else if(method.isAnnotationPresent(Around.class)){
                    simpleMethodWrapper.setAroundMethod(method);
                }
            }
            addMethodWrapper(beanName, simpleMethodWrapper);
        }
    }

    private SimpleJoinPoint tryGetProceedingJoinPoint(String beanName, String methodName){
        SimpleJoinPoint joinPoint;
        try{
            Class clazz = jamBeanFactory.getBeanDefinition(beanName).getClazz();
            Method decoratedMethod = clazz.getMethod(methodName);
            joinPoint = new SimpleJoinPoint(decoratedMethod);
        }catch (Exception e){
            joinPoint = null;
        }
        return joinPoint;
    }

    private void addMethodWrapper(String beanName, SimpleMethodWrapper simpleMethodWrapper){
        if(simpleMethodWrapper==null){
            return ;
        }
        synchronized (this.methodWrappers){
            if(this.methodWrappers.containsKey(beanName)){
                this.methodWrappers.get(beanName).add(simpleMethodWrapper);
            }else{
                List<SimpleMethodWrapper> list = new ArrayList<>();
                list.add(simpleMethodWrapper);
                this.methodWrappers.put(beanName, list);
            }
        }
    }

    public Object generateJdkProxy(String beanName, Object origin){
        if(origin==null){
            return null;
        }
        AopJdkPolicyProxy jdkPolicyProxy = new AopJdkPolicyProxy(origin);
        this.methodWrappers.get(beanName).stream().forEach(a->{
            a.setJoinPointCustomer(origin);
            jdkPolicyProxy.addDecoratedMethod(a);
        });
        Object target = Proxy.newProxyInstance(jdkPolicyProxy.getClass().getClassLoader(), origin.getClass().getInterfaces(), jdkPolicyProxy);
        return target;
    }

    public Object generateCglibProxy(String beanName, Object origin){
        if(origin==null){
            return null;
        }
        AopCglibPolicyProxy cglibPolicyProxy = new AopCglibPolicyProxy(origin);
        this.methodWrappers.get(beanName).stream().forEach(a->{
            a.setJoinPointCustomer(origin);
            cglibPolicyProxy.addDecoratedMethod(a);
        });
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(origin.getClass());
        enhancer.setCallback(cglibPolicyProxy);
        return enhancer.create();
    }
}
