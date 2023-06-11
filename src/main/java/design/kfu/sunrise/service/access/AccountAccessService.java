package design.kfu.sunrise.service.access;

import design.kfu.sunrise.domain.dto.conversation.ConversationMessageDTO;
import design.kfu.sunrise.domain.model.*;
import design.kfu.sunrise.domain.model.conversation.Conversation;
import design.kfu.sunrise.domain.model.payment.Payment;
import design.kfu.sunrise.domain.model.util.Notification;
import design.kfu.sunrise.domain.model.util.Review;
import design.kfu.sunrise.repository.CommentRepository;
import design.kfu.sunrise.service.account.AccountService;
import design.kfu.sunrise.service.authority.AuthorityService;
import design.kfu.sunrise.service.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Component(value = "access")
@RequiredArgsConstructor
public class AccountAccessService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private AuthorityService authorityService;

    public boolean isPartner(Account account) {
        return account.getRole().equals(Account.Role.PARTNER);
    }

    public boolean isAdmin(Account account) {
        return account.getRole().equals(Account.Role.ADMIN);
    }

    @Transactional
    public boolean hasAccessToWriteComment(Account account, Club club) {
        log.info("Account with id {} trying to write comment to Club with id {}", account.getId(), club.getId());

        Authority authority = authorityService.findOrThrow(account, club);
        Set<Authority.AuthorityType> authorityTypeList = authority.getAuthorityTypes();
        boolean access = authorityTypeList
                .stream()
                .anyMatch(type -> type.equals(Authority.AuthorityType.WRITE_CLUB_COMMENTS));

        return access || isAdmin(account) || isPartner(account);
    }


    @Transactional
    public boolean hasAccessToReadComment(Account account, Club club) {
        log.info("Account with id {} trying to read comments from Club with id {}", account.getId(), club.getId());

        Authority authority = authorityService.findOrThrow(account, club);
        Set<Authority.AuthorityType> authorityTypeList = authority.getAuthorityTypes();
        boolean access = authorityTypeList
                .stream()
                .anyMatch(type -> type.equals(Authority.AuthorityType.READ_CLUB_COMMENTS));

        return access || isAdmin(account) || isPartner(account);
    }

    @Transactional
    public boolean hasAccessToCreateCategory(Account account) {
        log.info("Account with id {} trying to create category", account.getId());
        return isPartner(account) || isAdmin(account);
    }


    @Transactional
    public boolean hasAccessToCreateClub(Account account) {
        return isAdmin(account) || isPartner(account);
    }

    @Transactional
    public boolean hasAccessToEditComment(Comment comment, Account account) {
        if (comment.getAccount().equals(account)) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean hasAccessToEnterClub(Account account, Club club) {
        return !club.getAccounts().contains(account);
    }

    public boolean hasAccessToDeleteCategory(Account account, Category category) {
        return isPartner(account) || isAdmin(account);
    }

    public boolean hasAccessToEditClub(Account account, Club club) {
        return isPartner(account)
                || isAdmin(account)
                || club.getAuthor().equals(account);
    }

    public boolean hasAccessToReadNotification(Account account, Notification notification) {
        return notification.getAccountId().equals(account.getId());
    }

    public boolean hasAccessToMakeReview(Account account, Review review, String object) {
        return isAdmin(account)|| isPartner(account);
    }

    public boolean  hasAccessToBan(Account account) {
        return isAdmin(account)|| isPartner(account);
    }

    public boolean hasAccessToSendMessage(Account account, ConversationMessageDTO messageDTO) {
        if (isAdmin(account) || isPartner(account)) {
            return true;
        } else {
            Long conversationId = messageDTO.getConversationId();
            Conversation conversation = conversationService.findOrNull(conversationId);
            if (conversation == null || !conversation.isAccountInConversation(account)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean hasAccessToConversation(Account account, Conversation conversation) {
        return conversation.isAccountInConversation(account);
    }

    public boolean hasAccessToPayment(Account account, Payment payment) {
        return payment.getAccount().equals(account) || isPartner(account) || isAdmin(account);
    }

    public boolean hasAccessToSendPayment(Account account, Payment payment) {
        return payment.getAccount().equals(account);
    }

    public boolean hasAccessToConfirmPayment(Account account, Payment payment) {
        return isPartner(account) || isAdmin(account);
    }

    public boolean hasAccessToRefundPayment(Account account, Payment payment) {
        return isPartner(account) || isAdmin(account);
    }

    public boolean hasAccessToPayForClub(Account account, Club club) {
        return !club.getAccounts().contains(account);
    }

    public boolean hasAccessToRequisites(Account account, Account req) {
        return isPartner(account) || isAdmin(account) || account.equals(req);
    }

    public boolean hasAccessToCreateRequisite(Account account) {
        return isPartner(account) || isAdmin(account);
    }

    public boolean hasAccessToAllRequisites(Account account) {
        return isAdmin(account);
    }

    public boolean hasAccessToReadReviews(Account account) {
        return isPartner(account) || isAdmin(account);
    }
}
