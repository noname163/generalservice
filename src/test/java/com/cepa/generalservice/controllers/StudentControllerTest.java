package com.cepa.generalservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cepa.generalservice.data.dto.response.StudentResponse;
import com.cepa.generalservice.services.studentService.StudentInformationService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentInformationService studentInformationService;

<<<<<<< HEAD
    @MockBean
    private UserService userService;

=======
>>>>>>> fc86c36d34e788d6325f7bbdf3c4784da51007af
    @Test
    @WithMockUser(authorities = "STUDENT") 
    public void testGetStudentInformation() throws Exception {
        
        StudentResponse studentResponse = StudentResponse.builder().build();
        studentResponse.setEmail("student@example.com");
        studentResponse.setFullName("Student Name");

        
        when(studentInformationService.getStudentByEmail("student@example.com")).thenReturn(studentResponse);


        MvcResult result = mockMvc.perform(get("/api/student/student@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        StudentResponse responseStudent = new ObjectMapper().readValue(responseContent, StudentResponse.class);

        assertEquals("student@example.com", responseStudent.getEmail());
        assertEquals("Student Name", responseStudent.getFullName());

        verify(studentInformationService, times(1)).getStudentByEmail("student@example.com");
    }
}
