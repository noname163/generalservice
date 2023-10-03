package com.cepa.generalservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cepa.generalservice.data.constants.SortType;
import com.cepa.generalservice.data.dto.response.CombinationResponse;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.services.combinationService.CombinationService;
import com.cepa.generalservice.utils.PageableUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class CombinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CombinationService combinationService;

    @MockBean
    private PageableUtil pageableUtil;

    @Test
    public void testGetSubjects() throws Exception {

        // Mock the behavior of subjectService.getSubjects to return a known response
        PaginationResponse<List<CombinationResponse>> expectedResponse = PaginationResponse
                .<List<CombinationResponse>>builder()
                .build();
        expectedResponse.setData(Collections.singletonList(CombinationResponse.builder().build()));
        expectedResponse.setTotalPage(1);
        expectedResponse.setTotalRow(1);
        when(combinationService.getCombination(0, 10, "field", SortType.ASC)).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/combinations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .param("field", "field")
                .param("sortType", "ASC"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(
                "{\"data\":[{\"id\":0,\"name\":null,\"url\":null,\"description\":null}],\"totalPage\":1,\"totalRow\":1}",
                mvcResult.getResponse().getContentAsString());
    }
}
