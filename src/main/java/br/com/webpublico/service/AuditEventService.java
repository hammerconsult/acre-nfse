package br.com.webpublico.service;

import org.springframework.stereotype.Service;

/**
 * Service for managing audit events.
 * <p/>
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
public class AuditEventService {

//    @Inject
//    private PersistenceAuditEventJPARepository persistenceAuditEventJPARepository;

//    @Inject
//    private AuditEventConverter auditEventConverter;
//
//    public List<AuditEvent> findAll() {
//        return auditEventConverter.convertToAuditEvent(persistenceAuditEventJPARepository.findAll());
//    }

//    public List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate) {
//        List<PersistentAuditEvent> persistentAuditEvents =
//            persistenceAuditEventJPARepository.findAllByAuditEventDateBetween(fromDate, toDate);

//        return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
//    }
}
