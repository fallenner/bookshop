package cn.hnust.bookshop.controller;

import cn.hnust.bookshop.bean.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public ResponseResult test() {
        ResponseResult result = new ResponseResult();
        result.setSuccessMsg("测试成功");
        return result;
    }
}
