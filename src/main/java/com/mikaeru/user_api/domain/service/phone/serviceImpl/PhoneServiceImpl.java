package com.mikaeru.user_api.domain.service.phone.serviceImpl;

import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.service.phone.PhoneService;
import com.mikaeru.user_api.repository.PhoneRepository;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired private UserRepository userRepository;

    @Autowired private PhoneRepository phoneRepository;

    private static final String USER_NOT_FOUND = "Usuário não encontrado!";
    private static final String PHONE_NOT_FOUND = "Telefone não encontrado!";

    @Override
    public void delete(UUID userUUID, UUID phoneUUID) {

        Optional<User> user = userRepository.findByExternalId(userUUID);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }

        Optional<Phone> phone = phoneRepository.findByExternalId(phoneUUID);

        if (phone.isEmpty()) {
            throw new EntityNotFoundException(PHONE_NOT_FOUND);
        }

        phoneRepository.delete(phone.get());
    }
}
