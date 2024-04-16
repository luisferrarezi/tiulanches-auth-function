package br.com.fiap.tiulanches_auth_functions;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FunctionTest {

    private static final String RESPONSE = "{\"@odata.context\":\"https://graph.microsoft.com/v1.0/$metadata#users/$entity\",\"userPrincipalName\":\"teste@hotmail.com\",\"id\":\"1111111111111111\",\"displayName\":\"teste teste teste\",\"surname\":\"teste\",\"givenName\":\"teste\",\"preferredLanguage\":\"pt-BR\",\"mail\":\"teste@hotmail.com\",\"mobilePhone\":null,\"jobTitle\":null,\"officeLocation\":null,\"businessPhones\":[]}";

    @Test
    @SuppressWarnings("unchecked")
    void testHttpTriggerJava() throws Exception {

        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "Azure");
        doReturn(queryParams).when(req).getQueryParameters();

        final Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer teste");
        doReturn(headers).when(req).getHeaders();

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        HttpResponseMessage ret = new Function().run(req, context);
        assertEquals(HttpStatus.FORBIDDEN, ret.getStatus());

//        final HttpURLConnection conn = mock(HttpURLConnection.class);
//        final InputStream is = new ByteArrayInputStream(RESPONSE.getBytes());
//        doReturn(is).when(conn).getInputStream();
//        doReturn(200).when(conn).getResponseCode();
//        ret = new Function().run(req, context);
//        assertEquals(HttpStatus.OK, ret.getStatus());
    }
}
