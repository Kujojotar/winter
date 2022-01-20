import core.beans.BeanDefinition;
import core.beans.factory.JamBeanFactory;
import core.beans.support.RootBeanDefinition;

public class SimpleAopTest {
    public static void main(String[] args) {
        BeanDefinition beanDefinition0 = new RootBeanDefinition(Fly.class);
        BeanDefinition beanDefinition1 = new RootBeanDefinition(Aspect.class);
        JamBeanFactory jamBeanFactory = new JamBeanFactory();
        jamBeanFactory.registerBean("fly",beanDefinition0);
        jamBeanFactory.registerBean("aspect", beanDefinition1);
        Bird bird = (Bird)jamBeanFactory.getBean("fly");
        bird.fly();
        System.out.println(bird==(Bird)jamBeanFactory.getSingleton("fly"));
    }
}
