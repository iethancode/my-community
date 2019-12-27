package xyz.peypey.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.peypey.community.dto.AccessTokenDTO;
import xyz.peypey.community.dto.GithubUser;
import xyz.peypey.community.utils.GithubUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


@Controller
public class AuthorizeController {
    @Autowired
    private  GithubUtil githubUtil;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String token = githubUtil.getAccessToken(accessTokenDTO);
        GithubUser user = githubUtil.getUser(token);
//         System.out.println(user.getName());
        if (user!=null){
           request.getSession().setAttribute("user", user);//登录成功
            return "redirect:/";
        }else{
            return "redirect:/";//登录失败
        }

    }
}
