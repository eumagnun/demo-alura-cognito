package br.com.xptogames.controller;

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

import br.com.xptogames.integration.CadastrarUsuario;
import br.com.xptogames.integration.ConfirmarCadastroUsuario;
import br.com.xptogames.integration.ConfirmarEsqueciSenhaUsuario;
import br.com.xptogames.integration.EsqueciSenhaUsuario;
import br.com.xptogames.integration.ListarUsuario;
import br.com.xptogames.integration.LogarUsuario;
import br.com.xptogames.integration.TrocarSenhaUsuario;
import br.com.xptogames.model.UserWSResponse;
import br.com.xptogames.model.Usuario;


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
	private EsqueciSenhaUsuario esqueciSenhaUsuario;
	
	@Autowired
	private ConfirmarEsqueciSenhaUsuario confirmarEsqueciSenhaUsuario;
	
	@Autowired
	private ListarUsuario listarUsuario;
	
	@Autowired
	private TrocarSenhaUsuario trocarSenhaUsuario;

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
		List<Usuario> usuarios = this.listarUsuario.listAll();
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
			UserWSResponse userWSResponse = this.logarUsuario.login(usuario);

			tratarMensagemRetornoWs(redirect, userWSResponse);
			
			return new ModelAndView("redirect:/consulta");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			return new ModelAndView("redirect:/login");
		}

		
	}

	@PostMapping(params = "formEsqueciMinhaSenha")
	public ModelAndView esqueciMinhaSenha(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirect) {
		try {
			if (result.hasErrors()) {
				return new ModelAndView("formEsqueciMinhaSenha", "formErrors", result.getAllErrors());
			}
			UserWSResponse userWSResponse = this.esqueciSenhaUsuario.forgotPassword(usuario.getCpf());

			tratarMensagemRetornoWs(redirect, userWSResponse);
			return new ModelAndView("redirect:/confirmarNovaSenha");
		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			return new ModelAndView("redirect:/formEsqueciMinhaSenha");
		}


	}

	@PostMapping(params = "formConfirmarNovaSenha")
	public ModelAndView formConfirmarNovaSenha(@Valid Usuario usuario, BindingResult result,
			RedirectAttributes redirect) {

		try {

			if (result.hasErrors()) {
				return new ModelAndView("formConfirmarNovaSenha", "formErrors", result.getAllErrors());
			}
			UserWSResponse userWSResponse = this.confirmarEsqueciSenhaUsuario.confirmForgotPassword(usuario);

			tratarMensagemRetornoWs(redirect, userWSResponse);
			
			return new ModelAndView("redirect:/login");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			return new ModelAndView("redirect:/confirmarNovaSenha");
		}
		
	}

	@PostMapping(params = "formNovoCadastro")
	public ModelAndView formNovoCadastro(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirect) {

		try {

			if (result.hasErrors()) {
				return new ModelAndView("formNovoCadastro", "formErrors", result.getAllErrors());
			}
			UserWSResponse userWSResponse = this.cadasUsuarioAction.create(usuario);

			tratarMensagemRetornoWs(redirect, userWSResponse);
			
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
			UserWSResponse userWSResponse = this.confirmarCadastroUsuario.confirmSignUp(usuario);

			tratarMensagemRetornoWs(redirect, userWSResponse);
			
			return new ModelAndView("redirect:/login");

		} catch (Exception e) {
			redirect.addFlashAttribute("globalMessageError", "Ocorreu um erro: " + e.getMessage());
			
			return new ModelAndView("redirect:/confirmarCadastro");
		}
	
	}


	private void tratarMensagemRetornoWs(RedirectAttributes redirect, UserWSResponse userWSResponse) {
		if (userWSResponse.getStatus() != 1) {
			redirect.addFlashAttribute("globalMessageError",
					userWSResponse.getMessage() + " - Status=" + userWSResponse.getStatus());
		} else {
			redirect.addFlashAttribute("globalMessageSucess",
					userWSResponse.getMessage() + " - Status=" + userWSResponse.getStatus());
		}
	}

}
