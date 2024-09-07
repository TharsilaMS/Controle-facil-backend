package com.controlefacil.controlefacil.controller;

import com.controlefacil.controlefacil.dto.MetaSonhoDTO;
import com.controlefacil.controlefacil.model.MetaSonho;
import com.controlefacil.controlefacil.model.Usuario;
import com.controlefacil.controlefacil.service.MetaSonhoService;
import com.controlefacil.controlefacil.util.ConversorDeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metas-sonho")
public class MetaSonhoController {

    @Autowired
    private MetaSonhoService metaSonhoService;

    @GetMapping
    public ResponseEntity<List<MetaSonhoDTO>> getAllMetasSonho(@RequestParam Long usuarioId) {
        List<MetaSonhoDTO> metaSonhoDTOs = metaSonhoService.getAllMetaSonhosByUsuarioId(usuarioId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(metaSonhoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaSonhoDTO> getMetaSonhoById(@PathVariable Long id) {
        try {
            MetaSonho metaSonho = metaSonhoService.getMetaSonhoById(id);
            return ResponseEntity.ok(convertToDTO(metaSonho));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MetaSonhoDTO> createMetaSonho(@RequestBody MetaSonhoDTO metaSonhoDTO) {
        LocalDate prazoDate = ConversorDeData.parseDate(metaSonhoDTO.getPrazo());
        LocalDateTime prazoDateTime = LocalDateTime.of(prazoDate, LocalTime.MIDNIGHT);

        MetaSonho metaSonho = new MetaSonho();
        metaSonho.setTitulo(metaSonhoDTO.getTitulo());
        metaSonho.setDescricao(metaSonhoDTO.getDescricao());
        metaSonho.setValorAlvo(metaSonhoDTO.getValorAlvo());
        metaSonho.setValorTotal(metaSonhoDTO.getValorAlvo());
        metaSonho.setValorEconomizado(BigDecimal.ZERO);
        metaSonho.setDataCriacao(LocalDateTime.now());
        metaSonho.setPrazo(prazoDateTime);
        metaSonho.setUsuario(new Usuario(metaSonhoDTO.getUsuarioId()));

        MetaSonho createdMetaSonho = metaSonhoService.createMetaSonho(metaSonho);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdMetaSonho));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaSonhoDTO> updateMetaSonho(@PathVariable Long id, @RequestBody MetaSonhoDTO metaSonhoDTO) {
        try {
            MetaSonho metaSonho = metaSonhoService.getMetaSonhoById(id);
            LocalDate prazoDate = ConversorDeData.parseDate(metaSonhoDTO.getPrazo());
            LocalDateTime prazoDateTime = LocalDateTime.of(prazoDate, LocalTime.MIDNIGHT);

            metaSonho.setTitulo(metaSonhoDTO.getTitulo());
            metaSonho.setDescricao(metaSonhoDTO.getDescricao());
            metaSonho.setValorAlvo(metaSonhoDTO.getValorAlvo());
            metaSonho.setValorTotal(metaSonhoDTO.getValorAlvo()); // Atualize valor total com valor alvo
            metaSonho.setPrazo(prazoDateTime);
            metaSonho.setUsuario(new Usuario(metaSonhoDTO.getUsuarioId()));

            MetaSonho updatedMetaSonho = metaSonhoService.updateMetaSonho(id, metaSonho);
            return ResponseEntity.ok(convertToDTO(updatedMetaSonho));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaSonho(@PathVariable Long id) {
        try {
            metaSonhoService.deleteMetaSonho(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/verificar-economia/{usuarioId}")
    public String verificarEconomiaEGuardar(@PathVariable Long usuarioId) {
        return metaSonhoService.verificarEconomiaEGuardar(usuarioId);
    }

    private MetaSonhoDTO convertToDTO(MetaSonho metaSonho) {
        MetaSonhoDTO metaSonhoDTO = new MetaSonhoDTO();
        metaSonhoDTO.setId(metaSonho.getId());
        metaSonhoDTO.setTitulo(metaSonho.getTitulo());
        metaSonhoDTO.setDescricao(metaSonho.getDescricao());
        metaSonhoDTO.setValorAlvo(metaSonho.getValorAlvo());
        metaSonhoDTO.setValorEconomizado(metaSonho.getValorEconomizado());
        metaSonhoDTO.setPrazo(ConversorDeData.formatDate(metaSonho.getPrazo().toLocalDate()));
        metaSonhoDTO.setStatus(metaSonho.getStatus());
        metaSonhoDTO.setUsuarioId(metaSonho.getUsuario().getIdUsuario());
        return metaSonhoDTO;
    }
}
