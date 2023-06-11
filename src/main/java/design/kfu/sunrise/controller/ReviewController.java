package design.kfu.sunrise.controller;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.util.Review;
import design.kfu.sunrise.service.category.CategoryService;
import design.kfu.sunrise.service.club.ClubService;
import design.kfu.sunrise.service.comment.CommentService;
import design.kfu.sunrise.service.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * @author Daniyar Zakiev
 */
@RestController
@RequestMapping(value = "/v1")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private CategoryService categoryService;


    @Operation(description = "Get reviews", summary = "Returns Page of needed reviews")
    @PreAuthorize("@access.hasAccessToReadReviews(#account)")
    @GetMapping("/reviews")
    public Page<Review> getReviews(@RequestParam(value = "of", defaultValue = "all") String all,
                                   @RequestParam(value = "size", defaultValue = "20") int size,
                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                   @AuthenticationPrincipal(expression = "account") Account account) {
        Pageable pageable = PageRequest.of(page, size);
        return switch (all) {
            case "comment" -> reviewService.findReviewsForComments(pageable);
            case "club" -> reviewService.findReviewsForClubs(pageable);
            case "category" -> reviewService.findReviewsForCategories(pageable);
            default -> reviewService.findReviews(pageable);
        };
    }


    //ToDo Сделать ReviewDTO main
    @Operation(description = "Make comment review", summary = "Returns boolean")
    @PreAuthorize("@access.hasAccessToMakeReview(#account, #review, \"comment\")")
    @PutMapping("/review/{review_id}/comment")
    public boolean createReviewComment(@Valid @RequestBody Review reviewResult, @PathVariable("review_id") Review review,
                                     @AuthenticationPrincipal(expression = "account") Account account) {
        review.setAccept(reviewResult.isAccept());
        review.setReason(review.getReason());

        Comment comment = commentService.findOrThrow(review.getObjectId());
        return reviewService.reviewComment(comment, review);
    }


    @Operation(description = "Make club review", summary = "THIS METHOD IS NOT NECESSARY NOW")
    @PreAuthorize("@access.hasAccessToMakeReview(#account, #review, \"club\")")
    @PutMapping("/review/{review_id}/club")
    public boolean makeReviewClub(@Valid @RequestBody Review reviewResult, @PathVariable("review_id") Review review, @AuthenticationPrincipal(expression = "account") Account account) {
        review.setAccept(reviewResult.isAccept());
        review.setReason(review.getReason());
        Club club = clubService.findOrThrow(review.getObjectId());
        return reviewService.reviewClub(club, review);
    }

    @Operation(description = "Make category review", summary = "THIS METHOD IS NOT NECESSARY NOW")
    @PreAuthorize("@access.hasAccessToMakeReview(#account, #review, \"category\")")
    @PutMapping("/review/{review_id}/category")
    public boolean makeReviewCategory(@Valid @RequestBody Review reviewResult, @PathVariable("review_id") Review review, @AuthenticationPrincipal(expression = "account") Account account) {
        review.setAccept(reviewResult.isAccept());
        review.setReason(review.getReason());

        Category category = categoryService.findOrThrow(review.getObjectId());
        return reviewService.reviewCategory(category, review);
    }
}
