package design.kfu.sunrise.mapper;

import design.kfu.sunrise.domain.dto.ReviewDTO;
import design.kfu.sunrise.domain.model.util.Review;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        //указаьб мапперы которые использует
})
public interface RewiewMapper {
    Review toReview(ReviewDTO dto);
    ReviewDTO toReviewDTO(Review review);
}
