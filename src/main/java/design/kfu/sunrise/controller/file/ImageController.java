package design.kfu.sunrise.controller.file;

import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.filerepository.service.CustomMinioService;
import design.kfu.sunrise.filerepository.service.ImageService;
import design.kfu.sunrise.requestlimiter.annotation.Limit;
import design.kfu.sunrise.requestlimiter.domain.limitation.LimitType;
import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.security.details.UserDetailsImpl;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.security.PermitAll;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    public static final String CLUB_PREFIX = "club/";

    @Autowired
    private ImageService imageService;

    @Operation(description = "Get image URL to Minio by club ID", summary = "Image URL is expirable, so you should get image again after some time")
    @PermitAll
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @GetMapping("/club/{club_id}")
    public List<String> getClubImage(@PathVariable("club_id") Long clubId) {
        return imageService.getSecuredImageUrls(Path.of(CLUB_PREFIX + clubId));
    }

    @Operation(description = "Get image URL to Minio by image's path", summary = "Image URL is expirable (after 24 hours), so you should get image again after some time")
    @PermitAll
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @GetMapping("/static/{image_path}")
    public ResponseEntity<Object> getImage(@PathVariable("image_path") String imagePath, Principal principal) throws IOException {
        Account account = null;
        try {
            account = ((UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getAccount();
        } catch (Exception ignored) {}
        Optional<InputStream> inputStreamOptional = imageService.getUnsecuredImage(Path.of(imagePath), account);
        if (inputStreamOptional.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(IOUtils.toByteArray(inputStreamOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(description = "Get image URL to Minio by image's path", summary = "Image URL is expirable (after 24 hours), so you should get image again after some time")
    @PermitAll
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @GetMapping("/{image_path}")
    public String getClubImage(@PathVariable("image_path") String imagePath) {
        return imageService.getSecuredImageUrl(Path.of(imagePath));
    }

    //ToDo сделать валидацию на то, что размер XXX на XXX
    @Operation(description = "Save image URL to Minio by club ID", summary = "Returns true if successfully saved")
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @PreAuthorize("@access.hasAccessToCreateClub(#account)")
    @PostMapping("/club/{club_id}")
    public String saveClubImage(@PathVariable("club_id") Club club,
                                          @RequestParam("file") MultipartFile multipartFile,
                                          @AuthenticationPrincipal(expression = "account") Account account) {
        return imageService.addImageForClub(club, multipartFile);
    }

    @Operation(description = "Remove image from Minio and club by path")
    @PreAuthorize("@access.hasAccessToEditClub(#account, #club)")
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @DeleteMapping("/club/{club_id}/{image_path}")
    public Boolean removeImage(@PathVariable("image_path") String imagePath, @PathVariable("club_id") Club club, @AuthenticationPrincipal(expression = "account") Account account) {
        return imageService.remove(club, Path.of(imagePath));
    }

    @Operation(description = "Save image URL to Minio by club ID", summary = "Returns true if successfully saved")
    @Limit(url = "/image/club", limitType = LimitType.PER_MINUTE, requestCount = 15)
    @PreAuthorize("@access.hasAccessToEditComment(#comment, #account)")
    @PostMapping(value = "/comment/{comment_id}", headers = "content-type=multipart/*")
    public String saveCommentImage(@PathVariable("comment_id") Comment comment, @AuthenticationPrincipal(expression = "account") Account account,
                                @RequestParam("file") MultipartFile multipartFile
                                ) {
        return imageService.addImageForComment(comment, multipartFile);
    }
}
