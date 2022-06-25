package br.com.curso.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.curso.forum.modelo.Curso;
import br.com.curso.forum.modelo.Topico;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nome);

	

}
