package com.controlefacil.controlefacil.service;

import com.controlefacil.controlefacil.exception.ResourceNotFoundException;
import com.controlefacil.controlefacil.model.Saldo;
import com.controlefacil.controlefacil.repository.SaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaldoService {
    @Autowired
    private SaldoRepository saldoRepository;

    public List<Saldo> getAllSaldos() {
        return saldoRepository.findAll();
    }

    public Saldo getSaldoById(Long id) {
        return saldoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Saldo not found"));
    }

    public Saldo saveSaldo(Saldo saldo) {
        return saldoRepository.save(saldo);
    }

    public void deleteSaldo(Long id) {
        saldoRepository.deleteById(id);
    }
}
