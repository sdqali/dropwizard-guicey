package ru.vyarus.dropwizard.guice.admin.rest;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * {@link AdminResource} annotation support. Denies rest method processing if accessed not from admin context.
 *
 * @author Vyacheslav Rusakov
 * @since 04.08.2015
 */
@AdminResource
public class AdminResourceFilter implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final Boolean isAdmin = (Boolean) requestContext.getProperty(AdminRestServlet.ADMIN_PROPERTY);
        if (isAdmin == null || !isAdmin) {
            throw new ForbiddenException();
        }
    }
}
