package org.restbucks.tdd.web.rest.assembler;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.modelmapper.ModelMapper;
import org.restbucks.tdd.domain.catalog.Catalog;
import org.restbucks.tdd.domain.ordering.Order;
import org.restbucks.tdd.web.rest.CatalogRestController;
import org.restbucks.tdd.web.rest.OrderRestController;
import org.restbucks.tdd.web.rest.resource.CatalogResource;
import org.restbucks.tdd.web.rest.resource.OrderResource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderResourceAssembler extends ResourceAssemblerSupport<Order, OrderResource> {

    private final ModelMapper modelMapper;

    public OrderResourceAssembler() {
        super(OrderRestController.class, OrderResource.class);
        this.modelMapper = new ModelMapper();
    }

    @Override
    public OrderResource toResource(Order entity) {
        OrderResource resource = modelMapper.map(entity, OrderResource.class);
        resource.add(linkTo(methodOn(OrderRestController.class).findOne(entity.getId().getValue()))
            .withSelfRel());
        return resource;
    }

    public Resources<OrderResource> toHalResources(List<Order> all) {
        return new Resources<>(toResources(all),
            linkTo(ControllerLinkBuilder.methodOn(CatalogRestController.class).all()).withSelfRel());
    }
}
