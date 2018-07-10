package com.tom.web.areas.user.controllers;

import com.google.common.base.Strings;
import com.tom.web.areas.AdminControllerBase;
import com.tom.web.areas.user.models.UserLoginInputDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController extends AdminControllerBase {
    /**
     * 登录方法
     *
     * @param returnUrl
     * @param model
     * @return
     */
    @RequestMapping(value = "/login.html")
    public String login(@RequestParam(required = false) String returnUrl, Model model) {
        model.addAttribute("returnurl", returnUrl);
        return "admin/login.html";
    }

    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    public String login(@RequestBody @Validated UserLoginInputDto input, @RequestParam(required = false) String
            returnUrl, BindingResult bindingResult, Model model) {
        List checkResult = CheckModelStatus(bindingResult);
        if (checkResult.size() > 0) {
            model.addAttribute("errormsg", checkResult.get(0));
        }
        //身份信息校验,委托给shiro 进行认证
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(input.getUserName(), input
                .getPassWord());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
            if (Strings.isNullOrEmpty(returnUrl)) {
                return "admin/index.html";
            } else {
                return "forward:" + returnUrl;
            }
        } catch (AuthenticationException e) {
            model.addAttribute("errormsg", "账号或密码错误");
            return "admin/login.html";
        }
    }
}
