package design.kfu.sunrise;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.util.Review;
import design.kfu.sunrise.repository.AccountRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.repository.CommentRepository;
import design.kfu.sunrise.service.comment.CommentService;
import design.kfu.sunrise.service.review.ReviewService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewServiceTests {

    /*
    void addCommentForReview(Comment comment);
    void addCategoryForReview(Category category);
    void addClubForReview(Club club);

    Page<Review> findReviewsForComments(Pageable pageable);
    Page<Review> findReviewsForClubs(Pageable pageable);
    Page<Review> findReviewsForCategories(Pageable pageable);
    Page<Review> findReviews(Pageable pageable);

    boolean reviewComment(Comment comment, Review review);
    boolean reviewCategory(Category category, Review review);
    boolean reviewClub(Club club, Review review);
     */

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClubRepository clubRepository;

    Club getClubFromRepository() {
        return clubRepository.findAll().stream().findFirst().orElseThrow();
    }

    Account getAccountFromRepository() {
        return accountRepository.findAll().stream().findFirst().orElseThrow();
    }

    Comment getCommentFromRepository() {
        return commentRepository.findById(4L).get();
    }

    @Test
    @Order(1)
    void addCommentForReviewTest() {
        Page<Review> reviewsForComments = reviewService.findReviewsForComments(PageRequest.of(0, 100));
        long totalElements = reviewsForComments.getTotalElements();
        Comment comment = getCommentFromRepository();
        reviewService.addCommentForReview(comment);

        Page<Review> reviewsForComments2 = reviewService.findReviewsForComments(PageRequest.of(0, 100));

        Assertions.assertEquals(totalElements + 1, reviewsForComments2.getTotalElements());
    }

    @Test
    void addClubForReviewTest() {
        Club club = getClubFromRepository();
        reviewService.addClubForReview(club);
        Page<Review> reviewsForClubs = reviewService.findReviewsForClubs(PageRequest.of(0, 100));
        Assertions.assertTrue(reviewsForClubs.hasContent());
    }

    @Test
    void findReviewsForCommentsTest() {
        Page<Review> reviewsForComments = reviewService.findReviewsForComments(PageRequest.of(0, 100));
        Assertions.assertTrue(reviewsForComments.hasContent());
    }

    @Test
    void findReviewsForClubsTest() {
        Page<Review> reviewsForClubs = reviewService.findReviewsForClubs(PageRequest.of(0, 100));
        Assertions.assertTrue(reviewsForClubs.hasContent());
    }

    @Test
    void findReviewsTest() {
        Page<Review> reviews = reviewService.findReviews(PageRequest.of(0, 100));
        Assertions.assertTrue(reviews.hasContent());
    }

}
