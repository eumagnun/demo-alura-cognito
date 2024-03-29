package br.com.aluragames.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.aluragames.integration.CadastrarUsuario;
import br.com.aluragames.integration.ConfirmarCadastroUsuario;
import br.com.aluragames.integration.ListarUsuario;
import br.com.aluragames.integration.LogarUsuario;
import br.com.aluragames.model.DefaultResponse;
import br.com.aluragames.model.Usuario;

@Controller
@RequestMapping("/")
public class UsuarioController {

	@Autowired
	private LogarUsuario logarUsuario;

	@Autowired
	private CadastrarUsuario cadasUsuarioAction;

	@Autowired
	private ConfirmarCadastroUsuario confirmarCadastroUsuario;

	@Autowired
	private ListarUsuario listarUsuario;

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message = "Hello World";

	@GetMapping("/")
	public String home(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/login")
	public String usuarioLogin(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "login";
	}

	@GetMapping("/cadastro")
	public String usuarioCadastro(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "cadastro";
	}

	@GetMapping("/confirmarCadastro")
	public String confirmarCadastro(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "confirmarCadastro";
	}

	@GetMapping("/consulta")
	public ModelAndView usuarioLista() {
		List<Usuario> usuarios = this.listarUsuario.execute();
		return new ModelAndView("consulta", "usuarios", usuarios);
	}

	@GetMapping("/esqueciMinhaSenha")
	public String esqueciMinhaSenha(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "esqueciMinhaSenha";
	}

	@GetMapping("/confirmarNovaSenha")
	public String confirmarEsqueciMinhaSenha(Map<String, Object> model) {
		model.put("usuario", new Usuario());
		return "confirmarNovaSenha";
	}

	@PostMapping(params = "form")
	public ModelAndView login(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirect) {

		try {
			if (result.hasErrors()) {
				return new ModelAndView("form", "formErrors", result.getAllErrors());
			}
			DefaultResponse defaultResponse = this.logarUsuario.execute(usuario);

			tratarMensagemRetornoWs(redirect, defaultResponse);

			return new ModelAndView("redirect:/consulta");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			return new ModelAndView("redirect:/login");
		}

	}

	@PostMapping(params = "formNovoCadastro")
	public ModelAndView formNovoCadastro(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirect) {

		try {

			if (result.hasErrors()) {
				return new ModelAndView("formNovoCadastro", "formErrors", result.getAllErrors());
			}
			DefaultResponse defaultResponse = this.cadasUsuarioAction.execute(usuario);

			tratarMensagemRetornoWs(redirect, defaultResponse);

			return new ModelAndView("redirect:/confirmarCadastro");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			return new ModelAndView("redirect:/cadastro");
		}

	}

	@PostMapping(params = "formConfirmarCadastro")
	public ModelAndView formConfirmarCadastro(@Valid Usuario usuario, BindingResult result,
			RedirectAttributes redirect) {

		try {

			if (result.hasErrors()) {
				return new ModelAndView("formConfirmarCadastro", "formErrors", result.getAllErrors());
			}
			DefaultResponse defaultResponse = this.confirmarCadastroUsuario.execute(usuario);

			tratarMensagemRetornoWs(redirect, defaultResponse);

			return new ModelAndView("redirect:/login");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());

			return new ModelAndView("redirect:/confirmarCadastro");
		}

	}

	private void tratarMensagemRetornoWs(RedirectAttributes redirect, DefaultResponse defaultResponse) {
		if (defaultResponse.getStatus() != 0) {
			redirect.addFlashAttribute("globalMessageError",
					defaultResponse.getMessage() + " - Status=" + defaultResponse.getStatus());
		} else {
			redirect.addFlashAttribute("globalMessageSucess",
					defaultResponse.getMessage() + " - Status=" + defaultResponse.getStatus());
		}
	}

}
