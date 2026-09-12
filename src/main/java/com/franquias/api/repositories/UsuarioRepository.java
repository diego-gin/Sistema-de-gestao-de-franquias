package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.Usuario;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    /**
     * Salva (insere ou atualiza) um usuário.
     *
     * Se o usuário tiver uma unidade associada apenas pelo ID (um "placeholder"
     * criado pelo Service, sem consultar o banco), essa referência é resolvida
     * aqui via em.getReference(), que cria um proxy gerenciado pela mesma
     * transação sem precisar carregar a entidade inteira — evitando tanto uma
     * consulta desnecessária quanto problemas de entidade "detached".
     */
    public Usuario salvar(Usuario usuario) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            if (usuario.getUnidade() != null && usuario.getUnidade().getId() != null) {
                UnidadeFranqueada referencia = em.getReference(
                        UnidadeFranqueada.class, usuario.getUnidade().getId());
                usuario.setUnidade(referencia);
            }

            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }

            em.getTransaction().commit();
            return usuario;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Usuario.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            List<Usuario> resultado = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getResultList();
            return resultado.stream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u ORDER BY u.nome", Usuario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
