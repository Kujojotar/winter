package core.beans.support;

import core.beans.BeanDefinition;
import core.beans.factory.JamBeanFactory;
import exception.NoSuchBeanDefinitionException;

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry{
    private JamBeanFactory jamBeanFactory;

    public SimpleBeanDefinitionRegistry(JamBeanFactory jamBeanFactory) {
        this.jamBeanFactory = jamBeanFactory;
    }

    @Override
    public void registerBeanDefinition(String paramString, BeanDefinition paramBeanDefinition) {
        jamBeanFactory.registerBean(paramString, paramBeanDefinition);
    }

    @Override
    public void removeBeanDefinition(String paramString) throws NoSuchBeanDefinitionException {
        jamBeanFactory.removeBean(paramString);
    }

    @Override
    public BeanDefinition getBeanDefinition(String paramString) throws NoSuchBeanDefinitionException {
        return jamBeanFactory.getBeanDefinition(paramString);
    }

    @Override
    public boolean containsBeanDefinition(String paramString) {
        return jamBeanFactory.isBeanExist(paramString);
    }

    @Override
    public int getBeanDefinitionCount() {
        return jamBeanFactory.getBeanCount();
    }
}
