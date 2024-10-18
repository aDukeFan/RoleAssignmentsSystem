package ru.luckyskeet.usermanagment.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessControlAspect {

    @Around("@annotation(CheckAccess)")
    public Object checkAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAdmin(authentication)) {
            return joinPoint.proceed();
        }

        if (isSameUserId(authentication, joinPoint)) {
            return joinPoint.proceed();
        }

        throw new AccessDeniedException("You do not have permission to access this resource");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals(UserAccessLevel.ADMIN.toString()));
    }


    private boolean isSameUserId(Authentication authentication, ProceedingJoinPoint joinPoint) {
        if (authentication != null) {
            Long currentUserId = ((UserDetailsImpl) authentication.getPrincipal()).getUserId();
            Long requestedUserId = Long.valueOf(joinPoint.getArgs()[0].toString());
            return currentUserId.equals(requestedUserId);
        }
        return false;
    }
}