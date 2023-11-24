package tracker.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tracker.model.RegisteredUser;
import tracker.service.RegisteredUserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final RegisteredUserService registeredUserService;

    @GetMapping("/search")
    public String search(Map<String, Object> model){
        return "search";
    }
}
