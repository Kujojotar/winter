package exception;

public class NoSuchBeanDefinitionException extends BeanException{
    private String cause;

    public NoSuchBeanDefinitionException(String cause){
        super();
        this.cause = cause;
    }

    @Override
    void printCause() {
        System.err.println("exception cause:"+cause);
    }
}
