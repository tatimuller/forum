 package br.com.curso.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.curso.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.curso.forum.controller.dto.TopicoDTO;
import br.com.curso.forum.form.AtualizacaoTopicoForm;
import br.com.curso.forum.form.TopicoForm;
import br.com.curso.forum.modelo.Topico;
import br.com.curso.forum.repository.CursoRepository;
import br.com.curso.forum.repository.TopicoRepository;

@RestController  //essa anotação permite que não seja preciso colocar a anotação @ResponseBody em toodos os métidos da classe
@RequestMapping("/topicos")  //Todos os métodos começarão com /topicos
public class TopicosController {
	
	
	@Autowired   //Injeção de dependencia
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	
	
	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso){ //paremetro recebe argumento pela url
		
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();	
			return TopicoDTO.converter(topicos);
		}else{
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);	
			return TopicoDTO.converter(topicos);
		}
		
				
		
	}
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { //notação @ResquestBody informa que as informações vem do corpo da requisição e não da url
		Topico topico = form.converter(cursoRepository); //antes de passar as infos para salvar, tem que transformar em objeto.
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); //seguindo as boas práticas do modelo Rest, devolvendo código 201
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
		 Optional<Topico> topico = topicoRepository.findById(id);
		 if(topico.isPresent()) {
			 return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));
		 }
		 return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form ){
		
		Optional<Topico> optional = topicoRepository.findById(id);
		 if(optional.isPresent()) {
			 Topico topico = form.atualizar(id, topicoRepository);
			 return ResponseEntity.ok(new TopicoDTO(topico));
		 }
		 return ResponseEntity.notFound().build();	
		
		
		
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover (@PathVariable Long id){
		
		Optional<Topico> optional = topicoRepository.findById(id);
		 if(optional.isPresent()) {
			 topicoRepository.deleteById(id);
			 return ResponseEntity.ok().build();
		 }
		 return ResponseEntity.notFound().build();			
		
				
		
		
	}
	

}
