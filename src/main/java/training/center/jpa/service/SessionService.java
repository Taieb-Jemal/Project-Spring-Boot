package training.center.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.center.jpa.model.Session;
import training.center.jpa.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public Optional<Session> getSessionByCode(String code) {
        return sessionRepository.findByCode(code);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session updateSession(Session session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public List<Session> getActiveSessions() {
        return sessionRepository.findByActif(true);
    }

    public List<Session> getSessionsByAnnee(Integer annee) {
        return sessionRepository.findByAnnee(annee);
    }

    public List<Session> getSessionsBySemestre(Session.Semestre semestre) {
        return sessionRepository.findBySemestre(semestre);
    }
}
