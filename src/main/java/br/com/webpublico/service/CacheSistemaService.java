package br.com.webpublico.service;

import br.com.webpublico.domain.dto.CacheSistemaNfseDTO;
import com.beust.jcommander.internal.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@Scope(value = "singleton")
public class CacheSistemaService implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(CacheSistemaService.class);

    private HashSet<CacheSistemaNfseDTO> caches = new HashSet<>();

    public synchronized boolean cached(CacheSistemaNfseDTO cache) {
        return caches.contains(cache);
    }

    public synchronized void addCache(CacheSistemaNfseDTO cache) {
        cache.setUuid(UUID.randomUUID().toString());
        caches.add(cache);
    }

    public synchronized void removeCache(CacheSistemaNfseDTO cache) {
        caches.remove(cache);
    }

    public synchronized void removeCache(String uuid) {
        caches.stream()
                .filter(cacheSistemaNfseDTO -> cacheSistemaNfseDTO.getUuid().equals(uuid))
                .findFirst()
                .ifPresent(cacheSistemaNfseDTO -> removeCache(cacheSistemaNfseDTO));
    }

    public Page<CacheSistemaNfseDTO> buscarCaches(Pageable pageable) {
        if (caches.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        long fromIndex = pageable.getOffset();
        long toIndex = pageable.getOffset() + pageable.getPageSize();
        if (toIndex > caches.size() - 1) {
            toIndex = caches.size();
        }
        List<CacheSistemaNfseDTO> cachesPagination = Lists.newArrayList(caches);
        cachesPagination = cachesPagination.subList((int) fromIndex, (int) toIndex);
        return new PageImpl<>(cachesPagination, pageable, caches.size());
    }

    public void aguardar(CacheSistemaNfseDTO cacheSistema) {
        while (cached(cacheSistema)) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
    }
}
