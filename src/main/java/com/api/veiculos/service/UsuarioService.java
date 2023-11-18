package com.api.veiculos.service;

import com.api.veiculos.exception.RegistroJaExistenteException;
import com.api.veiculos.model.Usuario;
import com.api.veiculos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario inserir(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail()))
            throw new RegistroJaExistenteException("Usuario", usuario.getEmail());
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepo.save(usuario);
    }

    public List<Usuario> consultar() {
        return usuarioRepo.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);
        if (usuarioOpt.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return usuarioOpt.get();
    }
}
