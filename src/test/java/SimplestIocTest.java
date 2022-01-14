import beansource.AutowiredBean;
import beansource.BigRabbit;
import beansource.SimpleBean;
import beansource.Xixi;
import core.beans.BeanDefinition;
import core.beans.factory.JamBeanFactory;
import core.beans.support.RootBeanDefinition;

public class SimplestIocTest {
    public static void main(String[] args) {
        BeanDefinition beanDefinition = new RootBeanDefinition(SimpleBean.class);
        BeanDefinition beanDefinition1 = new RootBeanDefinition(Xixi.class);
        BeanDefinition beanDefinition2 =  new RootBeanDefinition(AutowiredBean.class);
        BeanDefinition beanDefinition3 = new RootBeanDefinition(BigRabbit.class);
        JamBeanFactory jamBeanFactory = new JamBeanFactory();
        jamBeanFactory.registerBean("dog", beanDefinition);
        jamBeanFactory.registerBean("huhu",beanDefinition1);
        jamBeanFactory.registerBean("bb", beanDefinition2);
        jamBeanFactory.registerBean("rabbit",beanDefinition3);
        //System.out.println(jamBeanFactory.getBean("dog"));
        //System.out.println(jamBeanFactory.getBean("huhu"));
        System.out.println(jamBeanFactory.getBean("bb"));
        System.out.println(jamBeanFactory.getBean("rabbit"));
    }
}
