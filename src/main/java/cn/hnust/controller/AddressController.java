package cn.hnust.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hnust.domain.Address;
import cn.hnust.service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController extends BaseController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("/to_add")
    public String toAdd(ModelMap m) {
        m.put("address", new Address());
        return "address/add";
    }

    @RequestMapping("/save")
    public String save(Address address, HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        try {
            addressService.save(address);
            msg.setSuccessMsg("保存成功");
        } catch (Exception e) {
            msg.setErrorMsg("保存失败");
        }
        m.put("msg", msg);
        return "common/jump";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("addresses", addressService.queryByCurrentUser());
        return resultMap;
    }

    @RequestMapping("remove")
    @ResponseBody
    public Map<String, Object> remove(HttpServletRequest request, HttpServletResponse response) {
        String id = get(request, "id");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            addressService.remove(id);
            msg.setSuccessMsg("删除成功");
        } catch (Exception e) {
            msg.setErrorMsg("删除失败");
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    @RequestMapping("to_edit")
    public String toEdit(HttpServletRequest request, HttpServletResponse response, ModelMap m) {
        String id = get(request, "id");
        m.put("address", addressService.findById(id));
        return "address/add";
    }
}
