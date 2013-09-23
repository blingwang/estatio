/*
 *
 *  Copyright 2012-2013 Eurocommercial Properties NV
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
package org.estatio.dom.party;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.estatio.dom.WithIntervalMutable;
import org.estatio.dom.contracttests.AbstractWithIntervalMutableContractTest_changeDates;
import org.estatio.dom.lease.Occupancy;

public class PartyRegistrationTest_changeDates extends AbstractWithIntervalMutableContractTest_changeDates<PartyRegistration> {

    private boolean locked;
    private PartyRegistration partyRegistration;

    @Before
    public void setUp() throws Exception {
        partyRegistration = withIntervalMutable;
    }
    
    protected PartyRegistration doCreateWithIntervalMutable(final WithIntervalMutable.Helper<PartyRegistration> mockChangeDates) {
        return new PartyRegistration() {
            @Override
            org.estatio.dom.WithIntervalMutable.Helper<PartyRegistration> getChangeDates() {
                return mockChangeDates;
            }
            @Override
            public boolean isLocked() {
                return locked;
            }
        };
    }
    
    @Test
    public void disableChangeDates_whenLocked() throws Exception {
        locked = true;
        assertThat(partyRegistration.disableChangeDates(null,null), is("Cannot modify when locked"));
    }
    
    @Test
    public void disableChangeDates_whenNotLocked() throws Exception {
        locked = false;
        assertThat(partyRegistration.disableChangeDates(null,null), is(nullValue()));
    }

    // //////////////////////////////////////

    @Test
    public void changeDatesDelegate() {
        partyRegistration = new PartyRegistration();
        assertThat(partyRegistration.getChangeDates(), is(not(nullValue())));
    }



}
