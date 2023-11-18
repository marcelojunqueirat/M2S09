package com.api.veiculos.service;

import com.api.veiculos.exception.RegistroJaExistenteException;
import com.api.veiculos.exception.RegistroNaoEncontradoException;
import com.api.veiculos.model.Multa;
import com.api.veiculos.model.Veiculo;
import com.api.veiculos.repository.MultaRepository;
import com.api.veiculos.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepo;

    @Autowired
    private MultaRepository multaRepo;


    public List<Veiculo> consultar() {
        return veiculoRepo.findAll();
    }

    public Veiculo consultar(String placa) {
        return veiculoRepo.findById(placa)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Veiculo", placa));
    }

    public Veiculo salvar(Veiculo veiculo) {
        boolean existe = veiculoRepo.existsById(veiculo.getPlaca());
        if(existe){
            throw new RegistroJaExistenteException("Veiculo", veiculo.getPlaca());
        }
        veiculo = veiculoRepo.save(veiculo);
        return veiculo;
    }

    public Multa cadastrarMulta(String placa, Multa multa) {
        var veiculo = this.consultar(placa);
        multa.setVeiculo(veiculo);
        multa = multaRepo.save(multa);
        return multa;
    }
}
