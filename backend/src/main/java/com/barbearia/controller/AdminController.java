package com.barbearia.controller;

import com.barbearia.dto.*;
import com.barbearia.model.*;
import com.barbearia.service.AdminService;
import com.barbearia.service.AgendamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Endpoints administrativos (requer autenticação Bearer)")
public class AdminController {

    private final AdminService adminService;
    private final AgendamentoService agendamentoService;

    public AdminController(AdminService adminService, AgendamentoService agendamentoService) {
        this.adminService = adminService;
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {
        return adminService.login(request.getUsuario(), request.getSenha());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        adminService.logout(extractToken(auth));
    }

    @GetMapping("/agendamentos")
    public List<AgendamentoResponse> agendamentos(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return agendamentoService.listarTodos();
    }

    @PatchMapping("/agendamentos/{id}/concluir")
    public void concluir(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        adminService.validarToken(extractToken(auth));
        agendamentoService.concluir(id);
    }

    @GetMapping("/clientes")
    public List<Cliente> clientes(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return adminService.listarClientes();
    }

    @GetMapping("/servicos")
    public List<Servico> servicos(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return adminService.listarServicos();
    }

    @PutMapping("/servicos/{tipo}")
    public Servico atualizarServico(@RequestHeader("Authorization") String auth,
                                    @PathVariable TipoServico tipo,
                                    @RequestBody Servico servico) {
        adminService.validarToken(extractToken(auth));
        return adminService.atualizarServico(tipo, servico);
    }

    @GetMapping("/barbeiros")
    public List<Barbeiro> barbeiros(@RequestHeader("Authorization") String auth,
                                    @RequestParam(required = false) String nome) {
        adminService.validarToken(extractToken(auth));
        return adminService.buscarBarbeiros(nome);
    }

    @PostMapping("/barbeiros")
    public Barbeiro criarBarbeiro(@RequestHeader("Authorization") String auth, @RequestBody Barbeiro barbeiro) {
        adminService.validarToken(extractToken(auth));
        return adminService.criarBarbeiro(barbeiro);
    }

    @PutMapping("/barbeiros/{id}")
    public Barbeiro editarBarbeiro(@RequestHeader("Authorization") String auth,
                                   @PathVariable String id,
                                   @RequestBody Barbeiro barbeiro) {
        adminService.validarToken(extractToken(auth));
        return adminService.editarBarbeiro(id, barbeiro);
    }

    @DeleteMapping("/barbeiros/{id}")
    public void desativarBarbeiro(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        adminService.validarToken(extractToken(auth));
        adminService.desativarBarbeiro(id);
    }

    @GetMapping("/faturamento")
    public FaturamentoResponse faturamento(@RequestHeader("Authorization") String auth,
                                             @RequestParam int mes,
                                             @RequestParam int ano) {
        adminService.validarToken(extractToken(auth));
        return adminService.faturamentoMensal(mes, ano);
    }

    @GetMapping("/cancelamentos")
    public List<Cancelamento> cancelamentos(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return adminService.historicoCancelamentos();
    }

    @GetMapping("/avaliacoes")
    public List<Avaliacao> avaliacoes(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return adminService.listarAvaliacoes();
    }

    @PatchMapping("/avaliacoes/{id}/validar")
    public Avaliacao validarAvaliacao(@RequestHeader("Authorization") String auth, @PathVariable String id) {
        adminService.validarToken(extractToken(auth));
        return adminService.validarAvaliacao(id);
    }

    @GetMapping("/ranking")
    public List<RankingBarbeiroResponse> ranking(@RequestHeader("Authorization") String auth) {
        adminService.validarToken(extractToken(auth));
        return adminService.rankingBarbeiros();
    }

    private String extractToken(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return auth;
    }
}
