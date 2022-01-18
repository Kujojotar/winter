package core.aop;

public interface JoinPoint {
    Object[] getArgs();

    Object getTarget();

    Object getThis();
}
