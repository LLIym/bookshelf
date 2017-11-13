package lv.tele2ssc.bookshelf.controllers.rest;

import java.util.List;
import lv.tele2ssc.bookshelf.controllers.SearchBookController;
import lv.tele2ssc.bookshelf.model.Book;
import lv.tele2ssc.bookshelf.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dima
 */
@RestController
public class SearchRestController {

    private static final Logger logger
            = LoggerFactory.getLogger(SearchRestController.class);

    @Autowired
    private BookService bookService;

    @RequestMapping(path = "/api/search", method = RequestMethod.GET)
    public List<Book> search(@RequestParam String term) {
        logger.debug("REST: searching for {}", term);

        List<Book> list = bookService.findByTerm(term);
        
        return list;
    }

}
