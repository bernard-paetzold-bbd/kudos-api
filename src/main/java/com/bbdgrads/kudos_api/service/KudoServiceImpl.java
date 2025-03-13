package com.bbdgrads.kudos_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.KudoRepository;

@Service
public class KudoServiceImpl implements KudoService {
    @Autowired
    private KudoRepository kudoRepository;

    @Override
    public void save(Kudo kudo) {
        kudoRepository.save(kudo);
    }

    @Override
    public void delete(long kudoId) {
        kudoRepository.deleteById(kudoId);
    }

    @Override
    public Kudo findByKudoId(Long kudoId) {
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

    @Override
    public void setFlagged(Kudo kudo, boolean flagged) {
        kudo.setFlagged(flagged);
    }

    @Override
    public void setRead(Kudo kudo, boolean read) {
        kudo.setRead(read);
    }

}
