package com.mikaeru.user_api.domain.service.phone.serviceImpl;

import com.mikaeru.user_api.domain.exception.BusinessException;
import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.service.phone.CrudPhoneService;
import com.mikaeru.user_api.repository.PhoneRepository;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CrudPhoneServiceImpl implements CrudPhoneService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private CrudPhoneService phoneService;

    @Override
    public Phone savePhone(Phone phone) {
        try {
            //TODO set owner of phone in controller this class model
            if (phone.getOwner() == null) {
                throw new BusinessException("Phone owner is null");
            } else {
                phoneRepository.save(phone);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException("Error when trying to save the user's phone");
        }

        return null;
    }

    @Override
    public void deletePhoneById(Long id) {
        phoneRepository.deleteById(id);
    }

    @Override
    public List<Phone> saveAllPhone(List<Phone> phones) {
        try {
            for(Phone phone : phones) {
                if (phone.getOwner() == null)
                    throw new BusinessException("Phone owner is null");
            }

            return phoneRepository.saveAll(phones);

        } catch (BusinessException e) {
            e.printStackTrace();
            throw new BusinessException("Error when trying to save the user's phone");
        }
    }
}
