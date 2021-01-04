package fr.yncrea.cir3.othello.controller;

import fr.yncrea.cir3.othello.domain.Game;
import fr.yncrea.cir3.othello.exception.InvalidMoveException;
import fr.yncrea.cir3.othello.repository.GameRepository;
import fr.yncrea.cir3.othello.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GameController {
    @Autowired
    private GameRepository repository;

    @Autowired
    private GameService service;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/create")
    public String create() {
        Game game = repository.save(service.create());

        return "redirect:/game/" + game.getId();
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable Long id, Model model) {
        Game game = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("game", game);

        return "game";
    }

    @GetMapping("/game/{id}/{row}/{col}")
    public String play(@PathVariable Long id, @PathVariable int row, @PathVariable int col, RedirectAttributes attribs) {
        Game game = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            repository.save(service.play(game, row, col));
        } catch (InvalidMoveException e) {
            attribs.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/game/" + id;
    }
}
