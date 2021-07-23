package cn.hnust.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.hnust.util.VFSUtil;

@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {
    @RequestMapping("/showImg")
    public void showImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = get(request, "path");
        VFSUtil.showVFSImage(request, response, path);
    }
}
