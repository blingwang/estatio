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
import org.estatio.dom.invoice.Invoice;
import org.estatio.dom.invoice.InvoiceStatus;
import org.estatio.dom.invoice.Invoices;
import org.estatio.dom.invoice.PaymentMethod;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.Leases;
import org.estatio.dom.party.Parties;
import org.estatio.dom.party.Party;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.fixture.asset.PropertiesAndUnitsFixture;
import org.estatio.fixture.invoice.InvoiceAndInvoiceItemForOxfPoison003;
import org.estatio.fixture.invoice.InvoicesAndInvoiceItemsFixture;
import org.estatio.fixture.lease.LeasesAndLeaseUnitsAndLeaseItemsAndLeaseTermsAndTagsAndBreakOptionsFixture;
import org.estatio.fixture.party.PersonsAndOrganisationsAndCommunicationChannelsFixture;
import org.estatio.integtests.EstatioIntegrationTest;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.isis.applib.fixturescripts.CompositeFixtureScript;

public class InvoiceTest_remove extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        scenarioExecution().install(new CompositeFixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                execute(new EstatioBaseLineFixture(), executionContext);
                execute("parties", new PersonsAndOrganisationsAndCommunicationChannelsFixture(), executionContext);
                execute("properties", new PropertiesAndUnitsFixture(), executionContext);
                execute("leases", new LeasesAndLeaseUnitsAndLeaseItemsAndLeaseTermsAndTagsAndBreakOptionsFixture(), executionContext);
                execute("invoices", new InvoicesAndInvoiceItemsFixture(), executionContext);
            }
        });
    }

    @Inject
    private Invoices invoices;
    @Inject
    private Parties parties;
    @Inject
    private Leases leases;
    
    private Party seller;
    private Party buyer;
    private Lease lease;

    @Before
    public void setUp() throws Exception {
        seller = parties.findPartyByReference(InvoiceAndInvoiceItemForOxfPoison003.SELLER_PARTY);
        buyer = parties.findPartyByReference(InvoiceAndInvoiceItemForOxfPoison003.BUYER_PARTY);
        lease = leases.findLeaseByReference(InvoiceAndInvoiceItemForOxfPoison003.LEASE);
    }
    
    @Test
    public void happyCase() throws Exception {
        // given
        List<Invoice> matchingInvoices = findMatchingInvoices(seller, buyer, lease);
        Assert.assertThat(matchingInvoices.size(), Is.is(1));
        Invoice invoice = matchingInvoices.get(0);
        // when
        invoice.remove();
        // then
        matchingInvoices = findMatchingInvoices(seller, buyer, lease);
        Assert.assertThat(matchingInvoices.size(), Is.is(0));
    }

    private List<Invoice> findMatchingInvoices(final Party seller, final Party buyer, final Lease lease) {
        return invoices.findMatchingInvoices(seller, buyer, PaymentMethod.DIRECT_DEBIT, lease, InvoiceStatus.NEW, InvoiceAndInvoiceItemForOxfPoison003.START_DATE);
    }

}
