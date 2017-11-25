package lv.tele2ssc.bookshelf.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lv.tele2ssc.bookshelf.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Value("${image.storage.path}")
    private String imageStoragePath;
    @Autowired
    private BookService bookService;

    public Resource loadImageAsResource(long bookId) {
        Book book = bookService.findById(bookId);
        String imageFileName = book.getImageFileName();
        Path resourceFilePath = imageFileName == null ? null : Paths.get(imageStoragePath, String.valueOf(bookId), imageFileName);
        if (resourceFilePath == null || !Files.exists(resourceFilePath)) {
            return null;
        }
        Resource result = new FileSystemResource(resourceFilePath.toFile());
        return result;
    }
    
}
