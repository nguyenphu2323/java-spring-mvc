package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Controller
// public class UserController {
//     @RequestMapping("/")
//     public String getHomePage() {
//         return "hello from Controller";
//     }
// }

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUser = this.userService.getUserByEmailAndAddress("");
        System.out.println(arrUser);

        model.addAttribute("arrUser", arrUser);
        model.addAttribute("hoidanit", "from controller with model");
        return "Hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users", users);
        return "admin/user/show";
    }

    @RequestMapping("admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserByID(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @RequestMapping("admin/user/create")
    public String getUserCreatePage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "admin/user/create", method = RequestMethod.POST)
    public String createUserPage(Model modelm, @ModelAttribute("newUser") User phu) {
        this.userService.handleSaveUser(phu);
        return "redirect:/admin/user";
    }

    @RequestMapping("admin/user/update/{id}")
    public String getUserUpdatePage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserByID(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User phu) {
        User currentUser = this.userService.getUserByID(phu.getId());

        if (currentUser != null) {
            currentUser.setFullName(phu.getFullName());
            currentUser.setAddress(phu.getAddress());
            currentUser.setPhone(phu.getPhone());

            this.userService.handleSaveUser(currentUser);

        }
        return "redirect:/admin/user";
    }

    @GetMapping("admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User phu) {
        this.userService.deleteUserByID(phu.getId());
        return "redirect:/admin/user";
    }

}
