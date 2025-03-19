package com.bbdgrads.kudos_api.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.bbdgrads.kudos_api.model.Kudo;
import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.repository.KudoRepository;
import com.bbdgrads.kudos_api.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class KudoServiceTest {

    @Mock
    private KudoRepository kudoRepository;

    @InjectMocks
    private KudoServiceImpl kudoService;

    @Autowired
    UserRepository userRepository;

    private Kudo testKudo;
    private User sendingUser;
    private User targetUser;

    @Before
    public void setUp() {
        sendingUser = new User("john doe", "john123", false);
        targetUser = new User("jane doe", "jane123", false);

        testKudo = new Kudo();

        testKudo.setMessage("Good job!");
        testKudo.setSendingUser(sendingUser);
        testKudo.setTargetUser(targetUser);
        testKudo.setFlagged(false);
        testKudo.setRead(false);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSave() {
        when(kudoRepository.save(any(Kudo.class))).thenReturn(testKudo);
        kudoService.save(testKudo);
        verify(kudoRepository, times(1)).save(testKudo);
    }

    @Test
    public void testDelete() {
        doNothing().when(kudoRepository).deleteById(any(Long.class));
        when(kudoRepository.findByKudoId(any(Long.class))).thenReturn(Optional.of(testKudo));
        kudoService.delete(1L);
        verify(kudoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByKudoId() {
        when(kudoRepository.findByKudoId(any(Long.class))).thenReturn(Optional.of(testKudo));

        var foundKudo = kudoService.findByKudoId(1L);

        assertNotNull(foundKudo);
        assertTrue(foundKudo.isPresent());

        assertEquals(testKudo.getMessage(), foundKudo.get().getMessage());

        verify(kudoRepository, times(1)).findByKudoId(1L);
    }

    @Test
    public void testFindByTargetUser() {
        when(kudoRepository.findByTargetUser(any(User.class))).thenReturn(Lists.list(testKudo));

        var foundKudos = kudoService.findByTargetUser(testKudo.getTargetUser());

        assertNotNull(foundKudos);
        assertFalse(foundKudos.isEmpty());

        Kudo foundKudo = foundKudos.get(0);

        assertEquals(testKudo.getMessage(), foundKudo.getMessage());

        verify(kudoRepository, times(1)).findByTargetUser(testKudo.getTargetUser());
    }

    @Test
    public void testFindBySendingUser() {
        when(kudoRepository.findBySendingUser(any(User.class))).thenReturn(Lists.list(testKudo));

        var foundKudos = kudoService.findBySendingUser(testKudo.getSendingUser());

        assertNotNull(foundKudos);
        assertFalse(foundKudos.isEmpty());

        Kudo foundKudo = foundKudos.get(0);

        assertEquals(testKudo.getMessage(), foundKudo.getMessage());

        verify(kudoRepository, times(1)).findBySendingUser(testKudo.getSendingUser());
    }

}
