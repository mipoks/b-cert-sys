package design.kfu.sunrise.filerepository.service;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
public interface ImageService {

    Boolean upload(Path path, MultipartFile multipartFile);

    String addImageForClub(Club club, MultipartFile multipartFile);
    String addImageForComment(Comment comment, MultipartFile multipartFile);

    Boolean remove(Club club, Path image);

    String getSecuredImageUrl(Path of);

    Optional<InputStream> getUnsecuredImage(Path of, Account account);

    List<String> getSecuredImageUrls(Path image);
}
