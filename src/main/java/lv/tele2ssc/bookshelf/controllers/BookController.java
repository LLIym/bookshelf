package lv.tele2ssc.bookshelf.controllers;

import java.io.IOException;
import lv.tele2ssc.bookshelf.services.BookService;
import javax.validation.Valid;
import lv.tele2ssc.bookshelf.model.Book;
import lv.tele2ssc.bookshelf.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller class. Controller class is processing requests from the user.
 */
@Controller
public class BookController {

    private static final Logger logger
            = LoggerFactory.getLogger(BookController.class);

    // We will user book service 
    @Autowired
    private BookService bookService;
    @Autowired
    private ImageService imageService;

    @RequestMapping(path = "/book", method = RequestMethod.GET)
    public String page(@RequestParam long bookId, Model model) {
        logger.debug("Book id {} is requested", bookId);

        Book book = bookService.findById(bookId);

        // put book to the model to use from the template
        model.addAttribute("book", book);
        return "book";
    }

    @RequestMapping(path = "/edit-book", method = RequestMethod.GET)
    public String edit(@RequestParam long bookId, Model model) {
        logger.debug("Edit Book id {} is requested", bookId);
        Book book = bookService.findById(bookId);

        model.addAttribute("book", book);
        return "edit-book";
    }

    @RequestMapping(path = "/edit-book", method = RequestMethod.POST)
    public String edit(@Valid Book book, BindingResult bindingResult, @RequestParam MultipartFile image, Model model) {
        // checks whether edited book has validation errors
        if (bindingResult.hasErrors()) {
            return "edit-book";
        }
        logger.debug("Saving book {}", book);
        
        storeImage(book, image, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return null;
        }

        bookService.save(book);

        model.addAttribute("book", book);
        return "redirect:/book?bookId=" + book.getId();
    }

    private void storeImage(Book book, MultipartFile image, BindingResult bindingResult) {
        if (image.isEmpty()) {
            logger.debug("No image uploded preserving previous");
            Book unchanged = bookService.findById(book.getId());
            book.setImageFileName(unchanged.getImageFileName());
        } else {
            logger.debug("Storing uploaded image {}", image.getOriginalFilename());
            book.setImageFileName(image.getOriginalFilename());
            try {
                imageService.store(book, image);
            } catch (IOException e) {
                logger.warn("Cannot save image", e);
                bindingResult.reject("image");
            }
        }
    }

    @RequestMapping(path = "/book-image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getBookImage(@RequestParam long bookId) {
        Resource file = imageService.loadImageAsResource(bookId);
        if (file == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
    }

}
