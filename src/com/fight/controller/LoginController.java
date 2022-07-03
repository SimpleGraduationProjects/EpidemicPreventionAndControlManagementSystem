package com.fight.controller;

import com.fight.controller.LoginModel;

import com.fight.dao.*;

import com.fight.model.*;

import com.fight.util.*;
import com.fight.util.CommonVal;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/commonapi")
public class LoginController {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    VolunteerInfoMapper volunteerInfoMapper;
    @Autowired
    AdminInfoMapper adminInfoMapper;
    @Autowired
    HospitalInfoMapper hospitalInfoMapper;

    /**
      系统进入登录页面接口
    */
    @RequestMapping(value = "sys_login")
    public String sys_login(ModelMap modelMap, String msg) {
        modelMap.addAttribute("msg", msg);

        return "sys_login";
    }

    /**
      系统退出接口
    */
    @RequestMapping(value = "sys_logout")
    public String sys_logout(ModelMap modelMap, String msg,
        HttpServletRequest request) {
        request.getSession().removeAttribute(CommonVal.sessionName);

        return "redirect:/commonapi/sys_login";
    }

    /**
      系统提交登录验证信息接口
    */
    @RequestMapping("sysSubmit")
    @ResponseBody
    public Object sysSubmit(LoginModel user, String imgCode, ModelMap modelMap,
        HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Map<String, Object> rs = new HashMap<String, Object>();

        //图片验证码验证,从session中查询验证码并校验
        if ((imgCode == null) || imgCode.trim().equals("")) {
            rs.put("code", 0);
            rs.put("msg",
                "请输入图片验证码");

            return rs;
        }

        if (!imgCode.toLowerCase()
                        .equals(request.getSession().getAttribute(CommonVal.code)
                                           .toString().toLowerCase())) {
            rs.put("code", 0);
            rs.put("msg",
                "图片验证码错误");

            return rs;
        }

        if (user.getLoginType() == null) {
            rs.put("code", 0);
            rs.put("msg",
                "请选择登录角色");

            return rs;
        }

        if ((user.getName() == null) || user.getName().equals("")) {
            rs.put("code", 0);
            rs.put("msg",
                "请输入登录名");

            return rs;
        }

        if ((user.getPassword() == null) || user.getPassword().equals("")) {
            rs.put("code", 0);
            rs.put("msg",
                "请输入密码");

            return rs;
        }

        if (user.getLoginType() == 1) {
            AdminInfoExample te = new AdminInfoExample(); //验证管理员账号密码
            AdminInfoExample.Criteria tc = te.createCriteria();
            tc.andNameEqualTo(user.getName());
            tc.andPassWordEqualTo(user.getPassword());

            List<AdminInfo> tl = adminInfoMapper.selectByExample(te);

            if (tl.size() == 0) { //从数据库中查询不到账号
                rs.put("code", 0);
                rs.put("msg",
                    "登录名或密码错误");

                return rs;
            } else {
                LoginModel login = new LoginModel();
                login.setId(tl.get(0).getId());
                login.setLoginType(1);
                login.setName(user.getName());
                request.getSession().setAttribute(CommonVal.sessionName, login); //设置登录session,保持会话
                rs.put("code", 1);
                rs.put("msg",
                    "登录成功");

                return rs;
            }
        }

        if (user.getLoginType() == 2) {
            HospitalInfoExample te = new HospitalInfoExample(); //验证医院账号密码
            HospitalInfoExample.Criteria tc = te.createCriteria();
            tc.andNameEqualTo(user.getName());
            tc.andPassWordEqualTo(user.getPassword());

            List<HospitalInfo> tl = hospitalInfoMapper.selectByExample(te);

            if (tl.size() == 0) { //从数据库中查询不到账号
                rs.put("code", 0);
                rs.put("msg",
                    "登录名或密码错误");

                return rs;
            } else {
                LoginModel login = new LoginModel();
                login.setId(tl.get(0).getId());
                login.setLoginType(2);
                login.setName(user.getName());
                request.getSession().setAttribute(CommonVal.sessionName, login); //设置登录session,保持会话
                rs.put("code", 1);
                rs.put("msg",
                    "登录成功");

                return rs;
            }
        }

        if (user.getLoginType() == 3) {
            CompanyInfoExample te = new CompanyInfoExample(); //验证厂商账号密码
            CompanyInfoExample.Criteria tc = te.createCriteria();
            tc.andNameEqualTo(user.getName());
            tc.andPassWordEqualTo(user.getPassword());

            List<CompanyInfo> tl = companyInfoMapper.selectByExample(te);

            if (tl.size() == 0) { //从数据库中查询不到账号
                rs.put("code", 0);
                rs.put("msg",
                    "登录名或密码错误");

                return rs;
            } else {
                LoginModel login = new LoginModel();
                login.setId(tl.get(0).getId());
                login.setLoginType(3);
                login.setName(user.getName());
                request.getSession().setAttribute(CommonVal.sessionName, login); //设置登录session,保持会话
                rs.put("code", 1);
                rs.put("msg",
                    "登录成功");

                return rs;
            }
        }

        if (user.getLoginType() == 4) {
            VolunteerInfoExample te = new VolunteerInfoExample(); //验证志愿者账号密码
            VolunteerInfoExample.Criteria tc = te.createCriteria();
            tc.andNameEqualTo(user.getName());
            tc.andPassWordEqualTo(user.getPassword());

            List<VolunteerInfo> tl = volunteerInfoMapper.selectByExample(te);

            if (tl.size() == 0) { //从数据库中查询不到账号
                rs.put("code", 0);
                rs.put("msg",
                    "登录名或密码错误");

                return rs;
            } else {
                LoginModel login = new LoginModel();
                login.setId(tl.get(0).getId());
                login.setLoginType(4);
                login.setName(user.getName());
                request.getSession().setAttribute(CommonVal.sessionName, login); //设置登录session,保持会话
                rs.put("code", 1);
                rs.put("msg",
                    "登录成功");

                return rs;
            }
        }

        rs.put("code", 0);
        rs.put("msg",
            "系统出错");

        return rs;
    }
}

