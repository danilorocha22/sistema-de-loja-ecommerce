package com.loja.danilo.controllers;

import com.loja.danilo.models.ClientePF;
import com.loja.danilo.models.ItemVenda;
import com.loja.danilo.models.Usuario;
import com.loja.danilo.models.Venda;
import com.loja.danilo.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Transactional
@Controller
@Scope("request")
@RequestMapping("vendas")
public class VendaController {

    @Autowired
    VendaRepository vendaRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ItemVendaRepository itemVendaRepository;

    @Autowired
    Venda venda;

    //
    @GetMapping("/form")
    public ModelAndView form(ItemVenda itemVenda) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("produto", produtoRepository.produtos());
        modelMap.addAttribute("clientesPF", clienteRepository.clientesPF());
        return new ModelAndView("/vendas/form", modelMap);
    }

    //Lista todos os produtos no catalogo
    @GetMapping("/catalogo")
    public ModelAndView catalogo(ItemVenda itemVenda, BindingResult result) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("produtos", produtoRepository.produtos());
        return new ModelAndView("/vendas/catalogo", modelMap);
    }

    //Lista todas as vendas
    @GetMapping("/list")
    public ModelAndView listar(Venda venda, ModelMap model) {
        model.addAttribute("vendas", vendaRepository.vendas());
        return new ModelAndView("/vendas/list", model);
    }

    //Adiciona um Item ao carrinho
    @PostMapping("/add")
    public ModelAndView addItem(@Valid ItemVenda itemVenda, BindingResult result) {
        if (result.hasErrors()) return catalogo(itemVenda, result);

        itemVenda.setVenda(this.venda);
        itemVenda.setProduto(produtoRepository.produto(itemVenda.getProduto().getId()));
        itemVenda.vlTotalItem();
        this.venda.setItensVenda(itemVenda);
        this.venda.vlTotalVenda();
        return new ModelAndView("redirect:/vendas/form");//redirect: redireciona para o método
    }

    //Salva uma venda
    @PostMapping("/save")
    public ModelAndView save(Venda venda, RedirectAttributes attributes) {
        if (this.venda.getItensVenda().isEmpty())
            attributes.addFlashAttribute("erroItens", " Seu carrinho está vazio, verifique!");

        if (!attributes.getFlashAttributes().isEmpty())
            return new ModelAndView("redirect:/vendas/form");

        this.venda.setId(null);
        ClientePF cliente = buscarClientePeloUsuario();
        this.venda.setCliente(clienteRepository.clientePF(cliente.getId()));
        this.venda.setLocalDate(venda.getLocalDate());
        vendaRepository.save(this.venda);
        this.venda.getItensVenda().clear();
        return new ModelAndView("redirect:/vendas/minhascompras");
    }

    @GetMapping("/minhascompras")
    public ModelAndView minhasCompras(Venda vendas, ModelMap model) {
        ClientePF cliente = buscarClientePeloUsuario();
        model.addAttribute("vendas", vendaRepository.comprasCliente(cliente));
        return new ModelAndView("/clientes/minhascompras", model);
    }

    private ClientePF buscarClientePeloUsuario() {
        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.usuario(loginUsuario);
        ClientePF cliente = clienteRepository.clientePF(usuario);
        return cliente;
    }

    //Remove um item do carrinho
    @GetMapping("/removeitem/{i}")
    public ModelAndView removeItem(@PathVariable("i") int i) {
        venda.getItensVenda().remove(i);
        return new ModelAndView("redirect:/vendas/form");
    }

    //Filtrar Vendas por Data
    @GetMapping("/filtrardatavendas")
    public ModelAndView filtrarData(@RequestParam String filtrarData,
                                    RedirectAttributes attributes,
                                    ModelMap model) {
        if (filtrarData == "") {
            attributes.addFlashAttribute("erroData", " Informe uma data!");
            return new ModelAndView("redirect:/vendas/list");
        }

        LocalDate localDate = LocalDate.parse(filtrarData);
        model.addAttribute("vendas", vendaRepository.vendasPelaData(localDate));
        return new ModelAndView("/vendas/list", model);
    }

    //Filtrar Compras por Data e Cliente
    @GetMapping("/filtrardatacompras")
    public ModelAndView filtrarDataCompras(@RequestParam String filtrarData,
                                           RedirectAttributes attributes,
                                           ModelMap model) {
        if (filtrarData == "") {
            attributes.addFlashAttribute("erroData", " Informe uma data!");
            return new ModelAndView("redirect:/vendas/minhascompras");
        }

        ClientePF clientePF = buscarClientePeloUsuario();
        LocalDate localDate = LocalDate.parse(filtrarData);
        model.addAttribute("vendas", vendaRepository.comprasPelaDataCliente(localDate,
                                                                                          clientePF));
        return new ModelAndView("/clientes/minhascompras", model);
    }

    //Detalhes de uma Venda
    @GetMapping("/detalhesdavenda/{id}")
    public ModelAndView detalhes(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("itensVenda", itemVendaRepository.itensVenda(id));
        return new ModelAndView("/vendas/detalhesdavenda", model);
    }

    //Detalhes de uma Compra
    @GetMapping("/detalhesdacompra/{id}")
    public ModelAndView detalhesDaCompra(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("itensVenda", itemVendaRepository.itensVenda(id));
        return new ModelAndView("/clientes/detalhesdacompra", model);
    }

    //Buscar lista de Produtos especificados
    @GetMapping("/buscarproduto")
    public ModelAndView buscarProduto(@RequestParam String buscarProduto,
                                      RedirectAttributes attributes,
                                      ModelMap model,
                                      ItemVenda itemVenda,
                                      BindingResult result) {

        if (buscarProduto == "") {
            attributes.addFlashAttribute("erroProduto", " Informe um produto!");
            return new ModelAndView("redirect:/");
        }

        model.addAttribute("produtos", produtoRepository.buscarProduto(buscarProduto));

        return new ModelAndView("/vendas/catalogo", model);
    }

    //Busca lista de Vendas de vários Clientes
    @GetMapping("/filtrarvendascliente")
    public ModelAndView filtrarVendasCliente(@RequestParam String nome,
                                    RedirectAttributes attributes,
                                    ModelMap model) {
        if (nome == "") {
            attributes.addFlashAttribute("erroNome", " Informe o cliente!");
            return new ModelAndView("redirect:/vendas/list");
        }
        model.addAttribute("vendas", vendaRepository.comprasListClientes(nome));
        return new ModelAndView("/vendas/list", model);
    }

}
