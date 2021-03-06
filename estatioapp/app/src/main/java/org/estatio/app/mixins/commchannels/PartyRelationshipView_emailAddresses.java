package org.estatio.app.mixins.commchannels;

import org.apache.isis.applib.annotation.Mixin;

import org.incode.module.communications.dom.impl.commchannel.CommunicationChannelOwner_emailAddressTitles;
import org.estatio.dom.party.relationship.PartyRelationshipView;

@Mixin
public class PartyRelationshipView_emailAddresses extends CommunicationChannelOwner_emailAddressTitles {
    public PartyRelationshipView_emailAddresses(final PartyRelationshipView party) {
        super(party.getTo(), ", ");
    }
}
