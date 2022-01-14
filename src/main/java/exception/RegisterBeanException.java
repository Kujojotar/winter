package exception;

public class RegisterBeanException extends BeanException{
    private String cause;

    public RegisterBeanException(String cause){
        super();
        this.cause = cause;
    }

    @Override
    void printCause() {
        System.err.println("exception cause:"+cause);
    }
}
