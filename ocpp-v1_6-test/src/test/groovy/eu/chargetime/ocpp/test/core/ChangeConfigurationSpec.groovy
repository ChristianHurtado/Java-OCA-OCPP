package eu.chargetime.ocpp.test.core

import eu.chargetime.ocpp.test.FakeCentralSystem
import eu.chargetime.ocpp.test.FakeChargePoint
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class ChangeConfigurationSpec extends Specification
{
    @Shared
    FakeCentralSystem centralSystem = FakeCentralSystem.instance;
    @Shared FakeChargePoint chargePoint = new FakeChargePoint();

    def setupSpec() {
        // When a Central System is running
        centralSystem.started();
    }

    def setup() {
        chargePoint.connect();
    }

    def cleanup() {
        chargePoint.disconnect();
    }

    def "Central System sends a ChangeConfiguration request and receives a response"() {
        def conditions = new PollingConditions(timeout: 1)

        when:
        centralSystem.sendChangeConfigurationRequest("key", "value");

        then:
        conditions.eventually {
            assert chargePoint.hasHandledChangeConfigurationRequest();
            assert centralSystem.hasReceivedChangeConfigurationConfirmation();
        }
    }
}
