package core.beans.support;

import annotation.beans.Autowired;
import annotation.beans.WinterConstructor;
import core.beans.BeanConstructor;
import core.beans.BeanDefinition;
import core.beans.MutablePropertyValues;
import core.beans.type.TypeChain;
import utils.TypeConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author james
 * @date 2022-1-13
 * 很简单版本的BeanDefinition的实现类
 * 目前不考虑一些复杂的因素
 */
public class RootBeanDefinition implements BeanDefinition {
    private String beanClassName;

    private Class clazz;

    private MutablePropertyValues mutablePropertyValues;

    private BeanConstructor constructor;

    private List<Field> autowireFields;

    public RootBeanDefinition(Class clazz){
        if(clazz==null)
            throw new IllegalStateException("Class类型为空");
        this.clazz = clazz;
        beanClassName = clazz.getName();
        mutablePropertyValues = new MutablePropertyValues();
        constructor = new BeanConstructor();
        autowireFields = new ArrayList<>();
        initProperties();
        parseBeanConstructor();
        parseAutowiredFields();
    }

    private void initProperties(){
        for(Field field: clazz.getDeclaredFields()){
            Class type = field.getType();
            mutablePropertyValues.addPropertyValue(field.getName(), TypeChain.check(type));
        }
        /*
        mutablePropertyValues.getPropertyValueList().stream().forEach(a-> {System.out.print("property name:"+((PropertyValue)a).getName()+"value:");
            System.out.println(((PropertyValue) a).getValue());
        });
         */
    }

    private class ConstructorWrapper{
        private Constructor constructor;

        protected Constructor getConstructor() {
            return constructor;
        }

        protected void setConstructor(Constructor constructor) {
            this.constructor = constructor;
        }
    }

    private void parseBeanConstructor(){
        if(clazz.isAnnotationPresent(WinterConstructor.class)){
            Annotation annotation = clazz.getAnnotation(WinterConstructor.class);
            Class[] argsTypes = ((WinterConstructor)annotation).types();
            String[] consArgs = ((WinterConstructor)annotation).values();
            Constructor targetConstructor;
            List<Object> list = new ArrayList<>();
            try {
                targetConstructor = clazz.getConstructor(argsTypes);
                for (int i = 0; i < argsTypes.length; i++) {
                    list.add(TypeConverter.typeConvert(argsTypes[i], consArgs[i]));
                }
            }catch (NoSuchMethodException e){
                targetConstructor = null;
            }
            constructor.setConstructor(targetConstructor);
            constructor.setArgs(list);
        }else{
            Constructor[] constructors = clazz.getConstructors();
            List<Object> list = new ArrayList<>();
            if(constructors.length==0){
                return ;
            }
            //由于lambda表达式不允许改变外部变量的特点采用了修饰类修饰了一下构造器
            //实际上就是一个偷梁换柱的做法
            ConstructorWrapper constructorWrapper = new ConstructorWrapper();
            Arrays.asList(constructors).forEach(a->{
                if(a.isAnnotationPresent(Autowired.class)){
                    constructorWrapper.setConstructor(a);
                }
            });
            if(constructorWrapper.constructor!=null){
                constructor.setConstructor(constructorWrapper.constructor);
                constructor.setAutowired(true);
                constructor.setArgs(list);
            }else{
                Constructor targetConstructor;
                try{
                    targetConstructor = clazz.getConstructor();
                }catch (NoSuchMethodException e){
                    //这边本来应该选一个适合的构造器，但是感觉麻烦懒得写了...
                    targetConstructor = null;
                }
                constructor.setConstructor(targetConstructor);
                constructor.setArgs(list);
            }
        }
    }

    private void parseAutowiredFields(){
        Arrays.asList(clazz.getDeclaredFields()).stream().forEach(a->{
            if(a.isAnnotationPresent(Autowired.class)){
                autowireFields.add(a);
            }
        });
    }



    @Override
    public String getParentName() {
        return null;
    }

    @Override
    public void setParentName(String parentName) {

    }

    @Override
    public String getBeanClassName() {
        return beanClassName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        beanClassName = beanClassName;
    }

    @Override
    public String getScope() {
        return "Singleton";
    }

    @Override
    public void setScope(String scope) {

    }

    @Override
    public boolean isLazyInit() {
        return false;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public MutablePropertyValues getProperties() {
        return null;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }

    public BeanConstructor getConstructor() {
        return constructor;
    }

    public void setConstructor(BeanConstructor constructor) {
        this.constructor = constructor;
    }

    public List<Field> getAutowireFields() {
        return autowireFields;
    }

    public void setAutowireFields(List<Field> autowireFields) {
        this.autowireFields = autowireFields;
    }
}
