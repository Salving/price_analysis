package teamworks.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamworks.server.domain.User;
import teamworks.server.repository.UserRepository;

import java.util.Objects;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @RequestMapping("/register")
    public String register(@RequestParam("login")String login,
                           @RequestParam("mail") String mail,
                           @RequestParam("password") String password) {
        if (userRepository.existsByLogin(login)) {
            return "Login taken";
        } else if (userRepository.existsByMail(mail)) {
            return "Mail taken";
        } else {
            userRepository.save(new User(login, mail, password));
            return "User registered";
        }
    }

    @ResponseBody
    @RequestMapping("/auth")
    public String auth(@RequestParam("user") String user,
                       @RequestParam("password") String password) {
        if (userRepository.existsByLogin(user)) {
            if (Objects.equals(userRepository.findByLogin(user).getPassword(), password)) {
                return "User authorized";
            }
        } else if (userRepository.existsByMail(user)) {
            if (Objects.equals(userRepository.findByMail(user).getPassword(), password)) {
                return "User authorized";
            }
        } else {
            return "User not found";
        }
        return "0";
    }
}
