package com.api.veiculos.controller;

import com.api.veiculos.dto.MultaRequest;
import com.api.veiculos.dto.MultaResponse;
import com.api.veiculos.dto.VeiculoRequest;
import com.api.veiculos.dto.VeiculoResponse;
import com.api.veiculos.model.Multa;
import com.api.veiculos.model.Veiculo;
import com.api.veiculos.service.VeiculoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculosController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private VeiculoService veiculoService;


    @GetMapping
    @RolesAllowed( {"ADMIN","USUARIO"} )
    public ResponseEntity<List<VeiculoResponse>> consultar() {
        var veiculos = veiculoService.consultar();
        var resp = new ArrayList<VeiculoResponse>();
        for (Veiculo veiculo : veiculos) {
            var veiculoDTO = modelMapper.map(veiculo, VeiculoResponse.class);
            if (veiculo.temMultas()) {
                var multasDTO = veiculo.getMultas().stream()
                        .map(multa -> modelMapper.map(multa, MultaResponse.class)).toList();
                veiculoDTO.setMultas(multasDTO);
            }
            resp.add(veiculoDTO);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{placa}")
    @RolesAllowed( {"ADMIN","USUARIO"} )
    public ResponseEntity<VeiculoResponse> consultar(@PathVariable("placa") String placa) {
        Veiculo veiculo = veiculoService.consultar(placa);
        var resp = modelMapper.map(veiculo, VeiculoResponse.class);
        if (veiculo.temMultas()) {
            var multasDTO = veiculo.getMultas().stream()
                    .map(multa -> modelMapper.map(multa, MultaResponse.class)).toList();
            resp.setMultas(multasDTO);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<VeiculoResponse> cadastrarVeiculo(@RequestBody @Valid VeiculoRequest request) {
        var veiculo = modelMapper.map(request, Veiculo.class);
        veiculo = veiculoService.salvar(veiculo);
        var resp = modelMapper.map(veiculo, VeiculoResponse.class);
        return ResponseEntity.created(URI.create(veiculo.getPlaca())).body(resp);
    }

    @PostMapping("/{placa}/multas")
    @RolesAllowed("ADMIN")
    public ResponseEntity<MultaResponse> cadastrarMulta(@PathVariable("placa") String placa,
                                                        @RequestBody @Valid MultaRequest request) {
        var multa = modelMapper.map(request, Multa.class);
        multa = veiculoService.cadastrarMulta(placa, multa);
        var resp = modelMapper.map(multa, MultaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
