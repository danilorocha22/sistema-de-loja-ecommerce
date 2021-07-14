package com.loja.danilo.controllers;

import com.loja.danilo.models.ItemVenda;
import com.loja.danilo.models.Produto;
import com.loja.danilo.repositorys.ItemVendaRepository;
import com.loja.danilo.repositorys.ProdutoRepository;
import com.loja.danilo.repositorys.VendaRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

/**
 * @author danilo
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("produtos")
public class ProdutoController {
	
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ItemVendaRepository itemVendaRepository;

    @GetMapping("/form")
    public ModelAndView form(Produto produto){
        return new ModelAndView("/produtos/form");
    }

    @GetMapping("/list")
    public ModelAndView listar(ModelMap model) {
        model.addAttribute("produtos", produtoRepository.produtos());
        return new ModelAndView("/produtos/list", model);
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) return form(produto);
        produtoRepository.save(produto);
        return new ModelAndView("redirect:/produtos/list");
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Produto produto = produtoRepository.produto(id);
        List<ItemVenda> itemVendaList = itemVendaRepository.buscaProdutoVendido(produto);
        if (itemVendaList.isEmpty()) {
            produtoRepository.remove(id);
            redirectAttributes.addFlashAttribute("success",
                    " Produto removido com sucesso!");
            return new ModelAndView("redirect:/produtos/list");
        } else {
            redirectAttributes.addFlashAttribute("erroProduto",
                    " Este produto não pode ser removido, já foi vendido!");
            return new ModelAndView("redirect:/produtos/list");
        }

    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("produto", produtoRepository.produto(id));
        return new ModelAndView("/produtos/form", model);
    }

    @PostMapping("/update")
    public ModelAndView update(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) return form(produto);
        produtoRepository.update(produto);
        return new ModelAndView("redirect:/produtos/list");
    }

}
