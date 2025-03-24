package com.bbdgrads.kudos_api.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.Log;
import com.bbdgrads.kudos_api.model.LogEvents;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.KudoRepository;

@Service
public class KudoServiceImpl implements KudoService {

    // private final LogServiceImpl logServiceImpl;
    @Autowired
    private KudoRepository kudoRepository;

    @Autowired
    private LogService logService;

    // KudoServiceImpl(LogServiceImpl logServiceImpl) {
    //     this.logServiceImpl = logServiceImpl;
    // }

    // Change made here to return saved kudo.
    @Override
    public Kudo save(Kudo kudo) {
        var createdKudo = kudoRepository.save(kudo);

        // var log = new Log();
        // log.setActingUser(kudo.getSendingUser());
        // log.setTargetUser(kudo.getTargetUser());
        // log.setKudo(kudo);
        // log.setEventId(3);
        // log.setVerboseLog(
        //         String.format("%s -- %s --> \"%s\" to %s", kudo.getSendingUser().getUsername(),
        //                 LogEvents.events.get(3),
        //                 kudo.getMessage().substring(0, Math.min(kudo.getMessage().length(), 50)),
        //                 kudo.getTargetUser().getUsername()));
        // logService.save(log);

        return createdKudo;
    }

    // Made change here to use optional and account for when kudo does not exist
    // with ID.
    @Override
    public void delete(Kudo kudo, User actingUser) {

        kudoRepository.delete(kudo);

        var log = new Log();
        log.setActingUser(actingUser);
        log.setKudo(kudo);
        log.setEventId(6);
        log.setVerboseLog(
                String.format("%s -- %s --> (%s) - %s", kudo.getSendingUser().getUsername(),
                        LogEvents.events.get(4),
                        kudo.getKudoId(),
                        kudo.getMessage().substring(0, Math.min(kudo.getMessage().length(), 50))));
        logService.save(log);
    }

    // Change made here
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

    public List<Kudo> findAllKudos() {
        return kudoRepository.findAll();
    }

    public void setRead(Kudo kudo, boolean read, User actingUser) {
        kudo.setRead(read);
        kudoRepository.save(kudo);

        var log = new Log();
        log.setActingUser(actingUser);
        log.setKudo(kudo);
        log.setEventId(7);
        log.setVerboseLog(
                String.format("%s -- %s --> (%s) - %s to %b", kudo.getSendingUser().getUsername(),
                        LogEvents.events.get(8),
                        kudo.getKudoId(),
                        kudo.getMessage().substring(0, Math.min(kudo.getMessage().length(), 50)), read));
        logService.save(log);
    }

    public void setFlagged(Kudo kudo, boolean flagged, User actingUser) {
        kudo.setFlagged(flagged);
        kudoRepository.save(kudo);

        var log = new Log();
        log.setActingUser(actingUser);
        log.setKudo(kudo);
        log.setEventId(8);
        log.setVerboseLog(
                String.format("%s -- %s --> (%s) - %s to %b", kudo.getSendingUser().getUsername(),
                        LogEvents.events.get(8),
                        kudo.getKudoId(),
                        kudo.getMessage().substring(0, Math.min(kudo.getMessage().length(), 50)), flagged));
        logService.save(log);
    }

    public void setMessage(Kudo kudo, String message, User actingUser) {
        kudo.setMessage(message);
        kudoRepository.save(kudo);

        var log = new Log();
        log.setActingUser(actingUser);
        log.setKudo(kudo);
        log.setEventId(9);
        log.setVerboseLog(
                String.format("%s -- %s --> (%s) - %s", kudo.getSendingUser().getUsername(),
                        LogEvents.events.get(4),
                        kudo.getKudoId(),
                        kudo.getMessage().substring(0, Math.min(kudo.getMessage().length(), 50))));
        logService.save(log);
    }
}
