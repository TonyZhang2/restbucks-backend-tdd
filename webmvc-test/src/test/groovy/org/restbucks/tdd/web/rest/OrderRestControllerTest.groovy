package org.restbucks.tdd.web.rest

import org.junit.Test
import org.restbucks.tdd.domain.ordering.Location
import org.restbucks.tdd.domain.ordering.Order
import org.restbucks.tdd.domain.ordering.OrderRepository
import org.restbucks.tdd.web.AbstractWebMvcTest
import org.restbucks.tdd.web.rest.assembler.OrderResourceAssembler
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.payload.FieldDescriptor

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.mockito.BDDMockito.given
import static org.restbucks.tdd.domain.ordering.Location.IN_STORE
import static org.restbucks.tdd.domain.ordering.Location.TAKE_AWAY
import static org.restbucks.tdd.domain.ordering.Order.newOrder
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [
    OrderRestController,
    OrderResourceAssembler
])
class OrderRestControllerTest extends AbstractWebMvcTest {

    @MockBean
    private OrderRepository orderRepository

    @Test
    void "it should return an order"() {

        def order = newOrder()
        order.with(TAKE_AWAY)

        given(orderRepository.findOne(order.id)).willReturn(order)

        // @formatter:off
        this.mockMvc.perform(get("/rel/orders/${order.id.value}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("location", is(order.location.name())))
            .andExpect(jsonPath("status", is(order.status.name())))
            .andDo(document('ordering/find_order_by_id',
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("location")
                    .description("The location of the order, should be one of ${Location.values()}"),
                fieldWithPath("status")
                    .description("The status of the order, should be one of ${Order.Status.values()}"),
                subsectionWithPath("_links").ignored()//validate by links() block
            ),
            links(halLinks(),
                linkWithRel("self")
                    .description("link to refresh the order")
            )
        ))
        // @formatter:on
    }

    @Test
    void "it should return orders searched by status"() {

        def order1 = newOrder()
        def order2 = newOrder()
        order1.with(TAKE_AWAY)
        order2.with(IN_STORE)

        def statusName = order1.status.name()

        given(orderRepository.findByStatus(statusName)).willReturn(Arrays.asList(order1, order2))

        // @formatter:off
        this.mockMvc.perform(get("/rel/orders/search?status=${statusName}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.orders[0].location", is(order1.location.name())))
            .andExpect(jsonPath("_embedded.orders[0].status", is(order1.status.name())))
        // @formatter:on


//
//        FieldDescriptor[] orderFields = [
//            fieldWithPath("location")
//                .description("The location of the order, should be one of ${Location.values()}"),
//            fieldWithPath("status")
//                .description("The status of the order, should be one of ${Order.Status.values()}"),
//            subsectionWithPath("links").ignored()
//        ]
//
//
//
//        // @formatter:off
//        this.mockMvc.perform(get("/rel/orders/search?status=${statusName}"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("\$", hasSize(2)))
//            .andExpect(jsonPath("[0].location", is(order1.location.name())))
//            .andExpect(jsonPath("[1].location", is(order2.location.name())))
//            .andExpect(jsonPath("[0].status", is(order1.status.name())))
//            .andExpect(jsonPath("[1].status", is(order2.status.name())))
//            .andDo(document('ordering/search_order',
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                responseFields(
//                    fieldWithPath("[]").description("An array of orders"))
//                    .andWithPrefix("[].", orderFields)
//                ,
//                links(atomLinks(),
//                    linkWithRel("self")
//                        .description("link to refresh the order")
//                )
//            ))
        // @formatter:on
    }

}
