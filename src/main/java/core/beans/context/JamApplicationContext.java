package core.beans.context;

import core.beans.factory.JamBeanFactory;
import core.beans.support.SimpleBeanDefinitionRegistry;
import exception.BeansException;

import java.io.IOException;

public class JamApplicationContext extends AbstractApplicationContext{
    private JamBeanFactory jamBeanFactory;

    private final Object beanFactoryMonitor = new Object();

    public JamApplicationContext(JamBeanFactory jamBeanFactory){
        this(true, jamBeanFactory);
    }

    public JamApplicationContext(boolean refresh, JamBeanFactory jamBeanFactory){
        super(new SimpleBeanDefinitionRegistry(jamBeanFactory));
        this.jamBeanFactory = jamBeanFactory;
        if(refresh)
            refresh();
    }

    protected final void refreshBeanFactory() throws BeansException{
        if(hasBeanFactory()){
            destroyBeans();
            closeBeanFactory();
        }
        try{
            JamBeanFactory jamBeanFactory = createBeanFactory();
            customizeBeanFactory(jamBeanFactory);
            loadBeanDefinitions(jamBeanFactory);
            synchronized (this.beanFactoryMonitor){
                this.jamBeanFactory = jamBeanFactory;
            }
        }catch (IOException e){
            throw new IllegalStateException("Resource is not present");
        }
    }

    protected final void closeBeanFactory(){
        synchronized (this.beanFactoryMonitor){
            this.jamBeanFactory = null;
        }
    }

    protected final boolean hasBeanFactory(){
        synchronized (this.beanFactoryMonitor){
            return this.jamBeanFactory!=null;
        }
    }

    protected void destroyBeans(){
        synchronized (this.beanFactoryMonitor) {
            for (String name : this.jamBeanFactory.getBeanNames()) {
                this.jamBeanFactory.removeBean(name);
            }
        }
    }

    protected void customizeBeanFactory(JamBeanFactory jamBeanFactory){};

    protected void loadBeanDefinitions(JamBeanFactory paramDefaultListableBeanFactory) throws IOException, BeansException{

    }
}
