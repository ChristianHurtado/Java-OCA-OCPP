package eu.chargetime.ocpp.model.test;

import eu.chargetime.ocpp.PropertyConstraintException;
import eu.chargetime.ocpp.model.UnlockConnectorConfirmation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * ChargeTime.eu - Java-OCA-OCPP
 * <p>
 * MIT License
 * <p>
 * Copyright (C) 2016 Thomas Volden <tv@chargetime.eu>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class UnlockConnectorConfirmationTest {
    UnlockConnectorConfirmation confirmation;

    @Before
    public void setUp() throws Exception {
        confirmation = new UnlockConnectorConfirmation();
    }

    @Test
    public void setStatus_illegalString_throwsPropertyConstraintException() {
        // Given
        String illegal = "some status";

        try {
            // When
            confirmation.setStatus(illegal);

            Assert.fail("Expected PropertyConstraintException");
        } catch (PropertyConstraintException ex) {
            assertThat(ex.getFieldKey(), equalTo("status"));
            assertThat(ex.getFieldValue(), equalTo(illegal));
        }
    }

    @Test
    public void setStatus_unlocked_statusIsSet() throws Exception {
        // Given
        String unlockStatus = "Unlocked";

        // When
        confirmation.setStatus(unlockStatus);

        // Then
        assertThat(confirmation.getStatus(), equalTo(unlockStatus));
    }

    @Test
    public void setStatus_unlockFailed_statusIsSet() throws Exception {
        // Given
        String unlockStatus = "UnlockFailed";

        // When
        confirmation.setStatus(unlockStatus);

        // Then
        assertThat(confirmation.getStatus(), equalTo(unlockStatus));
    }

    @Test
    public void setStatus_notSupported_statusIsSet() throws Exception {
        // Given
        String unlockStatus = "NotSupported";

        // When
        confirmation.setStatus(unlockStatus);

        // Then
        assertThat(confirmation.getStatus(), equalTo(unlockStatus));
    }

    @Test
    public void validate_returnFalse() {
        // When
        boolean isValid = confirmation.validate();

        // Then
        assertThat(isValid, is(false));
    }

    @Test
    public void validate_statusIsSet_returnTrue() throws Exception {
        // Given
        confirmation.setStatus("Unlocked");

        // When
        boolean isValid = confirmation.validate();

        // Then
        assertThat(isValid, is(true));
    }
}