package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    /**
     * 필터 흐름은
     * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러   과 같이 진행된다
     * <p>
     * 필터는 특정 URL 패턴에 적용할 수 있다. /* 이라고 하면 모든 요청에 필터가 적용된다.
     * <p>
     * 참고로 스프링을 사용하는 경우 여기서 말하는 서블릿은 스프링의 디스패처 서블릿으로 생각하면 된다.
     * <p>
     * HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 //로그인 사용자
     * HTTP 요청 -> WAS -> 필터(적절하지 않은 요청이라 판단, 서블릿 호출X) //비 로그인 사용자
     * 필터에서 적절하지 않은 요청이라고 판단하면 거기에서 끝을 낼 수도 있다
     * <p>
     * 필터 체인
     * HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러
     * 필터는 체인으로 구성되는데, 중간에 필터를 자유롭게 추가할 수 있다. 예를 들어서 로그를 남기는 필터를
     * 먼저 적용하고, 그 다음에 로그인 여부를 체크하는 필터를 만들 수 있다.
     */

    /**
     * Http 요청시 같은 요청의 로그에 모두 같은 식별자를 자동으로 남기는 방법은 logback mdc 를 찾아보자.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest 는 기능이 몇개 없어서 바꿔준다.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            //다음 필터가 있으면 다음 필터 호출, 없다면 servlet 호출한다.
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
