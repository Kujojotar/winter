package core.beans;

public interface BeanDefinition {

    String getParentName();

    void setParentName(String parentName);

    String getBeanClassName();

    void setBeanClassName(String beanClassName);

    String getScope();

    void setScope(String scope);

    boolean isLazyInit();

    boolean isAbstract();

    boolean isSingleton();

    MutablePropertyValues getProperties();

    Class getClazz();

}
