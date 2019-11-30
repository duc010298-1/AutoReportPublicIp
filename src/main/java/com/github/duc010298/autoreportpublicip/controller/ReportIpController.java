package com.github.duc010298.autoreportpublicip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

@RestController
@RequestMapping("/")
public class ReportIpController {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @GetMapping
    public String getIp() {
        EntityManager session = entityManagerFactory.createEntityManager();
        try {
            return (String) session.createNativeQuery("select ip from public_ip").getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (session.isOpen()) session.close();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> publicIp(@RequestParam("ip") String ip) {
        EntityManager session = entityManagerFactory.createEntityManager();
        try {
            session.joinTransaction();
            session.createNativeQuery("delete from public_ip").executeUpdate();
            session.createNativeQuery("insert into public_ip values (?)").setParameter(1, ip).executeUpdate();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            if (session.isOpen()) session.close();
        }
    }
}
