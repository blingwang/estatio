/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.integtests.invoice;

import java.util.List;
import javax.inject.Inject;
import org.estatio.dom.invoice.InvoiceStatus;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.Leases;
import org.estatio.dom.lease.invoicing.InvoiceItemForLease;
import org.estatio.dom.lease.invoicing.InvoiceItemsForLease;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.asset.PropertiesAndUnitsForAll;
import org.estatio.fixture.invoice.InvoiceAndInvoiceItemForOxfPoison003;
import org.estatio.fixture.invoice.InvoicesAndInvoiceItemsForAll;
import org.estatio.fixture.lease.LeasesEtcForAll;
import org.estatio.fixture.party.PersonsAndOrganisationsAndCommunicationChannelsForAll;
import org.estatio.integtests.EstatioIntegrationTest;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.CompositeFixtureScript;

public class InvoiceItemsForLeaseTest_findByLeaseAndInvoiceStatus extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        scenarioExecution().install(new CompositeFixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                execute(new EstatioBaseLineFixture(), executionContext);
                execute("parties", new PersonsAndOrganisationsAndCommunicationChannelsForAll(), executionContext);
                execute("properties", new PropertiesAndUnitsForAll(), executionContext);
                execute("leases", new LeasesEtcForAll(), executionContext);
                execute("invoices", new InvoicesAndInvoiceItemsForAll(), executionContext);
            }
        });
    }

    @Inject
    private Leases leases;
    @Inject
    private InvoiceItemsForLease invoiceItemsForLease;

    @Test
    public void givenValidLeaseWithNewInvoiceItems() throws Exception {
        // given
        Lease lease = leases.findLeaseByReference(InvoiceAndInvoiceItemForOxfPoison003.LEASE);
        // when
        List<InvoiceItemForLease> invoiceItems = invoiceItemsForLease.findByLeaseAndInvoiceStatus(lease, InvoiceStatus.NEW);
        // then
        Assert.assertThat(invoiceItems.size(), Is.is(2));
    }

}