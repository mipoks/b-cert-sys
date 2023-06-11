package design.kfu.sunrise.service.util;

import org.springframework.stereotype.Service;

/**
 * @author Daniyar Zakiev
 */
@Service
public class AnonymousServiceImpl implements AnonymousService{

    private static final int MIN_INT_VALUE = 10000;
    private static final int MAX_INT_VALUE = 99999;

    @Override
    public String hideEmail(String email) {
        int length = email.length();
        StringBuilder stringBuilder = new StringBuilder(email.substring(0, length / 4));
        int sobaka = email.indexOf('@');
        StringBuilder result = stringBuilder
                .append("*".repeat(length / 4 * 3))
                .append(email.substring(sobaka));
        return result.toString();
    }

    @Override
    public String generateComment(int length) {
        int randInt = (int) (MIN_INT_VALUE + (Math.random() * (MAX_INT_VALUE - MIN_INT_VALUE)));
        return String.valueOf(randInt);
    }
}
