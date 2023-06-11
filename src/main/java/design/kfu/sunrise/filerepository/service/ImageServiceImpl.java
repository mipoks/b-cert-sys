package design.kfu.sunrise.filerepository.service;

import com.jlefebure.spring.boot.minio.MinioException;
import design.kfu.sunrise.domain.event.ClubEvent;
import design.kfu.sunrise.domain.event.CommentEvent;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.filerepository.exception.FileServiceException;
import design.kfu.sunrise.service.access.AccountAccessService;
import design.kfu.sunrise.service.comment.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {


    @Autowired
    private AccountAccessService accountAccessService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ApplicationEventPublisher publisher;

    private CustomMinioService minioService;
    private Tika tika;

    private static final List<String> ALLOWED_FORMAT = List.of("image/jpeg", "image/png");

    @Autowired
    public ImageServiceImpl(CustomMinioService minioService) {
        this.minioService = minioService;
        this.tika = new Tika();
    }

    public String checkImageFormat(MultipartFile multipartFile) throws IOException {
        String fileType = tika.detect(multipartFile.getBytes());
        boolean contains = ALLOWED_FORMAT.contains(fileType);
        if (!contains) {
            throw new FileServiceException("File type must be " + ALLOWED_FORMAT.stream().collect(Collectors.joining(", ")));
        }
        return fileType;
    }

    public String makeFileName(Path path, String fileType) {
        return switch (fileType) {
            case "image/jpeg" -> path.getFileName() + ".jpeg";
            case "image/png" -> path.getFileName() + ".png";
            default -> path.getFileName().toString();
        };
    }

    public String makeFileName(String fileName, String fileType) {
        return switch (fileType) {
            case "image/jpeg" -> fileName + ".jpeg";
            case "image/png" -> fileName + ".png";
            default -> fileName.toString();
        };
    }

    @Override
    public Boolean upload(Path path, MultipartFile multipartFile) {
        try {
            String fileType = checkImageFormat(multipartFile);
            minioService.upload(path, multipartFile.getInputStream());
            return true;
        } catch (Exception e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String addImageForClub(Club club, MultipartFile multipartFile) {
        try {
            String fileType = checkImageFormat(multipartFile);
            String fileName = getFreeFileName(club);
            String filePath = makeFileName(fileName, fileType);
            Path path = Path.of(filePath);
            minioService.upload(path, multipartFile.getInputStream());
            club.getClubInfo().getImages().add(filePath);
            publisher.publishEvent(
                    new ClubEvent(Club.class.getName(), ClubEvent.Event.IMAGE_ADD.getName(), club)
            );
            return getSecuredImageUrl(path);
        } catch (Exception e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String addImageForComment(Comment comment, MultipartFile multipartFile) {
        try {
            String fileType = checkImageFormat(multipartFile);
            String fileName = getFreeFileName(comment);
            String filePath = makeFileName(fileName, fileType);
            Path path = Path.of(filePath);
            minioService.upload(path, multipartFile.getInputStream());
            comment.getImages().add(filePath);
            publisher.publishEvent(
                    new CommentEvent(Comment.class.getName(), CommentEvent.Event.IMAGE_ADD.getName(), comment)
            );
            return getSecuredImageUrl(path);
        } catch (Exception e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Boolean remove(Club club, Path image) {
        try {
            minioService.remove(image);
            club.getClubInfo().getImages().remove(image.toString());
            publisher.publishEvent(
                    new ClubEvent(Club.class.getName(), ClubEvent.Event.IMAGE_REMOVE.getName(), club)
            );
            return true;
        } catch (MinioException e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }

    private String getFreeFileName(Club club) {
        List<String> images = club.getClubInfo().getImages();
        int index = 0;
        if (images.size() > 0) {
            String last = images.get(images.size() - 1);
            index = Integer.parseInt(last.split("_")[1].split("\\.")[0]);
        }
        return "cl" + club.getId() + "_" + ++index;
    }

    private String getFreeFileName(Comment comment) {
        List<String> images = comment.getImages();
        int index = 0;
        if (images.size() > 0) {
            String last = images.get(images.size() - 1);
            index = Integer.parseInt(last.split("_")[1].split("\\.")[0]);
        }
        return "cm" + comment.getId() + "_" + ++index;
    }

    @Override
    public String getSecuredImageUrl(Path of) {
        try {
            return minioService.getSignedUrl(of);
        } catch (MinioException e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<InputStream> getUnsecuredImage(Path of, Account account) {
        try {
            String path = of.toString();
            if (path.startsWith("cm")) {
                Long commentId = Long.parseLong(path.substring(2).split("_")[0]);
                Comment comment = commentService.findOrThrow(commentId);
                boolean access = false;
                if (account != null) {
                    access = accountAccessService.hasAccessToReadComment(account, comment.getClub());
                }
                if (access) {
                    return Optional.of(minioService.get(of));
                }
            } else {
                return Optional.of(minioService.get(of));
            }
        } catch (MinioException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<String> getSecuredImageUrls(Path of) {
        try {
            return List.of(minioService.getSignedUrl(of));
        } catch (MinioException e) {
            throw new FileServiceException(e.getMessage(), e.getCause());
        }
    }
}
