package com.bbdgrads.kudos_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.repository.KudoRepository;

@Service
public class KudoServiceImpl implements KudoService {
    @Autowired
    private KudoRepository kudoRepository;

    @Override
    public List<Kudo> findByTarget_user_id(Long target_id) {
        return kudoRepository.findAll();
    }

    @Override
    public void save(Kudo kudo) {
        kudoRepository.save(kudo);
    }

    @Override
    public void delete(long kudo_id) {
        kudoRepository.deleteById(kudo_id);
    }

}
