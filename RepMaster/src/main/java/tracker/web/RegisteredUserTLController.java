package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.model.RegisteredUser;
import tracker.service.RegisteredUserService;

import java.util.Map;

/**
 * Controller osztály a login.hmtl és registration.html-ekhez, ezeken történik a bejelentkezés, illetve felhasználó regisztrálás
 */
@Controller
@RequiredArgsConstructor
public class RegisteredUserTLController {
    /**
     * a regisztrált felhasználó service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final RegisteredUserService registeredUserService;
    /**
     * rendszerüzenet, amit gombnyomások után kiír
     */
    private String msg = "";

    /**
     * létrehozza a regisztrációs htmlt, kiírja ha valami nem stimmel a regisztrációval
     * @param model model
     * @return html neve
     */
    @GetMapping("/registration")
    public String registration(Map<String, Object> model){
        model.put("msg", msg);
        model.put("newRegisteredUser", new RegisteredUser());
        msg="";
        return "registration";
    }

    /**
     * egy felhasználó regisztrálása, ha a felhasználónév már létezik, akkor sikertelen
     * @param registeredUser a regisztráló felhasználó
     * @return visszairányít a regisztrációs oldalra, ahol kiírja egy sikeres/sikertelen regisztrációs üzenetet
     */
    @PostMapping("/newRegisteredUser")
    public String register(RegisteredUser registeredUser) {
        msg = registeredUserService.addRegisteredUser(registeredUser);
        return "redirect:/registration";
    }

    /**
     * létrehozza a bejelentkezési htmlt, kiírja ha valami nem stimmel a bejelentkezéssel
     * @param model model
     * @return html neve
     */
    @GetMapping("/")
    public String login(Map<String, Object> model){
        model.put("msg", msg);
        model.put("loginUser", new RegisteredUser());
        model.put("visitor", null);
        msg="";
        return "login";
    }

    /**
     * egy regisztrált felhasználó bejelentkezése, ha a felhasználónév vagy jelszó nem helyes, akkor sikertelen
     * @param registeredUser a bejelentkező felhasználó
     * @return ha sikertelen visszairányít a bejelentkezési oldalra, ahol kiírja mi a hiba. ha sikeres akkor betölti a felhasználó edzőtermeit
     */
    @PostMapping("/loginUser")
    public String login(RegisteredUser registeredUser) {
        msg = registeredUserService.loginUser(registeredUser.getUserName(), registeredUser.getPassword());
        if(msg.contains("failed"))
            return "redirect:/";
        return "redirect:/gyms";
    }

    /**
     * egy felhasználó nem regisztrál, csak böngészni akar a publikus edzőtermek/edzőtervek/gyakorlatok között. előbb kijelentkezteti a bejelentkezett felhasználót
     * @return átirányítja a keresés oldalra
     */
    @PostMapping("/visitor")
    public String visitor() {
        registeredUserService.singOutUser();
        return "redirect:/search";
    }
}
