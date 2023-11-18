package com.api.veiculos.controller;

import com.api.veiculos.dto.AutenticacaoRequest;
import com.api.veiculos.dto.AutenticacaoResponse;
import com.api.veiculos.dto.UsuarioRequest;
import com.api.veiculos.dto.UsuarioResponse;
import com.api.veiculos.model.Usuario;
import com.api.veiculos.service.AutenticacaoService;
import com.api.veiculos.service.UsuarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutenticacaoService autenticacaoService;


    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> consultar() {
        var usuarios = usuarioService.consultar();
        var resp = usuarios.stream().map(usuario -> modelMapper.map(usuario, UsuarioResponse.class)).toList();
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<UsuarioResponse> inserir(@RequestBody @Valid UsuarioRequest request) {
        var usuario = modelMapper.map(request, Usuario.class);
        usuario = usuarioService.inserir(usuario);
        var resp = modelMapper.map(usuario, UsuarioResponse.class);
        return ResponseEntity.created(URI.create(usuario.getId().toString())).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponse> login(@RequestBody @Valid AutenticacaoRequest request) {
        var token = autenticacaoService.autenticar(request.getEmail(), request.getSenha());
        return ResponseEntity.ok(new AutenticacaoResponse(token));
    }
}
