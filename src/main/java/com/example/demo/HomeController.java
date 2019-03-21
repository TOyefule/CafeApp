package com.example.demo;

//import com.cloudinary.utils.ObjectUtils;
//import com.example.demo.beans.Menu;
//import com.example.demo.beans.User;
//import com.example.demo.configurations.CloudinaryConfig;
//import com.example.demo.repositories.MenuRepository;
//import com.example.demo.service.UserService;
//import org.hibernate.validator.constraints.SafeHtml;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import java.io.IOException;
//import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.beans.Menu;
import com.example.demo.beans.User;
import com.example.demo.configurations.CloudinaryConfig;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;



@Controller
public class HomeController {

 @Autowired
  UserService userService;

  @Autowired
  CloudinaryConfig cloudc;

  @Autowired
  MenuRepository menurepository;

  @RequestMapping("/")
  public String list(Model model){
    model.addAttribute("menu", menurepository.findAll());

    return "Menulist";
  }

  @RequestMapping("/login")
  public String login(){

    return "login";
  }

  @GetMapping("/add")
  public String cafeform(Model model){
    model.addAttribute("menu", new Menu());

    return "cafeform";
  }

  @PostMapping("/process")
  public String processForm(@Valid @ModelAttribute Menu menu, BindingResult result,
                            @RequestParam("file") MultipartFile file){
    if (file.isEmpty()) {
      return "redirect:/add";
    }
    if (result.hasErrors()){
      return "cafeform";
    }try {
      Map uploadResult = cloudc.upload(file.getBytes(),
              ObjectUtils.asMap("resourcetype", "auto"));
      menu.setImage(uploadResult.get("url").toString());
      menurepository.save(menu);
    }catch (IOException e) {
      e.printStackTrace();
      return "redirect:/add";
    }
//    menurepository.save(menu);
    return "redirect:/";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String showRegistrationPage(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @RequestMapping(value="/register", method=RequestMethod.POST)
  public String processRegistrationPage(@Valid @ModelAttribute("user")
                                                User user, BindingResult
                                                result,
                                        Model model) {
    model.addAttribute("user", new User());
    if (result.hasErrors()) {
      return "registration";
    }
    else {
      userService.saveUser(user);
      model.addAttribute("message", "User Account Successfully Created");
    }
    return "Menulist";
  }



}
