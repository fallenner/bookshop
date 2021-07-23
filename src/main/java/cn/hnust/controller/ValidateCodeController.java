package cn.hnust.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.hnust.util.ValidateCode;

@Controller
@RequestMapping("/validate_code")
public class ValidateCodeController extends BaseController {
    @RequestMapping("/get")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String validateCode = ValidateCode.generateVerifyCode(5);
        request.getSession().setAttribute("validate", validateCode);
        ValidateCode.outputImage(150, 50, response.getOutputStream(), validateCode);
    }
}
