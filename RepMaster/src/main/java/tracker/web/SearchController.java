package tracker.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tracker.model.Gym;
import tracker.model.Rateable;
import tracker.model.RegisteredUser;
import tracker.service.GymService;
import tracker.service.RegisteredUserService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final GymService gymService;
    private final RegisteredUserService registeredUserService;
    private boolean searched = false;
    private List<Rateable> rateableList;

    @GetMapping("/search")
    public String search(Map<String, Object> model){
        if(!searched) rateableList = registeredUserService.SearchGymBySplit(null, null);
        model.put("elements", rateableList);
        model.put("search", new Search());
        return "search";
    }

    @PostMapping("/search")
    public String r_desc(Search search) {
        searched=true;
        rateableList = registeredUserService.SearchGymBySplit(search.searchBar, search.order);
        return "redirect:/search";
    }

    @Getter
    @Setter
    class Search{
        private String searchBar;
        private String order;
    }
}
