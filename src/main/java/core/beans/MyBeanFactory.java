package core.beans;

import exception.BeansException;
import exception.NoSuchBeanDefinitionException;

/**
 * 最初版本的BeanFactory定义，本着简单的原则就先弄个出版的雏形出来
 */
public interface MyBeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Class requiredType) throws BeansException;

    boolean containsBean(String name);

    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

}
