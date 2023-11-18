package com.api.veiculos.service;

import com.api.veiculos.exception.RegistroJaExistenteException;
import com.api.veiculos.model.Usuario;
import com.api.veiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;


    public Usuario inserir(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail()))
            throw new RegistroJaExistenteException("Usuario", usuario.getEmail());
        usuario.setSenha(usuario.getSenha());
        return usuarioRepo.save(usuario);
    }

    public List<Usuario> consultar() {
        return usuarioRepo.findAll();
    }

}
