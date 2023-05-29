package com.skycast.controller;

import com.skycast.model.ApiData;
import com.skycast.service.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
class ApiControllerTest {


    ApiController apiController;

    // Will replace with custom behaviour
    @Mock
    private ApiService apiService;

    @BeforeEach
    public void setupController() {
        apiController = new ApiController(apiService);
    }

    @Test
    public void testGetCurrentData() throws Exception {
        ApiData testApiData = new ApiData(null, null, null);
        // Arrange
        Mockito.when(apiService.getApiAllData(any())).thenReturn(testApiData);
        // Act
        ApiData receivedCurrentData = apiController.getCurrentData(null);
        // Assert
        assertThat(receivedCurrentData).isSameAs(testApiData);
    }

}