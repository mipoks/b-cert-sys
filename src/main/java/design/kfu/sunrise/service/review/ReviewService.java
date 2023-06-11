package design.kfu.sunrise.service.review;

import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.util.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Daniyar Zakiev
 */
public interface ReviewService {
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
}
