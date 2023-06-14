//package com.challenge.devchall.errorController;
//
//import com.challenge.devchall.base.rsData.RsData;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class customErrorController implements ErrorController {
//
//    private static final String ERROR_PATH = "/error";
//
//    @RequestMapping(ERROR_PATH)
//    public String handleError(HttpServletRequest request) {
////        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
////
////        if (statusCode != null) {
////            if (statusCode == HttpStatus.NOT_FOUND.value()) {
////                // 404 에러에 대한 처리 로직을 구현합니다.
////                // 예를 들어, 커스텀 404 에러 페이지로 리다이렉트하거나 특정 뷰를 반환할 수 있습니다.
////                return "error/404"; // 404 에러 페이지의 뷰 이름을 반환합니다.
////            } else if (statusCode >= HttpStatus.INTERNAL_SERVER_ERROR.value() &&
////                    statusCode < HttpStatus.MULTIPLE_CHOICES.value()) {
////                // 500 에러에 대한 처리 로직을 구현합니다.
////                // 예를 들어, 커스텀 500 에러 페이지로 리다이렉트하거나 특정 뷰를 반환할 수 있습니다.
////                return "error/500"; // 500 에러 페이지의 뷰 이름을 반환합니다.
////            }
////        }
//
//        // 그 외의 경우에는 일반적인 에러 페이지를 반환합니다.
//        return "error/500"; // 일반적인 에러 페이지의 뷰 이름을 반환합니다.
//    }
//
//}