package com.mdbank.integration.controller;

import com.mdbank.controller.GlobalDataController;
import com.mdbank.service.GlobalDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GlobalDataController.class)
public class GlobalDataControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GlobalDataService globalDataService;

    /**
     * Проверяется успешное добавление задания на обновление глобальных данных.
     */
    @Test
    @WithMockUser(username = "lolkek", roles = "ADMIN")
    public void testAddGlobalDataToUpdateTaskSuccessfully() throws Exception {
        mockMvc.perform(
                post("/rest/v1/global-data/update/SOLAR/26-06-2017/26-06-2017"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Данные успешно добавлены в очередь на обновление")));
        Thread.sleep(3000);
    }
}
