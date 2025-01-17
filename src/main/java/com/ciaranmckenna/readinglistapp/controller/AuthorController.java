package com.ciaranmckenna.readinglistapp.controller;

import com.ciaranmckenna.readinglistapp.dao.entity.Author;
import com.ciaranmckenna.readinglistapp.dao.entity.Book;
import com.ciaranmckenna.readinglistapp.exceptions.NotFoundException;
import com.ciaranmckenna.readinglistapp.dto.AuthorRecord;
import com.ciaranmckenna.readinglistapp.service.ReadingListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/author/")
@Controller
public class AuthorController {

    private final ReadingListService readingListService;

    public AuthorController(ReadingListService readingListService) {
        this.readingListService = readingListService;
    }

    @GetMapping("list")
    public String findAllAuthors(Model model){
        List<AuthorRecord> authorRecordList = readingListService.findAllAuthors();
        model.addAttribute("author", authorRecordList);
        return "authors/list-authors";
    }

    @GetMapping("{id}")
    public String findAuthorById(@PathVariable int id, Model model) throws NotFoundException {
        Author authorById = readingListService.findAuthorById(id);
        if (authorById != null){
            model.addAttribute("author", authorById);
        }
        return "authors/list-authors";
    }

    @GetMapping("search")
    public String showFormForSearch(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "authors/author-search";
    }

    @GetMapping("name")
    public String findAuthorByNameContaining(@RequestParam String firstName, @RequestParam String lastName, Model model){

        if (firstName.isEmpty() && lastName.isEmpty()) {
            firstName = ""; // empty String signifies broadest possible search
            lastName = ""; // empty String signifies broadest possible search

        }
        List<AuthorRecord> authorNameContaining = readingListService.findByAuthorNameContainingIgnoreCase(firstName, lastName);
        model.addAttribute("author", authorNameContaining);
        return "authors/list-authors";
    }

    @GetMapping("registration")
    public String showFormForAdd(Model model){
        Author author = new Author();
        model.addAttribute("author", author);
        return "authors/author-form";
    }

    @PostMapping("add")
    public String addAuthor(@ModelAttribute("author") Author author){
        readingListService.addAuthor(author);
        return "redirect:/author/list";
    }

    @GetMapping("update/{authorId}")
    public String updateAuthor(@PathVariable("authorId") int id, Model model) throws NotFoundException {
        Author authorById = readingListService.findAuthorById(id);
        model.addAttribute("author", authorById);
        return "authors/author-form";
    }

    @GetMapping("delete/{authorId}")
    public String deleteAuthor(@PathVariable("authorId") int id){
        readingListService.deleteAuthorById(id);
        return "redirect:/author/list";
    }

}
