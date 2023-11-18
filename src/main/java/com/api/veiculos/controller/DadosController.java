package com.api.veiculos.controller;

import com.api.veiculos.model.*;
import com.api.veiculos.service.UsuarioService;
import com.api.veiculos.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dados")
public class DadosController {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<?> carregarDados() {
        var veiculos = veiculoService.consultar();
        if (veiculos.isEmpty()) {
            Veiculo veiculo1 = new Veiculo("ABC-1234", TipoVeiculo.AUTOMOVEL, "Astra", 2008, "prata");
            Veiculo veiculo2 = new Veiculo("BCA-4321", TipoVeiculo.ONIBUS, "Enterprise", 1960, "verde");
            Veiculo veiculo3 = new Veiculo("HHE-1020", TipoVeiculo.CAMIONETA, "Ranger", 2014, "branco");
            Veiculo veiculo4 = new Veiculo("BBT-6654", TipoVeiculo.CAMINHAO, "SCANIA TH110", 1999, "preto");
            veiculoService.salvar(veiculo1);
            veiculoService.salvar(veiculo2);
            veiculoService.salvar(veiculo3);
            veiculoService.salvar(veiculo4);

            Multa multa1Veic1 = new Multa(veiculo1, "Farol apagado", "Gothan City", 250F);
            Multa multa2Veic1 = new Multa(veiculo1, "Insulfilm", "Gothan City", 100F);
            Multa multa1Veic2 = new Multa(veiculo2, "Excesso velocidade", "Hiper-espaço", 400F);
            Multa multa1Veic3 = new Multa(veiculo3, "Desrespeitar sinalização", "Somewhere", 800.50F);
            Multa multa1Veic4 = new Multa(veiculo4, "Trafegar na contramão", "Carroça City", 900.98F);
            veiculoService.cadastrarMulta(veiculo1.getPlaca(),multa1Veic1);
            veiculoService.cadastrarMulta(veiculo1.getPlaca(),multa2Veic1);
            veiculoService.cadastrarMulta(veiculo2.getPlaca(),multa1Veic2);
            veiculoService.cadastrarMulta(veiculo3.getPlaca(),multa1Veic3);
            veiculoService.cadastrarMulta(veiculo4.getPlaca(),multa1Veic4);
        }
        var usuarios = usuarioService.consultar();
        if (usuarios.isEmpty()) {
            usuarioService.inserir(new Usuario("Marcelo", "marcelo@example.com", "102030", Role.ADMIN));
            usuarioService.inserir(new Usuario("Maria", "maria@example.com", "123456", Role.USUARIO));
            usuarioService.inserir(new Usuario("James Kirk", "james@enterprise.com", "123456", Role.ADMIN));
            usuarioService.inserir(new Usuario("Spock", "spock@enterprise.com", "123456", Role.ADMIN));
            usuarioService.inserir(new Usuario("Leonard McCoy", "mccoy@enterprise.com", "123456", Role.USUARIO));
            usuarioService.inserir(new Usuario("Montgomery Scott", "scott@enterprise.com", "123456", Role.USUARIO));
        }
        return ResponseEntity.ok().build();
    }
}
