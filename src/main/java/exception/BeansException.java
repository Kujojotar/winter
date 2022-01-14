package exception;


public class BeansException extends BeanException{
    private String cause;

    public BeansException(String cause){
        super();
        this.cause = cause;
    }

    @Override
    void printCause() {
        System.err.println("exception cause:"+cause);
    }
}
