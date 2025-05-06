package org.yapp.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.domain.user.application.BanCheckingService;

@Component
@RequiredArgsConstructor
public class BanCheckingInterceptor implements HandlerInterceptor {

  private final BanCheckingService banCheckingService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 블랙리스트에 유저 ID 존재하면 인터셉터 통과 못함
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      Long userId = (Long) authentication.getPrincipal();
      if (banCheckingService.existsInBlackList(userId)) {
        throw new ApplicationException(AuthErrorCode.PERMANENTLY_BANNED);
      }
    }
    return true;
  }
}
