package br.com.webpublico.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AsyncService {

    @Async
    public void run(final Runnable runnable) {
        runnable.run();
    }
}