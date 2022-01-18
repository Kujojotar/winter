package core.beans.resource.reader;

import core.beans.BeanDefinition;
import core.beans.support.RootBeanDefinition;

/**
 * @author james
 */
public class DefaultBeanAnnotationReader{

    public BeanDefinition loadBeanDefinition(String fullName){
        Class model;
        try{
            model = Class.forName(fullName);
        }catch(ClassNotFoundException e){
            model = null;
        }
        BeanDefinition beanDefinition = makeBeanDefinitionByClass(model);
        return beanDefinition;
    }

    public BeanDefinition makeBeanDefinitionByClass(Class model){
        if(model == null){
            return null;
        }
        return new RootBeanDefinition(model);
    }

}
