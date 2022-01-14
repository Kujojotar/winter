package core.beans.factory;

import annotation.beans.Resource;
import annotation.beans.WinterConstructor;
import core.beans.BeanConstructor;
import core.beans.BeanDefinition;
import core.beans.support.RootBeanDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author james
 * @date 2022-1-13
 * 根据Spring重要容器DefaultListableBeanFactory改编
 * 目前是简单的单例版本
 * 目前默认使用反射的初始化策略，也就是最原始的那个...
 */
public class JamBeanFactory {

    /**
     * 用来表示null的情况
     * 出现这个的原因是因为ConcurrentHashMap不允许null值
     */
    private static final Object Object_NULL = new Object();

    /** 原版也是用一个ConcurrentHashMap实现BeanDefinition的储 */
    private Map<String, BeanDefinition> beans = new ConcurrentHashMap<>(16);

    /** 实际上存储单例的地方，用的也是一个ConcurrentHashMap */
    private Map<String,Object> singletons = new ConcurrentHashMap<>(16);

    /** 记录正在创建的bean */
    private final Set singletonsCurrentlyInCreation = Collections.synchronizedSet(new HashSet());


    /**
     * 万恶的getBean方法👻
     * @param beanName bean的名字
     * @return 所要获取bean实例
     */
    public Object getBean(String beanName){
        if(!beans.containsKey(beanName)){
            return null;
        }
        Object beanObject = getSingleton(beanName);
        if(beanObject==null){
             beanObject = getSingleton(beanName, new ObjectFactory() {
                 @Override
                 public Object getObject() {
                     return doCreateBean(beanName);
                 }
             });
        }
        return beanObject;
    }

    public void registerBean(String beanName, BeanDefinition beanDefinition){
        this.beans.put(beanName, beanDefinition);
    }

    protected void registerSingleton(String beanName, Object beanInstance)throws IllegalStateException{
        synchronized (this.singletons){
            Object old = this.singletons.get(beanName);
            if(old!=null){
                throw new IllegalStateException("这个bean已经被注册过了:"+beanName);
            }
            addSingleton(beanName, beanInstance);
        }
    }

    protected void addSingleton(String beanName,Object instance){
        synchronized (this.singletons){
            singletons.put(beanName, instance!=null?instance:Object_NULL);
        }
    }

    protected Object getSingleton(String beanName){
        Object instance;
        synchronized (this.singletons){
            Object old = singletons.get(beanName);
            instance = old!=Object_NULL?old:null;
        }
        return instance;
    }

    protected Object getSingleton(String beanName,ObjectFactory objectFactory){
        Object instance;
        synchronized (this.singletons){
            Object old = singletons.get(beanName);
            if(old==null){
                if(isSingletonCurrentlyCreation(beanName)){
                    throw new IllegalStateException("bean正在创建之中");
                }
            }
            old = objectFactory.getObject();
            instance = old!=Object_NULL?old:null;
        }
        return instance;
    }

    protected void removeSingleton(String beanName) {
        this.singletons.remove(beanName);
    }

    public boolean containsSingleton(String beanName) {
        return (this.singletons.containsKey(beanName));
    }

    protected void beforeSingletonObjectCreation(String beanName){
        if(!this.singletonsCurrentlyInCreation.add(beanName)){
            throw new IllegalStateException("bean正在创建中!!");
        }
    }

    protected void afterSingletonObjectCreation(String beanName){
        if(!this.singletonsCurrentlyInCreation.remove(beanName)){
            throw new IllegalStateException("bean正在创建中!!");
        }
    }

    public final boolean isSingletonCurrentlyCreation(String beanName){
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    protected Object doCreateBean(String beanName){
        BeanDefinition bd = beans.get(beanName);
        beforeSingletonObjectCreation(beanName);
        Object beanObject = createBeanInstance(bd);
        registerSingleton(beanName, beanObject);
        afterSingletonObjectCreation(beanName);
        return beanObject;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition){
        if(beanDefinition instanceof RootBeanDefinition){
            BeanConstructor beanConstructor = ((RootBeanDefinition)beanDefinition).getConstructor();
            Object beanObject ;
            try{
                if(beanConstructor.isAutowired()){
                    List<Object> args = beanConstructor.getArgs();
                    Arrays.asList(beanConstructor.getConstructor().getParameters()).forEach(a->args.add(getRequiredObjectByType(a.getType())));
                }
                beanObject = beanConstructor.getConstructor().newInstance(beanConstructor.getArgs().toArray());
            }catch(Exception e){
                System.out.println(e);
                return null;
            }
                ((RootBeanDefinition)beanDefinition).getAutowireFields().stream().forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(beanObject, getRequiredObjectByType(field.getType()));
                    }catch (IllegalAccessException e){

                    }
                });
            return beanObject;
            }
        return null;
    }

    private Object getRequiredObjectByType(Class type){
        List<String> beanNameList = getRequiredType(type);
        //懒鬼的实现方式
        return getBean(beanNameList.get(0));
    }

    /**
     * 从容器管理的bean对象中寻找requiredType的或者继承requiredType的类，考虑了继承关系
     * @param requiredType 寻找的类型
     * @return 所有满足requiredType的类型
     */
    public List<String> getRequiredType(Class requiredType){
        List<String> list = new ArrayList<>();
        beans.entrySet().stream().forEach(a->{
            if(a.getValue().getClazz().isAssignableFrom(requiredType)){
                list.add(a.getKey());
            }
        });
        return list;
    }

    @Deprecated
    private void setAutowiredValues(Class clazz, Object instance){
        for(Field field:clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(Resource.class)){
                Annotation autowireAnnotation = field.getAnnotation(Resource.class);
                String requiredBean = ((Resource)autowireAnnotation).beanName();
                if(requiredBean.length()<1){
                    continue;
                }
                Object val = getBean(requiredBean);
                field.setAccessible(true);
                try {
                    field.set(instance, val);
                }catch (IllegalAccessException e){
                    continue ;
                }
            }
        }
    }


}
