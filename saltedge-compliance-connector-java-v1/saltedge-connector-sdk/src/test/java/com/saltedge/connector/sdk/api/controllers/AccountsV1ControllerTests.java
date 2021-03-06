/*
 * @author Constantin Chelban (constantink@saltedge.com)
 * Copyright (c) 2020 Salt Edge.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.saltedge.connector.sdk.api.controllers;

import com.saltedge.connector.sdk.api.mapping.AccountsResponse;
import com.saltedge.connector.sdk.api.mapping.DefaultRequest;
import com.saltedge.connector.sdk.api.mapping.EmptyJsonModel;
import com.saltedge.connector.sdk.config.Constants;
import com.saltedge.connector.sdk.models.persistence.Token;
import com.saltedge.connector.sdk.provider.ProviderApi;
import com.saltedge.connector.sdk.provider.models.AccountData;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;

public class AccountsV1ControllerTests {
    ProviderApi mockProviderService = Mockito.mock(ProviderApi.class);

    @Test
    public void basePathTest() {
        assertThat(AccountsV1Controller.BASE_PATH).isEqualTo(Constants.API_BASE_PATH + "/v1/accounts");
    }

    @Test
    public void whenList_thenReturnStatus200AndAccountsList() {
        // given
        List<AccountData> testData = getTestData();
        given(mockProviderService.getAccounts("1")).willReturn(testData);

        // when
        AccountsV1Controller controller = new AccountsV1Controller();
        controller.providerService = mockProviderService;
        ResponseEntity<AccountsResponse> result = controller.list(
                new Token("1"),
                new DefaultRequest()
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().data).isEqualTo(testData);
    }

    @Test
    public void whenRefresh_thenReturnStatus200AndEmptyResponse() {
        // when
        AccountsV1Controller controller = new AccountsV1Controller();
        controller.providerService = mockProviderService;
        ResponseEntity<EmptyJsonModel> result = controller.refresh(
                new Token("1"),
                new DefaultRequest()
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        verifyNoInteractions(mockProviderService);
    }

    private List<AccountData> getTestData() {
        ArrayList<AccountData> result = new ArrayList<>();
        result.add(new AccountData(
                "1",
                1000.0,
                1000.0,
                0.0,
                "EUR",
                "FK93RAND49838728129448",
                "account",
                "account",
                "619656558",
                false,
                "82-78-66",
                "active",
                "TBNFFR23PAR",
                new HashMap<>()
        ));
        return result;
    }
}
