package org.guccikray.creditcardmanagesystem.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    private void transactionalMethods() {}

    @Pointcut("execution(* org.guccikray.creditcardmanagesystem.service..*(..))")
    private void fromServicePackage() {}

    @AfterReturning(pointcut = "transactionalMethods() && fromServicePackage() && args(userId,..)" )
    public void afterTransactionalMethod(JoinPoint joinPoint, Long userId) {
        Class<?> declaringClass = joinPoint.getSignature().getDeclaringType();
        String className = declaringClass.getName();

        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        logger.debug("Method '{}' in class {} executed by user with id: {}",
            joinPoint.getSignature().getName(),
            className,
            userId
        );
    }
}
