package com.bbdgrads.kudos_api.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.KudoRepository;

@Service
public class KudoServiceImpl implements KudoService {
    @Autowired
    private KudoRepository kudoRepository;

    // Change made here to return saved kudo.
    @Override
    public Kudo save(Kudo kudo) {
        return kudoRepository.save(kudo);
    }

    // Made change here to use optional and account for when kudo does not exist with ID.
    @Override
    public void delete(long kudoId) {
        Kudo tempKudo = findByKudoId(kudoId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("The kudo with kudoId %s does not exist", kudoId)));

        kudoRepository.deleteById(kudoId);
    }

    //Change made here
    @Override
    public Optional<Kudo> findByKudoId(Long kudoId) {
        return kudoRepository.findByKudoId(kudoId);
    }

    @Override
    public List<Kudo> findByTargetUser(User targetUser) {
        return kudoRepository.findByTargetUser(targetUser);
    }

    @Override
    public List<Kudo> findBySendingUser(User targetUser) {
        return kudoRepository.findBySendingUser(targetUser);
    }

}
