package core.beans.support;

import core.beans.BeanDefinition;
import exception.NoSuchBeanDefinitionException;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String paramString, BeanDefinition paramBeanDefinition);

    void removeBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String paramString) throws NoSuchBeanDefinitionException;

    boolean containsBeanDefinition(String paramString);

    int getBeanDefinitionCount();


}
