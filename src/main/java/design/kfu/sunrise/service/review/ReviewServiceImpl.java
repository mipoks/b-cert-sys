package design.kfu.sunrise.service.review;

import design.kfu.sunrise.domain.event.CategoryEvent;
import design.kfu.sunrise.domain.event.ClubEvent;
import design.kfu.sunrise.domain.event.CommentEvent;
import design.kfu.sunrise.domain.model.Category;
import design.kfu.sunrise.domain.model.Club;
import design.kfu.sunrise.domain.model.Comment;
import design.kfu.sunrise.domain.model.util.Review;
import design.kfu.sunrise.exception.ErrorType;
import design.kfu.sunrise.exception.Exc;
import design.kfu.sunrise.repository.CategoryRepository;
import design.kfu.sunrise.repository.ClubRepository;
import design.kfu.sunrise.repository.CommentRepository;
import design.kfu.sunrise.repository.util.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * @author Daniyar Zakiev
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void addCommentForReview(Comment comment) {
        Review review = Review.builder()
                .instant(Instant.now())
                .objectId(comment.getId())
                .objectHash(comment.generateHash())
                .objectName(Comment.class.getSimpleName())
                .viewed(false)
                .build();
        reviewRepository.save(review);
    }

    @Override
    public void addCategoryForReview(Category category) {
        Review review = Review.builder()
                .instant(Instant.now())
                .objectId(category.getId())
                .objectHash(category.generateHash())
                .objectName(Category.class.getSimpleName())
                .viewed(false)
                .build();
        reviewRepository.save(review);
    }

    @Override
    public void addClubForReview(Club club) {
        Review review = Review.builder()
                .instant(Instant.now())
                .objectId(club.getId())
                .objectHash(club.generateHash())
                .objectName(Club.class.getSimpleName())
                .viewed(false)
                .build();
        reviewRepository.save(review);
    }

    @Override
    public Page<Review> findReviewsForComments(Pageable pageable) {
        return reviewRepository.findAllByViewedAndObjectNameOrderByInstant(false, Comment.class.getSimpleName(), pageable);
    }

    @Override
    public Page<Review> findReviewsForClubs(Pageable pageable) {
        return reviewRepository.findAllByViewedAndObjectNameOrderByInstant(false, Club.class.getSimpleName(), pageable);
    }

    @Override
    public Page<Review> findReviewsForCategories(Pageable pageable) {
        return reviewRepository.findAllByViewedAndObjectNameOrderByInstant(false, Category.class.getSimpleName(), pageable);
    }

    @Override
    public Page<Review> findReviews(Pageable pageable) {
        return reviewRepository.findAllByViewedOrderByInstant(false, pageable);
    }

    @Override
    @Transactional
    public boolean reviewComment(Comment comment, Review review) {
        if (!review.getObjectHash().equals(comment.generateHash())) {
            throw Exc.gen(ErrorType.ENTITY_ACCESS_VIOLATION);
        }
        Comment commentLocked = commentRepository.findByIdWithLock(comment.getId()).get();
        commentLocked.getCommentInfo().setVisible(review.isAccept());
        commentRepository.save(commentLocked);
        review.setViewed(true);
        reviewRepository.save(review);

        CommentEvent event;
        if (comment.getCommentInfo().isVisible()) {
            event = new CommentEvent(Comment.class.getName(), CommentEvent.Event.PUBLISH.getName(), comment);
        } else {
            event = new CommentEvent(Comment.class.getName(), CommentEvent.Event.DECLINE.getName(), comment);
        }
        publisher.publishEvent(event);
        return true;
    }

    @Override
    @Transactional
    public boolean reviewCategory(Category category, Review review) {
        if (!review.getObjectHash().equals(category.generateHash())) {
            throw Exc.gen(ErrorType.ENTITY_ACCESS_VIOLATION);
        }
        Category categoryLocked = categoryRepository.findByIdWithLock(category.getId()).get();
        categoryLocked.setVisible(review.isAccept());
        categoryRepository.save(categoryLocked);
        review.setViewed(true);
        reviewRepository.save(review);

        CategoryEvent event;
        //Не перехватывается
        if (category.isVisible()) {
            event = new CategoryEvent(Comment.class.getName(), CategoryEvent.Event.PUBLISH.getName(), category);
        } else {
            event = new CategoryEvent(Comment.class.getName(), CategoryEvent.Event.DECLINE.getName(), category);
        }
        publisher.publishEvent(event);
        return true;
    }

    @Override
    @Transactional
    public boolean reviewClub(Club club, Review review) {
        if (!review.getObjectHash().equals(club.generateHash())) {
            throw Exc.gen(ErrorType.ENTITY_ACCESS_VIOLATION);
        }
        Club clubLocked = clubRepository.findByIdWithLock(club.getId()).get();
        clubLocked.getClubInfo().setVisible(review.isAccept());
        clubRepository.save(clubLocked);
        review.setViewed(true);
        reviewRepository.save(review);

        ClubEvent event;
        if (club.getClubInfo().getVisible()) {
            event = new ClubEvent(Club.class.getName(), ClubEvent.Event.PUBLISH.getName(), club);
        } else {
            event = new ClubEvent(Club.class.getName(), ClubEvent.Event.DECLINE.getName(), club);
        }
        publisher.publishEvent(event);
        return true;
    }
}
