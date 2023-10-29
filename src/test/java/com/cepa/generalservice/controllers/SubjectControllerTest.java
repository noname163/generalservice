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
import com.cepa.generalservice.data.constants.StateType;
import com.cepa.generalservice.data.dto.response.PaginationResponse;
import com.cepa.generalservice.data.dto.response.SubjectResponse;
import com.cepa.generalservice.services.subjectService.SubjectService;
import com.cepa.generalservice.utils.PageableUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class SubjectControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private SubjectService subjectService;

        @MockBean
        private PageableUtil pageableUtil;

        @Test
        public void testGetSubjects() throws Exception {

                // Mock the behavior of subjectService.getSubjects to return a known response
                PaginationResponse<List<SubjectResponse>> expectedResponse = PaginationResponse
                                .<List<SubjectResponse>>builder()
                                .build();
                expectedResponse.setData(Collections.singletonList(SubjectResponse.builder().build()));
                expectedResponse.setTotalPage(1);
                expectedResponse.setTotalRow(1);
                when(subjectService.getSubjects(0, 10, "field", SortType.ASC, StateType.TRUE))
                                .thenReturn(expectedResponse);

                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/subjects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "10")
                                .param("field", "field")
                                .param("sortType", "ASC"))
                                .andExpect(status().isOk())
                                .andReturn();
                assertEquals("{\"data\":[{\"id\":0,\"name\":null,\"url\":null,\"description\":null}],\"totalPage\":1,\"totalRow\":1}",
                                mvcResult.getResponse().getContentAsString());
        }

}
