package cn.chat.room.site.controller;

import cn.chat.room.site.form.UserForm;
import cn.chat.room.site.pojo.User;
import cn.chat.room.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/login")
    @ResponseBody
    public ModelAndView findUserByName(UserForm form, Map<String, String> map, HttpServletResponse response) {
        User user = userService.findUserByName(form);
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=gb2312");
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (user != null) {
                map.put("username", form.getUsername());
                //登陆成功，重定向聊天页面
                return new ModelAndView("chat", map);
            } else {
                //失败重定向登录页面
                out.print("<script language=\"javascript\">alert('用户名或密码错误，请重试！');</script>");

                return new ModelAndView("index");
            }
        }
    }
}
