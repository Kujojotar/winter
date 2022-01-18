package core.beans.context;

import core.beans.factory.JamBeanFactory;
import core.beans.support.BeanDefinitionRegistry;
import exception.BeansException;

public abstract class AbstractApplicationContext {
    private BeanDefinitionRegistry registry;

    private AbstractApplicationContext parent;

    public AbstractApplicationContext(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void setParent(AbstractApplicationContext parent) {
        this.parent = parent;
    }

    public void refresh(){
        prepareRefresh();
        prepareBeanFactory(null);
    }

    public void prepareRefresh(){

    }

    protected void prepareBeanFactory(JamBeanFactory beanFactory){

    }

    protected void onRefresh() throws BeansException{};

    protected void destroyBeans(){

    }

    protected JamBeanFactory createBeanFactory(){
        return new JamBeanFactory();
    }
}
