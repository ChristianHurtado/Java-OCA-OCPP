package eu.chargetime.ocpp.test;

import eu.chargetime.ocpp.Client;
import eu.chargetime.ocpp.Session;
import eu.chargetime.ocpp.SessionEvents;
import eu.chargetime.ocpp.feature.Feature;
import eu.chargetime.ocpp.feature.profile.Profile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.TestConfirmation;
import eu.chargetime.ocpp.utilities.TestUtilities;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/*
 ChargeTime.eu - Java-OCA-OCPP
 Copyright (C) 2015-2016 Thomas Volden <tv@chargetime.eu>

 MIT License

 Copyright (c) 2016 Thomas Volden

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
public class ClientTest extends TestUtilities {
    private Client client;
    private SessionEvents eventHandler;

    @Mock
    private Session session = mock(Session.class);
    @Mock
    private Profile profile = mock(Profile.class);
    @Mock
    private Feature feature = mock(Feature.class);
    @Mock
    private Request request = mock(Request.class);

    @Before
    public void setup() {
        when(request.validate()).thenReturn(true);
        doReturn(request.getClass()).when(feature).getRequestType();
        doReturn(TestConfirmation.class).when(feature).getConfirmationType();
        when(feature.getAction()).thenReturn(null);
        doAnswer(invocation -> eventHandler = invocation.getArgumentAt(1, SessionEvents.class)).when(session).open(any(), any());

        client = new Client(session) {
        };

        when(profile.getFeatureList()).thenReturn(aList(feature));
        client.addFeatureProfile(profile);
    }

    @Test
    public void connect_connects() {
        // Given
        String someUrl = "localhost";

        // When
        client.connect(someUrl);

        // Then
        verify(session, times(1)).open(eq(someUrl), anyObject());
    }

    @Test
    public void send_aMessage_isCommunicated() throws Exception {
        // When
        client.send(request);

        // Then
        verify(session, times(1)).sendRequest(anyString(), eq(request));
    }

    @Test
    public void responseReceived_aMessageWasSend_PromiseIsCompleted() throws Exception {
        // Given
        String someUniqueId = "Some id";
        when(session.sendRequest(any(), any())).thenReturn(someUniqueId);

        // When
        client.connect(null);
        CompletableFuture<Confirmation> promise = client.send(request);
        eventHandler.handleConfirmation(someUniqueId, null);

        // Then
        assertThat(promise.isDone(), is(true));
    }

    @Test
    public void handleRequest_returnsConfirmation() {
        // Given
        client.connect(null);
        when(feature.handleRequest(0, request)).thenReturn(new TestConfirmation());

        // When
        Confirmation conf = eventHandler.handleRequest(request);

        // Then
        assertThat(conf, instanceOf(TestConfirmation.class));
    }

    @Test
    public void handleRequest_callsFeatureHandleRequest() {
        // Given
        client.connect(null);

        // When
        eventHandler.handleRequest(request);

        // Then
        verify(feature, times(1)).handleRequest(eq(0), eq(request));
    }

    @Test
    public void send_aMessage_validatesMessage() throws Exception {
        // When
        client.send(request);

        // Then
        verify(request, times(1)).validate();
    }

}