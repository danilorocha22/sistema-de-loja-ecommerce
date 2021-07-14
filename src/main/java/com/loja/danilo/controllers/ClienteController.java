package com.loja.danilo.controllers;

import com.loja.danilo.models.ClientePF;
import com.loja.danilo.models.Role;
import com.loja.danilo.models.Usuario;
import com.loja.danilo.repositorys.ClienteRepository;
import com.loja.danilo.repositorys.RoleRepository;
import com.loja.danilo.repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/form")
    public ModelAndView form(ClientePF clientePF) {
        return new ModelAndView("/clientes/form");
    }

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("clientesPF", clienteRepository.clientesPF());
        return new ModelAndView("/clientes/list", model);
    }

    private ClientePF setDadosClienteUsuario(ClientePF cliente) {
        Usuario usuario = new Usuario();
        usuario = cliente.getUsuario();
        usuario.setPassword(criptPassword(cliente.getUsuario().getPassword()));
        Role role = roleRepository.find(2L);
        usuario.getRoles().add(role);
        cliente.setUsuario(usuario);
        usuario.setCliente(cliente);
        return cliente;
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid ClientePF clientePF, BindingResult result) {
        if (result.hasErrors()) return form(clientePF);
        clienteRepository.save(setDadosClienteUsuario(clientePF));
        return new ModelAndView("redirect:/clientes/list");
    }

    @GetMapping("/meuperfil")
    public ModelAndView meuperfil() {
        ModelMap model = new ModelMap();
        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.usuario(loginUsuario);
        System.out.println(usuario.getRoles().get(0).getAuthority());
        if (usuario.getRoles().get(0).getAuthority().equals("ROLE_EDITOR")) {
            model.addAttribute("clientesPF", clienteRepository.clientePF(usuario));
            return new ModelAndView("/clientes/perfil", model);
        } else {
            return new ModelAndView("/home");
        }
    }


    @GetMapping("/formcadastro")
    public ModelAndView formCadastro(ClientePF clientePF) {
        return new ModelAndView("/auth/formcadastro");
    }

    @GetMapping("/login")
    public ModelAndView login(ClientePF clientePF) {
        return new ModelAndView("/auth/login");
    }

    @PostMapping("/savecadastro")
    public ModelAndView saveCadastro(@Valid ClientePF clientePF,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return formCadastro(clientePF);
        }
        if(clientePF.getUsuario().getLogin().equals(null) ||
                clientePF.getUsuario().getLogin().isEmpty() ||
                clientePF.getUsuario().getLogin().isBlank() ||
                clientePF.getUsuario().getPassword().equals(null) ||
                clientePF.getUsuario().getPassword().isEmpty() ||
                clientePF.getUsuario().getPassword().isBlank()) {

            redirectAttributes.addFlashAttribute("erroLogin",
                                                "Informe login e senha!");
            return new ModelAndView("redirect:/clientes/formcadastro");
        }

        clienteRepository.save(setDadosClienteUsuario(clientePF));
        redirectAttributes.addFlashAttribute("success", "Registrado com sucesso!");
        return new ModelAndView("redirect:/clientes/login");
    }

    private String criptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        ClientePF cliente = clienteRepository.clientePF(id);
        if(cliente.getVendas().isEmpty()) {
            clienteRepository.remove(id);
            redirectAttributes.addFlashAttribute("success",
                    " Cliente removido com sucesso!");
            return new ModelAndView("redirect:/clientes/list");
        } else {
            redirectAttributes.addFlashAttribute("erroCliente",
                    " Este cliente possui compras, não é possivel removê⁻lo!");
            return new ModelAndView("redirect:/clientes/list");
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("clientePF", clienteRepository.clientePF(id));
        return new ModelAndView("/clientes/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(@Valid ClientePF clientePF, BindingResult result) {
        if (result.hasErrors()) return form(clientePF);
        Usuario usuario = usuarioRepository.usuarioPeloCliente(clientePF);
        clientePF.setUsuario(usuarioRepository.usuario(usuario.getLogin()));
        clienteRepository.update(clientePF);
        if (usuario.getRoles().get(0).getAuthority().equals("ROLE_EDITOR")) {
            return new ModelAndView("redirect:/clientes/meuperfil");
        } else {
            return new ModelAndView("redirect:/clientes/list");
        }
    }

    @GetMapping("/filtrarcliente")
    public ModelAndView filtrarNome(@RequestParam String filtrarNome,
                                    RedirectAttributes attributes,
                                    ModelMap model) {
        if (filtrarNome == "") {
            attributes.addFlashAttribute("erroNome", " Informe o nome!");
            return new ModelAndView("redirect:/clientes/list");
        }

        model.addAttribute("clientesPF", clienteRepository.filtrarNome(filtrarNome));
        return new ModelAndView("/clientes/list", model);
    }
}
