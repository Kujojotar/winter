package core.beans.context;

import core.beans.BeanDefinition;
import core.beans.support.BeanDefinitionRegistry;
import core.beans.support.RootBeanDefinition;
import utils.StringUtils;

import java.io.File;

public class ClassPathBeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry){
        this.registry = registry;
    }

    public int scan(String ... basePackages){
        int beansCountBefore = registry.getBeanDefinitionCount();
        doScan(basePackages);
        return registry.getBeanDefinitionCount() - beansCountBefore;
    }

    protected void doScan(String ... basePackages){
        for(int i=0;i<basePackages.length;i++){
            File rootFile;
            try{
                rootFile = new File(basePackages[i]);
                if(rootFile.isDirectory()){
                    for(File file: rootFile.listFiles()){
                        doScan(file.getAbsolutePath());
                    }
                }else{
                    Class model = rootFile.getClass();
                    if(isBean(model)) {
                        BeanDefinition bd = makeBeanDefinition(model);
                        String beanName = getBeanName(model);
                        registry.registerBeanDefinition(beanName, bd);
                    }
                }
            }catch(NoSuchFieldError e){
                continue;
            }
        }
    }

    private String getBeanName(Class model){
        return StringUtils.getDefaultBeanName(model.getSimpleName());
    }

    private BeanDefinition makeBeanDefinition(Class bdModel){
        return new RootBeanDefinition(bdModel);
    }

    private boolean isBean(Class clazz){
        return true;
    }
}
