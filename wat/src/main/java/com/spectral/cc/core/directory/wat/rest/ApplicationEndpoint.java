package com.spectral.cc.core.directory.wat.rest;

import com.spectral.cc.core.directory.wat.consumer.DirectoryJPAProviderConsumer;
import com.spectral.cc.core.directory.base.model.organisational.Application;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 *
 */
@Path("/applications")
public class ApplicationEndpoint {

    @PersistenceContext(unitName = "cc-directory")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(Application entity) {
        em = DirectoryJPAProviderConsumer.getInstance().getDirectoryJpaProvider().createEM();
        em.getTransaction().begin();
        em.persist(entity);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return Response.created(UriBuilder.fromResource(ApplicationEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id)
    {
        em = DirectoryJPAProviderConsumer.getInstance().getDirectoryJpaProvider().createEM();
        Application entity = em.find(Application.class, id);
        if (entity == null) {
            em.close();
            return Response.status(Status.NOT_FOUND).build();
        }
        em.getTransaction().begin();
        em.remove(entity);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        em = DirectoryJPAProviderConsumer.getInstance().getDirectoryJpaProvider().createEM();
        TypedQuery<Application> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM Application a LEFT JOIN FETCH a.osInstances LEFT JOIN FETCH a.company WHERE a.id = :entityId ORDER BY a.id", Application.class);
        findByIdQuery.setParameter("entityId", id);
        Application entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            em.close();
            return Response.status(Status.NOT_FOUND).build();
        }
        em.close();
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/json")
    public List<Application> listAll() {
        em = DirectoryJPAProviderConsumer.getInstance().getDirectoryJpaProvider().createEM();
        final List<Application> results = em.createQuery("SELECT DISTINCT a FROM Application a LEFT JOIN FETCH a.osInstances LEFT JOIN FETCH a.company ORDER BY a.id", Application.class).getResultList();
        //em.close();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(Application entity) {
        em = DirectoryJPAProviderConsumer.getInstance().getDirectoryJpaProvider().createEM();
        em.getTransaction().begin();
        entity = em.merge(entity);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return Response.noContent().build();
    }
}