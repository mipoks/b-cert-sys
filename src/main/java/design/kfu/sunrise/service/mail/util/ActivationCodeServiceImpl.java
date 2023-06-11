package design.kfu.sunrise.service.mail.util;

import design.kfu.sunrise.domain.model.Account;
import design.kfu.sunrise.domain.model.util.ActivationCode;
import design.kfu.sunrise.repository.util.ActivationCodeRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Optional;

/**
 * @author Daniyar Zakiev
 */
@Service
public class ActivationCodeServiceImpl implements ActivationCodeService {

    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    private MessageDigest digest;

    @SneakyThrows
    @PostConstruct
    private void initDigest() {
        digest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public ActivationCode generate(Account account) {
        ActivationCode ac = ActivationCode.builder()
                .accountId(account.getId())
                .code(generateCode(account.getEmail()))
                .build();
        return ac;
    }

    @Override
    public ActivationCode save(ActivationCode code) {
        return activationCodeRepository.save(code);
    }

    @Override
    public Optional<ActivationCode> findByCode(String code) {
        return activationCodeRepository.findById(code);
    }

    @Override
    public void delete(ActivationCode activationCode) {
        activationCodeRepository.delete(activationCode);
    }


    @SneakyThrows
    private String generateCode(String email) {
        byte[] hashedBytes = digest.digest((email + Instant.now()).getBytes("UTF-8"));
        return convertByteArrayToHexString(hashedBytes);
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
