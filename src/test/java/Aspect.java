import annotation.aspect.After;
import annotation.aspect.Around;
import annotation.aspect.Before;
import annotation.aspect.Pointcut;
import core.aop.ProceedingJoinPoint;

@annotation.aspect.Aspect(value = "execution.fly.fly")
public class Aspect {

    @Pointcut(value = "execution/fly/fly", bean = "fly", method = "fly")
    public void semeng(){};

    @Before("ss")
    public void doBefore(){
        System.out.println("before");
    }

    @After("")
    public void doAfter(){
        System.out.println("after");
    }

    @Around("")
    public void doAround(ProceedingJoinPoint joinPoint){
        System.out.println("around before");
        try {
            joinPoint.proceed();
        }catch(Throwable a){

        }
        System.out.println("around after");
    }
}
